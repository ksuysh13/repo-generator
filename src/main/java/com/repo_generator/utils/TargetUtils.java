package com.repo_generator.utils;

import com.repo_generator.config.TargetConfig;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.URIish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class TargetUtils extends GitUtils {
    @Autowired
    private TargetConfig config;

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
            return false;
        }
    }
}
