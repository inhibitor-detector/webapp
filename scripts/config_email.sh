#!/bin/bash

APPLICATION_PROPERTIES="./application.properties"
APPLICATION_EXAMPLE="./application-example.properties"
EMAIL_API_LINK="https://support.google.com/mail/answer/185833?hl=en#"

# Crear siempre application.properties si no existe
if [ ! -f "$APPLICATION_PROPERTIES" ]; then
  echo "Creando $APPLICATION_PROPERTIES desde $APPLICATION_EXAMPLE..."
  cp "$APPLICATION_EXAMPLE" "$APPLICATION_PROPERTIES"
fi

# Preguntar por la configuración del email
read -p "¿Quieres configurar email-feature? (y/n): " config_email
if [ "$config_email" = "y" ]; then
  read -p "Ingresa tu email: " user_email
  echo "Para obtener tu API Key, visita: $EMAIL_API_LINK"
  read -p "Ingresa tu API Key: " api_key
  if [ -z "$user_email" ] || [ -z "$api_key" ]; then
    echo "Error: Debes proporcionar tanto el email como la API Key."
  else
    if [ -f "$APPLICATION_PROPERTIES" ]; then
      echo "El archivo $APPLICATION_PROPERTIES ya existe. Se eliminará y se creará de nuevo..."
      rm "$APPLICATION_PROPERTIES"
    fi
    cp "$APPLICATION_EXAMPLE" "$APPLICATION_PROPERTIES"
    printf "\n# Email feature config\n" >> "$APPLICATION_PROPERTIES"
    printf "spring.mail.username=%s\n" "$user_email" >> "$APPLICATION_PROPERTIES"
    printf "spring.mail.password=%s\n" "$api_key" >> "$APPLICATION_PROPERTIES"
    echo "Configuración guardada correctamente en $APPLICATION_PROPERTIES."
  fi
else
  echo "Saltando configuración de email-feature."
fi
