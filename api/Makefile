# Database variables
container = inhibitor-detector-db
user = user
password = 123123123
database = inhibitor_detector


# Application Commands
install:
	mvn clean install

test:
	mvn test

run: start_db
	mvn clean package
	java -jar ./webapp/target/webapp-1.0-SNAPSHOT.jar

# Database Commands
start_db:
	@if [ "$(container)" = "inhibitor-detector-db" ]; then echo "Using default container name: inhibitor-detector-db."; fi
	docker start $(container)

stop_db:
	@if [ "$(container)" = "inhibitor-detector-db" ]; then echo "Using default container name: inhibitor-detector-db."; fi
	docker stop $(container)

create_db:
	@if [ "$(container)" = "inhibitor-detector-db" ]; then echo "Using default container name: 'inhibitor-detector-db'."; fi
	@if [ "$(user)" = "user" ]; then echo "Using default user name: 'user'."; fi
	@if [ "$(password)" = "123123123" ]; then echo "Using default container name: '123123123'."; fi
	@if [ "$(database)" = "inhibitor_detector" ]; then echo "Using default container name: 'inhibitor_detector'."; fi

	docker run --name $(container) -e POSTGRES_PASSWORD=$(password) -e POSTGRES_USER=$(user) -p 5432:5432 -d postgres
	sleep 5  # Wait for the PostgreSQL container to start (you can adjust this as needed)
	docker exec -it $(container) psql -U $(user) -c 'CREATE DATABASE "$(database)" WITH ENCODING "UTF-8";'

populate_db:
	@if [ "$(container)" = "inhibitor-detector-db" ]; then echo "Using default container name: 'inhibitor-detector-db'."; fi
	@if [ "$(user)" = "user" ]; then echo "Using default user name: 'user'."; fi
	@if [ "$(password)" = "123123123" ]; then echo "Using default container name: '123123123'."; fi
	@if [ "$(database)" = "inhibitor-detector" ]; then echo "Using default container name: 'inhibitor-detector'."; fi

	docker exec -i $(container) psql -U $(user) --dbname $(database) < ./persistence/src/main/resources/schema.sql
	docker exec -i $(container) psql -U $(user) --dbname $(database) < ./persistence/src/main/resources/data.sql


# Nombre de la imagen y el repositorio en Docker Hub
IMAGE_NAME = tataabancens/detector-api
TAG = 1.0.2

# Comando para construir y subir la imagen
deploy:
	@echo "Construyendo la imagen Docker..."
	docker build --no-cache -t $(IMAGE_NAME):$(TAG) .
	@echo "Subiendo la imagen a Docker Hub..."
	docker push $(IMAGE_NAME):$(TAG)

start:
	@echo "Pulling latest images..."
	docker-compose pull
	@echo "Rebuilding containers..."
	docker-compose up -d


