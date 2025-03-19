# webapp

This repo contains the Webapp of this project, "Detector de inhibidores"

In here you will find 2 main folders:
- [api](./api/README.md)
  - Contains the code relevant to the API and its Database
- [frontend](./front-end/README.md)
  - Contains the code relevant to the Frontend

Inside each one you will find more a more detailed README of the module.

## Initialization

To initialize this Webapp, you can simply run:
```
make start
```

This will generate 3 Docker Containers, exposing different ports:
- `inhibitor-detector-api`
  - The API, on port localhost:8001
- `inhibitor-detector-db`
  - The Database, on port localhost:5432
- `inhibitor-detector-frontend`
  - The Frontend, on port localhost:3000

## Email feature

When running make start it will automatically prompt the user with the instructions to configure email

Then when an inhibition is detected the user will send an email to the configured receptors of each detector