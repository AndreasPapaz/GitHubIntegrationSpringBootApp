package com.papazandreas.branch.service.github;

import com.papazandreas.branch.model.github.GitHubEntityResponse;
import com.papazandreas.branch.model.github.GitHubRepoMapper;
import com.papazandreas.branch.model.github.GitHubUserMapper;

import org.assertj.core.api.CompletableFutureAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.naming.CommunicationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class GitHubServiceImplTest {
    @InjectMocks
    private GitHubServiceImpl gitHubService;

    @Mock
    private RestTemplate restTemplate;


    private GitHubUserMapper mockUser;
    private GitHubRepoMapper[] mockRepoList;
    private GitHubRepoMapper mockRepo;
    private GitHubRepoMapper mockRepo2;

    private ResponseEntity<GitHubUserMapper> userResponseEntity;
    private ResponseEntity<GitHubRepoMapper[]> reposResponseEntity;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        mockUser = new GitHubUserMapper();
        mockUser.setLogin("octocat");
        mockUser.setName("The Octocat");
        mockUser.setAvatarUrl("https://avatars.githubusercontent.com/u/583231?v=4");
        mockUser.setLocation("San Francisco");
        mockUser.setEmail(null);
        mockUser.setHtmlUrl("https://github.com/octocat");
        mockUser.setCreatedAt("2011-01-25T18:44:36Z");

        mockRepo = new GitHubRepoMapper();
        mockRepo.setName("repo1");
        mockRepo.setHtmlUrl("https://github.com/octocat/repo1");

        mockRepo2 = new GitHubRepoMapper();
        mockRepo2.setName("repo2");
        mockRepo2.setHtmlUrl("https://github.com/octocat/repo2");

        mockRepoList = new GitHubRepoMapper[]{mockRepo, mockRepo2};

        userResponseEntity = new ResponseEntity<>(mockUser, HttpStatus.OK);
        reposResponseEntity = new ResponseEntity<>(mockRepoList, HttpStatus.OK);

        setPrivateField(gitHubService, "gitHubUserApiUrl", "https://api.mockgithub.com/user");
        setPrivateField(gitHubService, "gitHubReposApiUrl", "https://api.mockgithub.com/repos");
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void getGitHubUser_validUserName_Success() {
        String username = "octocat";

        // Mock API request to GitHub
        when(restTemplate.getForObject(anyString(), eq(GitHubUserMapper.class), eq(username)))
                .thenReturn(mockUser);
        when(restTemplate.getForObject(anyString(), eq(GitHubRepoMapper[].class), eq(username)))
                .thenReturn(mockRepoList);

        // Branch (Demo) gitHubService impl
        GitHubEntityResponse response = gitHubService.getGitHubUser(username);

        // Assertions
        assertNotNull(response);
        assertEquals("octocat", response.getUserName());
        assertEquals("The Octocat", response.getDisplayName());

        // todo - we can assert mattern matching for github urls
        assertEquals("https://avatars.githubusercontent.com/u/583231?v=4", response.getAvatar());
        assertEquals("San Francisco", response.getGeoLocation());
        assertNull(response.getEmail());

        // todo - we can assert mattern matching for github urls
        assertEquals("https://github.com/octocat", response.getUrl());

        String createdAt = response.getCreatedAt();
        String regexPattern = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
        assertTrue(Pattern.matches(regexPattern, createdAt), "Date format should match yyyy-MM-dd HH:mm:ss");

        assertNotNull(response.getRepos());
        assertEquals(2, response.getRepos().size());
        assertEquals("repo1", response.getRepos().get(0).getName());
        // todo - we can assert mattern matching for github urls
        assertEquals("https://github.com/octocat/repo1", response.getRepos().get(0).getUrl());
        assertEquals("repo2", response.getRepos().get(1).getName());
        // todo - we can assert mattern matching for github urls
        assertEquals("https://github.com/octocat/repo2", response.getRepos().get(1).getUrl());
    }

    @Test
    void getGitHubUser_inValidUserName_Fail() {
        String invalidUsername = "invalidUser";

        // MOCK 3rd party api service.
        when(restTemplate.getForObject(anyString(), eq(GitHubUserMapper.class), eq(invalidUsername)))
                .thenThrow(new CompletionException("NOT FOUND", new Throwable()));
        when(restTemplate.getForObject(anyString(), eq(GitHubRepoMapper[].class), eq(invalidUsername)))
                .thenThrow(new CompletionException("NOT FOUND", new Throwable()));

        // Assert Exception for invalid username.
        Exception exception = assertThrows(CompletionException.class, () -> {
            gitHubService.getGitHubUser(invalidUsername);
        });
    }
}
