package com.papazandreas.branch.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitHubUserMapper {
    private String login;

    private String name;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    private String location;

    private String email;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("created_at")
    private String createdAt;
}