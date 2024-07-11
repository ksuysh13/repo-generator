package com.repo_generator.utils;

import com.repo_generator.config.TargetConfig;
import com.repo_generator.services.TargetService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.URIish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class TargetUtils extends GitUtils {
    @Autowired
    private TargetConfig config;
    private static final Logger logger = LoggerFactory.getLogger(TargetService.class);

    public boolean updateTargetRepository(File repositoryDir, String repositoryName) throws IOException {
        try (Git git = Git.open(repositoryDir)) {
            git.remoteAdd().setName(config.getName()).setUri(new URIish(String.format(
                    config.getUrl() + "%s/%s",
                    config.getUserName(),
                    config.getToken(),
                    config.getWorkspace(),
                    repositoryName
            ))).call();
            git.push().setRemote(config.getName()).setPushAll().setPushTags().call();
            return true;
        } catch (GitAPIException | URISyntaxException e) {
            logger.error("Error update repository {}", e.getMessage());
            return false;
        }
    }
}
