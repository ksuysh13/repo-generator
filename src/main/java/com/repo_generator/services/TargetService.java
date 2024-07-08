package com.repo_generator.services;

import com.repo_generator.entity.GitRepository;
import com.repo_generator.requests.TargetRequest;
import com.repo_generator.utils.TargetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class TargetService {
    private static final Logger logger = LoggerFactory.getLogger(TargetService.class);
    private final TargetRequest request;
    private final TargetUtils utils;

    public TargetService(
            TargetRequest request,
            TargetUtils utils
    ) {
        this.request = request;
        this.utils = utils;
    }

    private ResponseEntity<String> processUpdateRepository(File repositoryDir, GitRepository repository) {
        if (repositoryDir.exists()) {
            String repositoryName = repositoryDir.getName();
            if (repository == null) {
                logger.debug("Repository doesn't exist. Call a createRepository method");
                request.createRepositoryRequest(repositoryName);
            }
            try {
                logger.debug("Call a push method");
                boolean success = utils.updateTargetRepository(repositoryDir, repositoryName);
                if (!success) {
                    logger.error("Error push repository {}", repositoryName);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error push repository");
                }
            } catch (IOException e) {
                logger.error("Error push repository {} {}", e.getMessage(), repositoryName);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error push repository");
            }
        } else {
            logger.error("Repository doesn't cloned locally");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Repository doesn't cloned locally");
        }
        return null;
    }

    public ResponseEntity<String> updateTargetRepositoryHandler(String repositoryName) {
        logger.info("Starting updateTarget method");
        File repositoryDir = utils.getLocalRepository(repositoryName);
        GitRepository repository = request.getRepositoryRequest(repositoryName);
        ResponseEntity<String> update = processUpdateRepository(repositoryDir, repository);
        if (update != null) return update;
        return ResponseEntity.ok("Repository updated successfully");
    }

    public ResponseEntity<String> syncTargetRepositoriesHandler() {
        logger.info("Starting syncTargetRepositories method");
        File[] repositoryDirs = utils.getLocalRepositories();
        if (repositoryDirs == null) {
            logger.debug("repositories dir is empty");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Repositories dir is empty");
        }
        for (File repositoryDir : repositoryDirs){
            GitRepository repository = request.getRepositoryRequest(repositoryDir.getName());
            ResponseEntity<String> update = processUpdateRepository(repositoryDir, repository);
            if (update != null) return update;
        }
        return ResponseEntity.ok("Repositories synchronized successfully");
    }
}
