# Zetch API Server

### 1. Build and run entire development environment
See a ZETCH team member for `.env` specifying secret values, place in `src/main/resources/`

```
docker-compose build && docker-compose up -d
```

note: the docker-compose build section is only necessary if you have made code changes

### 2. Stop environment

```
docker-compose down --remove-orphans
```

### 3. View running logs

```
docker-compose logs -f
```

### 4. Run specific service

database: `docker-compose up db -d`

api: `docker-compose up api -d`

### 5. View swagger UI of apis:

`localhost:8080/swagger-ui/index.html`

### 6. Deployed service swagger link:
`https://zetch.tech/swagger-ui/index.html`

### 6. Run Jacoco test coverage:

```bash
mvn test
# Generated in target/site/jacoco/index.html
```

### Run PMD static analysis bug finder:

```bash
mvn pmd:pmd
# Generated in target/site/pmd.html
```

### Auth

Some routes are protected by OAuth2 authentication using Cognito.
Auth tokens can be generated either in Postman or Swagger UI.

In Postman, utilizing [variables](https://learning.postman.com/docs/sending-requests/variables/):

![postman_auth_config.png](docs/postman_auth_config.png)

In Swagger UI, click on Authorize, then enter the client id.

See a ZETCH team member for `.env` specifying secret values.

For a list of users which can log in with Cognito, see the `Application.commandLineRunner()` method.
Every user has the same password -- `123456`.

### Run checkstyle

```
mvn checkstyle:checkstyle
# generate checkstyle reports in /target/site/
mvn install
# copies the whole /target/site/ into /reports

# view checkstyle report in /reports/checkstyle.html
```



### SonarQube

```
https://sonarcloud.io/project/overview?id=astrohsy_ZETCH
```