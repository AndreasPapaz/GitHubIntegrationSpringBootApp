package com.papazandreas.branch.service.github;

import com.papazandreas.branch.model.github.GitHubEntityResponse;
import com.papazandreas.branch.model.github.GitHubRepoEntity;
import com.papazandreas.branch.model.github.GitHubRepoMapper;
import com.papazandreas.branch.model.github.GitHubUserMapper;
import com.papazandreas.branch.service.github.impl.GitHubService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This service handles the retrieval of GitHub user information and their associated repositories.
 * It processes the data to construct a {@link GitHubEntityResponse} object, which encapsulates
 * the user's details along with their repositories.
 * @Andreas Papazafeiropoulos
 * @version 1.0
 * @since 2024-04-27
 */
@Service
public class GitHubServiceImpl implements GitHubService {
    private static final String CACHE_VALUE = "gitHubUserData";
    private static final String CACKE_KEY = "#githubusername";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String EXPECTD_DATE_TIME_FORMATD = "yyyy-MM-dd HH:mm:ss";

    @Value("${github.api.user}")
    private String gitHubUserApiUrl;

    @Value("${github.api.repos}")
    private String gitHubReposApiUrl;

    @Resource
    private RestTemplate restTemplate;


   /**
    * Retrieves GitHub user information and their repositories based on the provided username.
    * This Service Call will utilize two GitHub Api's
    * 1. GitHub User Api
    * 2. GitHub User Repo Api
    * @param githubusername the GitHub username
    * @return GitHubEntityResponse containing user details and repositories
   */
   @Override
   @Cacheable(value = CACHE_VALUE, key = CACKE_KEY)
   public GitHubEntityResponse getGitHubUser(String githubusername) {
       if (githubusername == null || githubusername.isEmpty()) {
           return null;
       }

       // GitHub Api Calls.
       ResponseEntity<GitHubUserMapper> gitHubUserResponse = restTemplate.getForEntity(gitHubUserApiUrl, GitHubUserMapper.class, githubusername);
       ResponseEntity<GitHubRepoMapper[]> gitHubUserRepoResponse = restTemplate.getForEntity(gitHubReposApiUrl, GitHubRepoMapper[].class, githubusername);

       // Merge GitHub User with associated repos.
       GitHubEntityResponse gitHubEntityResponse = constructGitHubUserResponse(gitHubUserResponse.getBody(),
               gitHubUserRepoResponse.getBody());

       return gitHubEntityResponse;
   }

   // Consider moving this method to a GitHub Response helper class.
   private GitHubEntityResponse constructGitHubUserResponse(GitHubUserMapper gitHubUser, GitHubRepoMapper[] gitHubUserRepo) {

       List<GitHubRepoEntity> reposList = new ArrayList<>();
       if (gitHubUserRepo != null) {
           reposList = Arrays.stream(gitHubUserRepo).map(repo -> {
               GitHubRepoEntity gitHubRepo = GitHubRepoEntity.builder()
                       .name(repo.getName())
                       .url(repo.getHtmlUrl())
                       .build();

               return gitHubRepo;
           }).collect(Collectors.toList());
       }

       GitHubEntityResponse userResponse = GitHubEntityResponse.builder()
               .userName(gitHubUser.getLogin())
               .displayName(gitHubUser.getName())
               .avatar(gitHubUser.getAvatarUrl())
               .geoLocation(gitHubUser.getLocation())
               .email(gitHubUser.getEmail())
               .url(gitHubUser.getHtmlUrl())
               .createdAt(formatDate(gitHubUser.getCreatedAt()))
               .repos(reposList)
               .build();


       return userResponse;
   }

    // Consider moving to a DateUtil class if broader approach is needed for other services.
    private static String formatDate(String originalDate) {
        String result = originalDate;
        if (originalDate == null || originalDate.isEmpty()) {
            return originalDate;
        }

        SimpleDateFormat originalFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        originalFormat.setLenient(true);

        Date date = null;
        try {
            date = originalFormat.parse(originalDate);
            SimpleDateFormat desiredFormat = new SimpleDateFormat(EXPECTD_DATE_TIME_FORMATD);
            result = desiredFormat.format(date);
        } catch (ParseException e) {
            // LOG parse exception
            System.out.println(e.getMessage());
        }

        return result;
    }
}
