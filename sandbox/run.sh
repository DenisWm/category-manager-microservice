docker network create adm_videos_services
docker network create elastic

sudo chown root app/filebeat/filebeat.docker.yml

mkdir -m 777 .docker
mkdir -m 777 .docker/es01
mkdir -m 777 .docker/keycloak
mkdir -m 777 .docker/filebeat

if [ -z "${GOOGLE_CLOUD_CREDENTIALS}" ] && [ -z "${GOOGLE_CLOUD_PROJECT_ID}" ]; then
    echo "Erro: Uma ou ambas as variáveis de ambiente GOOGLE_CLOUD_CREDENTIALS e GOOGLE_CLOUD_PROJECT_ID não estão definidas." >&2
    exit 1
fi

echo "As variáveis de ambiente estão definidas:"
echo "GOOGLE_CLOUD_CREDENTIALS: ${GOOGLE_CLOUD_CREDENTIALS}"
echo "GOOGLE_CLOUD_PROJECT_ID: ${GOOGLE_CLOUD_PROJECT_ID}"

envsubst <../.env.template > ./app/env.local

docker compose -f services/docker-compose.yml up -d
#docker compose -f elk/docker-compose.yml up -d
#docker compose -f app/docker-compose.yml up -d

echo  "Inicializando os containers..."
sleep 20

