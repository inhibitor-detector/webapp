# Java inhibitor-detector-api ☕
This is a Java API, using Spring Boot, Spring Data JPA, Spring Security, and JWT.

The API is a CRUD for a User, Detectors and Signal entities.

## Requirements 📋
- Docker :whale:
- Make :wrench:

# Run 🏃
```make start```

⚠️ It will run docker-compose up and start both a postgres container with the database with initial values

This will provide 3 users.
- user: tata, password: 12345678, role: USER
- user: det_1_cliente_1, password: 12345678, role: DETECTOR
- user: det_1_cliente_2, password: 12345678, role: DETECTOR

# Commands ✅
```make deploy```

It will deploy latest version held in master branch of the proyect

```make start-local```

It will build and start the api with the current branch
