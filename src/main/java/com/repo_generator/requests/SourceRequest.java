package com.repo_generator.requests;

import com.repo_generator.config.SourceConfig;
import com.repo_generator.entity.GitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Component
public class SourceRequest extends AbcRequest {
    @Autowired
    private SourceConfig config;


    @Override
    protected HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + config.getToken());
        return headers;
    }

    @Override
    public List<GitRepository> getRepositoriesRequest() {
        URI uri = URI.create(config.getApi() + "user/repos");
        ResponseEntity<GitRepository[]> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(createHeaders()),
                new ParameterizedTypeReference<GitRepository[]>(){}
        );
        return Arrays.asList(response.getBody());
    }

    @Override
    public GitRepository getRepositoryRequest(String repositoryName) {
        return getRepositoriesRequest()
                .stream()
                .filter(r -> r.getName().equals(repositoryName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void createRepositoryRequest(String repositoryName) {
        return;
    }
}
