package com.repo_generator.utils;

import com.repo_generator.config.SourceConfig;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class SourceUtils extends GitUtils {
    private static final Logger logger = LoggerFactory.getLogger(SourceUtils.class);
    @Autowired
    private SourceConfig config;

    private CredentialsProvider getCredentials() {
        return new UsernamePasswordCredentialsProvider(
                config.getUserName(),
                config.getToken()
        );
    }

    public boolean updateLocalRepository(File repositoryDir) throws IOException {
        try (Git git = Git.open(repositoryDir)) {
//            git.checkout().setName("main").call();
            PullResult pr = git.pull()
                    .setRemote("origin")
                    .setCredentialsProvider(getCredentials())
                    .call();
            return true;
        } catch (GitAPIException e) {
            logger.error("Error update repository {}", e.getMessage());
            return false;
        }
    }

    public boolean cloneRepository(File repositoryDir, String repositoryName) {
        try (
            Git git = Git.cloneRepository()
                    .setRemote("origin")
                    .setCredentialsProvider(getCredentials())
                    .setURI(String.format(
                            config.getUrl() + "%s/%s",
                            config.getUserName(),
                            repositoryName
                    ))
                    .setCloneAllBranches(true)
                    .setDirectory(repositoryDir)
                    .call();
        ) {
            return true;
        } catch (GitAPIException e) {
            logger.error("Error clone repository {}", e.getMessage());
            return false;
        }
    }
}
