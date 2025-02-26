# Comando para construir y subir la imagen TODO: Geti this into one file
IMAGE_NAME = tataabancens/detector-api
TAG = 1.0.4

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

stop:
	@echo "Stoppings..."
	docker-compose down

start-local:
	@echo "Pulling latest images..."
	docker-compose up -d db
	@echo "Rebuilding api..."
	mvn clean package
	@echo "Running api..."
	java -jar webapp/target/webapp-1.0-SNAPSHOT.jar