package com.papazandreas.branch.model.github;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GitHubEntityResponse {
    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("display_name")
    private String displayName;

    private String avatar;

    @JsonProperty("geo_location")
    private String geoLocation;

    private String email;

    private String url;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;

    private List<GitHubRepoEntity> repos;
}
