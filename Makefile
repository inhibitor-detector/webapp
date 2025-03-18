# Comando para construir y subir la imagen
API_IMAGE_NAME = tataabancens/detector-api
API_TAG = 1.0.4

FRONTED_IMAGE_NAME = tataabancens/detector-frontend
FRONTEND_TAG = 1.0.4


deploy:
	@echo "Construyendo la imagen Docker api"
	docker build -f ./api/Dockerfile --no-cache -t $(API_IMAGE_NAME):$(API_TAG) .
	@echo "Subiendo la imagen a Docker Hub..."
	docker push $(API_IMAGE_NAME):$(API_TAG)

	@echo "Construyendo la imagen Docker frontend"
	docker build -f ./front-end/Dockerfile --no-cache -t $(FRONTED_IMAGE_NAME):$(FRONTEND_TAG) .
	@echo "Subiendo la imagen a Docker Hub..."
	docker push $(FRONTED_IMAGE_NAME):$(FRONTEND_TAG)

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