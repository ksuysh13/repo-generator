package com.repo_generator.utils;

import com.repo_generator.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public abstract class GitUtils {
    @Autowired
    private AppConfig config;

    public File[] getLocalRepositories() {
        return new File(config.getBaseDir()).listFiles(File::isDirectory);
    }

    public File getLocalRepository(String repositoryName) {
        return new File(config.getBaseDir(), repositoryName);
    }
}
