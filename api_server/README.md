# Zetch API Server

### 1. Run the DB container
```bash
docker-compose up -d
```

### 2. Sample API interaction
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

### 3. Shut down the DB container
```bash
docker-compose down
```
### View swagger UI of apis: 
```bash
localhost:8080/swagger-ui.html
```
### Run Jacoco test coverage:
```bash
mvn test
# Generated in target/site/jacoco/index.html
```