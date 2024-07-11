package com.repo_generator.controllers;

import com.repo_generator.entity.GitRepository;
import com.repo_generator.services.SourceService;
import com.repo_generator.utils.SourceUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//@RestController
//@RequestMapping("/repositories/source")
//public class SourceController {
//    private final SourceService service;
//
//    public SourceController(SourceService service) {
//        this.service = service;
//    }
//
//    @GetMapping
//    @Operation(summary = "Getting a list of repositories")
//    @ApiResponse(responseCode = "200", description = "Success")
//    public List<GitRepository> getRepositories() {
//        return service.getRepositoriesHandler();
//    }
//
//    @PutMapping("/{repositoryName}")
//    @Operation(summary = "")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Successfully updated"),
//            @ApiResponse(responseCode = "401", description = "Incorrect Github creds"),
//            @ApiResponse(responseCode = "404", description = "Repository doesn't exits"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    public ResponseEntity<String> updateLocalRepository(
//            @Parameter(description = "Repository Name")
//            @PathVariable
//            String repositoryName
//    ) {
//        return service.updateLocalRepositoryHandler(repositoryName);
//    }
//
//    @PutMapping
//    @Operation(summary = "")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Successfully synchronized"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    public ResponseEntity<String> SyncLocalRepositories() {
//        return service.SyncLocalRepositoriesHandler();
//    }
//}

@Controller
@RequestMapping("/api/source")
public class SourceController{
    private final SourceService service;
    private final SourceUtils utils;

    public SourceController(SourceService service, SourceUtils utils) {
        this.service = service;
        this.utils = utils;
    }

    @GetMapping("/repositories")
    public String index(Model model) {
        List<GitRepository> repositories = service.getRepositoriesHandler();
        model.addAttribute("repositories", repositories);
        return "components/repositories::repositories";
    }
//    @GetMapping("/localrepo")
//    public String getLocalRepositories(Model model) {
//        File[] files = utils.getLocalRepositories();
//        List<GitRepository> repositories = new ArrayList<>();
//        for(File file : files){
//            GitRepository repository = new GitRepository();
//            repository.setName(file.getName());
//            repositories.add(repository);
//        }
//        model.addAttribute("repositories", repositories);
//        return "components/repositories::repositories";
//    }

    @PutMapping("/{repositoryName}")
    public String updateLocalRepository(
            @PathVariable
            String repositoryName,
            Model model
    ){
        ResponseEntity<String> message =  service.updateLocalRepositoryHandler(repositoryName);
        model.addAttribute("message", message.getBody());
        return "components/notification::notification";
    }
    @PutMapping
    public String SyncLocalRepositories(
            Model model
    ){
        ResponseEntity<String> message = service.SyncLocalRepositoriesHandler();
        model.addAttribute("message", message.getBody());
        return "components/notification::notification";
    }
}
