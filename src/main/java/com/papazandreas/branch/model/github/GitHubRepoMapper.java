package com.papazandreas.branch.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitHubRepoMapper {
    private String name;

    @JsonProperty("html_url")
    private String htmlUrl;

}
