package com.repo_generator.services;

import com.repo_generator.entity.GitRepository;
import com.repo_generator.requests.SourceRequest;
import com.repo_generator.utils.SourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class SourceService {
    private static final Logger logger = LoggerFactory.getLogger(SourceService.class);
    private final SourceRequest request;
    private final SourceUtils utils;

    public SourceService(
            SourceRequest request,
            SourceUtils utils
    ) {
        this.request = request;
        this.utils = utils;
    }

    private ResponseEntity<String> processUpdateRepository(File repositoryDir, GitRepository repository) {
        if (repositoryDir.exists()) {
            logger.debug("Call a pull method");
            try{
                boolean success = utils.updateLocalRepository(repositoryDir);
                if (!success) {
                    logger.error("Error cloning repository");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error cloning repository");
                }
            } catch (IOException e) {
                logger.error("Error updating repository {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error updating repository " + e.getMessage());
            }
        } else {
            logger.debug("Call a clone method");
            repositoryDir.mkdir();
            boolean success = utils.cloneRepository(repositoryDir, repository.getName());
            if (!success) {
                logger.error("Incorrect Github credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Github credentials ");
            }
            logger.debug("Repository successfully cloned");
            return null;
        }
        return null;
    }

    public List<GitRepository> getRepositoriesHandler() {
        logger.info("Success getRepository method");
        return request.getRepositoriesRequest();
    }

    public ResponseEntity<String> updateLocalRepositoryHandler(String repositoryName) {
        logger.info("Starting updateLocal method");
        File repositoryDir = utils.getLocalRepository(repositoryName);
        GitRepository repository = request.getRepositoryRequest(repositoryName);
        if (repository == null) {
            logger.error("Repository doesn't exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Repository doesn't exits");
        }
        ResponseEntity<String> update = processUpdateRepository(repositoryDir, repository);
        if (update != null) return update;
        return ResponseEntity.ok("Repository updated successfully");
    }

    public ResponseEntity<String> SyncLocalRepositoriesHandler() {
        logger.info("Starting syncLocalRepositories method");
        List<GitRepository> repositories = request.getRepositoriesRequest();
        for (GitRepository repository: repositories) {
            File repositoryDir = utils.getLocalRepository(repository.getName());
            ResponseEntity<String> update = processUpdateRepository(repositoryDir, repository);
            if (update != null) {
                logger.error("Error update {} repo", repository.getName());
                return update;
            }
        }
        return ResponseEntity.ok("Repositories synchronized successfully");
    }
}
