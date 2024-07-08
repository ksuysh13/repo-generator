package com.repo_generator.requests;

import com.repo_generator.entity.GitRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public abstract class GitApiRequest {
    protected RestTemplate restTemplate;

    public GitApiRequest() {
        this.restTemplate = new RestTemplate();
    }

    protected abstract HttpHeaders createHeaders();

    public abstract List<GitRepository> getRepositoriesRequest();

    public abstract GitRepository getRepositoryRequest(String repositoryName);

    public abstract void createRepositoryRequest(String repositoryName);
}
