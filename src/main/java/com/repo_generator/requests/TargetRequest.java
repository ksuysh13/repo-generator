package com.repo_generator.requests;

import com.repo_generator.config.TargetConfig;
import com.repo_generator.entity.GitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Component
public class TargetRequest extends AbcRequest {
    @Autowired
    private TargetConfig config;

    @Override
    protected HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(
                (config.getUserName() + ":" + config.getToken()).getBytes()
        ));
        return headers;
    }

    @Override
    public List<GitRepository> getRepositoriesRequest() {
        try {
            URI uri = URI.create(config.getApi() + "repositories/ssau_practice");
            ResponseEntity<GitRepository[]> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    new HttpEntity<>(createHeaders()),
                    new ParameterizedTypeReference<GitRepository[]>() {
                    }
            );
            return Arrays.asList(response.getBody());
        } catch (RestClientException e) {
            return null;
        }
    }

    @Override
    public GitRepository getRepositoryRequest(String repositoryName) {
        try {
            URI uri = URI.create(config.getApi() + String.format(
                    "repositories/ssau_practice/%s",
                    repositoryName
            ));
            ResponseEntity<GitRepository> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    new HttpEntity<>(createHeaders()),
                    new ParameterizedTypeReference<GitRepository>() {
                    }
            );
            return response.getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    @Override
    public void createRepositoryRequest(String repositoryName) {
        URI uri = URI.create(config.getApi() + String.format(
                "repositories/ssau_practice/%s",
                repositoryName
        ));
        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(
                """
                {
                    "scm": "git",
                    "project": {
                        "key": "TES"
                    },
                    "is_private": true
                }
                """,
                headers
        );
        ResponseEntity<String> response = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<String>() {
                }
        );
    }
}
