package com.papazandreas.branch.controllers.github;

import com.papazandreas.branch.model.github.GitHubEntityResponse;
import com.papazandreas.branch.service.github.GitHubServiceImpl;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller responsible for handling GitHub user-related HTTP requests.
 * <p>
 * This controller provides endpoints to interact with GitHub user data.
 * It leverages the {@link GitHubServiceImpl} to fetch and process data from the GitHub API.
 * </p>
 * <p>
 * All endpoints defined in this controller are prefixed with {@code /github-user}.
 * </p>
 *
 * @Andreas Papazafeiropoulos
 * @version 1.0
 * @since 2024-04-27
 */
@RestController
@RequestMapping("/github-user")
public class GitHubUserController {

    private final String USER_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";

    @Resource
    private GitHubServiceImpl gitHubService;

    @GetMapping("/{username}")
    public ResponseEntity<GitHubEntityResponse> getUser(@PathVariable
                                                        @NotBlank(message = "Username must not be empty")
                                                        @Pattern(regexp = USER_NAME_PATTERN,
                                                                message = "Invalid GitHub username format")
                                                        String username) {

        GitHubEntityResponse gitHubEntityResponse = gitHubService.getGitHubUser(username);
        return new ResponseEntity<>(gitHubEntityResponse, HttpStatus.OK);
    }
}
