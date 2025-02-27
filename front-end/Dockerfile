FROM node:22

WORKDIR /app

RUN echo "Clonando repositorio en $(date)" && git clone https://github.com/inhibitor-detector/webapp.git

WORKDIR /app/webapp/front-end

RUN npm install

CMD git pull && npm start
