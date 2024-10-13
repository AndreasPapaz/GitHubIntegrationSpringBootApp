# GitHub User API

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation & Running](#installation)
- [Running the Application](#running-the-application)
- [API Endpoint](#api-endpoint)

## Features

- **Fetch GitHub User Data**: Retrieves user details such as username, display name, avatar, location, email, URL, and account creation date.
- **Fetch GitHub Repositories**: Lists all repositories of the specified user.

## Architecture

The application follows a Layered Architecture, promoting separation of concerns. 
The primary layers include:

- Controller Layer: Handles HTTP requests and responses, input validation by regex matching pattern.
- Service Layer: Contains business logic and interacts with external APIs.
- Model Layer: Defines data structures and entities (3rd party response mapping & defined response entitty).
- Configuration Layer: Manages application configurations and bean definitions.
- Exception Handling: Centralized error handling mechanisms.


## Prerequisites

- **Java 17 or higher**: [Download Java](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- **Gradle**: [Download Gradle](https://gradle.org/install/)
- **Git**: [Download Git](https://git-scm.com/downloads)

## Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/github-user-api.git
   cd github-user-api
2. **Run the SpringBoot Application via Gradle**
   ```bash
   gradle clean build 
   

## Running the Application
1. **Run the SpringBoot Application via Gradle**
   ```bash
   gradle bootRun
   
2. **Application will run on localhost:8080**
   ```bash
   curl -X GET localhost:8080/github-user/octocat

3. **Go to Browswer if you do not want to use curl via terminal**
   
   ```http://localhost:8080/github-user/{username}``` 


## Api Endpoint


### Get GitHub User Information

Retrieve information about a GitHub user by their username.

- **Endpoint**: `/github-user/{username}`
- **Method**: `GET`
- **URL**: `http://localhost:8080/github-user/{username}`
- **URL Parameters**:
   - `username` (string) **required**: The GitHub username of the user you want to retrieve information for.

#### Request

```http
GET /github-user/{username} HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

#### Response

```json
{
  "user_name": "<string>",
  "display_name": "<string>",
  "avatar": "<string - URL>",
  "geo_location": "<string>",
  "email": "<string | null>",
  "url": "<string - URL>",
  "created_at": "<string - YYYY-MM-DD HH:MM:SS>",
  "repos": [
    {
      "name": "<string>",
      "url": "<string - URL>"
    }
    // ... 
  ]
}
