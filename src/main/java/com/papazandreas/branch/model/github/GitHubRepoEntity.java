package com.papazandreas.branch.model.github;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GitHubRepoEntity {
    private String name;
    private String url;
}
