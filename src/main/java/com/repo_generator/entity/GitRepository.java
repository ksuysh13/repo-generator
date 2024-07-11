package com.repo_generator.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.File;

@Builder
@Data
@Schema(description = "Github repository schema")
public class GitRepository {
    @Schema(title = "Name", defaultValue = "")
    private String name;
    @Schema(title = "description", defaultValue = "")
    private String description;
    @Schema(title = "url", defaultValue = "")
    private String url;
    @Builder.Default
    private boolean isCloned = false;
}
