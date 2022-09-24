# Zetch API Server

### 1. Build and run entire development environment

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

`localhost:8080/swagger-ui.html`

### 6. Run Jacoco test coverage:

```bash
mvn test
# Generated in target/site/jacoco/index.html
```

### Sample API interaction

```bash
# Add a user to database
curl --location --request POST 'http://localhost:8080/users/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "bob",
    "name": "Bob",
    "email": "bob@me.com"
}'

# Check if the user added to the database
curl --location --request GET 'http://localhost:8080/users/'
```
