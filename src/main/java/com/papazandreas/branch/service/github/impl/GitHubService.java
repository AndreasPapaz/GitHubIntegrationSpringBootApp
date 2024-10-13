package com.papazandreas.branch.service.github.impl;

import com.papazandreas.branch.model.github.GitHubEntityResponse;

public interface GitHubService {
    GitHubEntityResponse getGitHubUser(String userName) throws Exception;
}
