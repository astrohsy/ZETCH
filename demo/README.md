# DEMO

### 1. Run the program.
```bash
docker-compose up -d
```

### 2. Testing the User Add API
```bash
# Add a user to database
curl localhost:5432/users/add \
    -d name=First \
    -d email=email@email.com \
    -d username=coolkid

# Check if the user added to the database
curl localhost:5432/users/all
```