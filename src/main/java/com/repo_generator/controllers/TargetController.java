package com.repo_generator.controllers;

import com.repo_generator.services.TargetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/repositories/target")
//public class TargetController {
//    private final TargetService service;
//
//    public TargetController(TargetService service) {
//        this.service = service;
//    }
//
//    @PutMapping("/{repositoryName}")
//    @Operation()
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Successfully updated"),
//            @ApiResponse(responseCode = "404", description = "Repository doesn't exist locally"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    public ResponseEntity<String> updateTargetRepo(
//            @Parameter(description = "Repository name")
//            @PathVariable
//            String repositoryName
//    ) {
////        status, body = service.updateTargetRepositoryHandler(repositoryName);
////        return ResponseEntity.status(status).body(body);
//        return service.updateTargetRepositoryHandler(repositoryName);
//    }
//
//    @PutMapping("")
//    @Operation()
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Successfully synchronized"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    public ResponseEntity<String> syncTargetRepositories() {
//        return service.syncTargetRepositoriesHandler();
//    }
//}

@Controller
@RequestMapping("/api/target")
public class TargetController {
        private final TargetService service;

    public TargetController(TargetService service) {
        this.service = service;
    }

    @PutMapping("/{repositoryName}")
    public String updateTargetRepository(
            @PathVariable
            String repositoryName,
            Model model
    ){
        ResponseEntity<String> message =  service.updateTargetRepositoryHandler(repositoryName);
        model.addAttribute("message", message.getBody());
        return "components/notification::notification";
    }

    @PutMapping
    public String syncTargetRepositories(
            Model model
    ) {
        ResponseEntity<String> message = service.syncTargetRepositoriesHandler();
        model.addAttribute("message", message.getBody());
        return "components/notification::notification";
    }
}