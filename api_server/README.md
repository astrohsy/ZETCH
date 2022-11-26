# Zetch API Server

## How to build, run, and test

### 1. Build and run entire development environment

**Important:** See a ZETCH team member for a `.env` file specifying secret values, place the file
in `src/main/resources/`.

```
docker-compose build && docker-compose up -d
```

Note: the docker-compose build section is only necessary if you have made code changes

### 2. Stop environment

```
docker-compose down --remove-orphans
```

### 3. View running logs

```
docker-compose logs -f
```

### 4. Run a specific service

Database: `docker-compose up db -d`

API: `docker-compose up api -d`

### 5. Testing

Run all tests:

```shell
ZETCH/api_server$ ./mvnw test
```

Jacoco coverage is generated in `api_server/target/site/jacoco/index.html`.

Run unit tests only:

```shell
ZETCH/api_server$ ./mvnw test -Punit-tests
```

Run integration tests only:

```shell
ZETCH/api_server$ ./mvnw test -Pintegration-tests
```

System tests are run in postman:

```shell
newman run https://api.getpostman.com/collections/23680701-9829f32b-1694-4065-b6b9-93cbc808c454?apikey={{postman-api-key}}
```

**Important:** See a ZETCH team member for postman-api-key.

## Style checker

```shell
ZETCH/api_server$ ./mvnw checkstyle:checkstyle
```

Checkstyle report is in `api_server/reports/checkstyle.html`.

## Static analysis

```shell
ZETCH/api_server$ ./mvnw pmd:pmd
```

PMD report is generated in `api_server/target/site/pmd.html`.

In addition, we are utilizing SonarCloud:

```
https://sonarcloud.io/project/overview?id=astrohsy_ZETCH
```

## API Documentation

### View Swagger UI

`localhost:8080/swagger-ui/index.html`

### View deployed Swagger UI

`https://zetch.tech/swagger-ui/index.html`

### Authentication notes

All API endpoints are protected by OAuth2 authentication using AWS Cognito. Some endpoints perform
additional security checks. See the Swagger documentation for more details (for example, to view
client logs a user has to belong to that client).

For testing/demo, the following existing users can login:

- `admin`: admin user
- `amy`: regular user
- `bob`: regular user

Every user has the same password -- `123456`.

#### Generating Auth tokens

##### In Postman

Utilizing [variables](https://learning.postman.com/docs/sending-requests/variables/):

![postman_auth_config.png](docs/postman_auth_config.png)

##### In Swagger UI

In Swagger UI, click on Authorize, then enter a client id from `.env`.


#### API Test

API Testing is done by Newman using the setting in our Postman, and it is run everytime we push to `main`.
It generates API Test reports as the picture below. You can check the report in this [link](https://github.com/astrohsy/ZETCH/suites/9456825682/artifacts/447560614).
<img width="1144" alt="Screen Shot 2022-11-25 at 11 34 31 PM" src="https://user-images.githubusercontent.com/16847671/204072374-45c08bac-75d5-4754-9f29-06258fea6d43.png">


#### Stress Test (JMeter)

Used to local machien to testing most used features GET `/reviews` and POST `/reviews`. In the local machine it processes approximately 110 req/min, but we can improve performance by deploying more containers on AWS Beanstalk and scale up RDS instance.
<img width="1297" alt="Screen Shot 2022-11-26 at 12 51 59 AM" src="https://user-images.githubusercontent.com/16847671/204074455-453f0c51-a5f4-4429-b182-5e6fdfec53b9.png">

The configuration for JMeter is included in the repo `jmeter_stress_test.jmx`. To actually run the test, you should see a Zetch member to get an issued user token.

