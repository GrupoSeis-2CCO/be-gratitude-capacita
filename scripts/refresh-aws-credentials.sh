#!/bin/bash
# Script para renovar credenciais AWS dinamicamente do IMDS (Instance Metadata Service)
# Este script obtém credenciais temporárias da IAM Role atribuída à EC2
# e atualiza o arquivo .env usado pelo Docker Compose.
#
# Uso: ./refresh-aws-credentials.sh [env_file_path]
# Padrão: /home/ubuntu/api/.env

set -e

ENV_FILE="${1:-/home/ubuntu/api/.env}"
COMPOSE_DIR="$(dirname "$ENV_FILE")"

echo "=== Renovando credenciais AWS do IMDS ==="
echo "Arquivo .env: $ENV_FILE"

# Obter o nome da role
ROLE_NAME=$(curl -s --connect-timeout 5 http://169.254.169.254/latest/meta-data/iam/security-credentials/)

if [ -z "$ROLE_NAME" ]; then
    echo "ERRO: Não foi possível obter a IAM Role. Verifique se a EC2 tem uma IAM Role atribuída."
    exit 1
fi

echo "IAM Role detectada: $ROLE_NAME"

# Obter credenciais temporárias
CREDS=$(curl -s --connect-timeout 5 http://169.254.169.254/latest/meta-data/iam/security-credentials/$ROLE_NAME)

if [ -z "$CREDS" ]; then
    echo "ERRO: Não foi possível obter credenciais da role $ROLE_NAME"
    exit 1
fi

# Extrair valores
AWS_ACCESS_KEY_ID=$(echo "$CREDS" | python3 -c "import sys, json; print(json.load(sys.stdin)['AccessKeyId'])")
AWS_SECRET_ACCESS_KEY=$(echo "$CREDS" | python3 -c "import sys, json; print(json.load(sys.stdin)['SecretAccessKey'])")
AWS_SESSION_TOKEN=$(echo "$CREDS" | python3 -c "import sys, json; print(json.load(sys.stdin)['Token'])")
EXPIRATION=$(echo "$CREDS" | python3 -c "import sys, json; print(json.load(sys.stdin)['Expiration'])")

echo "Credenciais obtidas. Expiração: $EXPIRATION"

# Atualizar .env mantendo outras variáveis
if [ -f "$ENV_FILE" ]; then
    # Remove linhas antigas de credenciais AWS
    sed -i '/^AWS_ACCESS_KEY_ID=/d' "$ENV_FILE"
    sed -i '/^AWS_SECRET_ACCESS_KEY=/d' "$ENV_FILE"
    sed -i '/^AWS_SESSION_TOKEN=/d' "$ENV_FILE"
fi

# Adiciona novas credenciais
echo "AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID" >> "$ENV_FILE"
echo "AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY" >> "$ENV_FILE"
echo "AWS_SESSION_TOKEN=$AWS_SESSION_TOKEN" >> "$ENV_FILE"

echo "Arquivo .env atualizado com sucesso!"

# Reiniciar container para aplicar novas credenciais
if [ -d "$COMPOSE_DIR" ] && [ -f "$COMPOSE_DIR/compose-api.yaml" ]; then
    echo "Reiniciando containers..."
    cd "$COMPOSE_DIR"
    sudo docker compose -f compose-api.yaml down api 2>/dev/null || true
    sudo docker compose -f compose-api.yaml up -d api
    echo "Container reiniciado com novas credenciais"
else
    echo "AVISO: compose-api.yaml não encontrado em $COMPOSE_DIR"
    echo "Reinicie manualmente o container para aplicar as novas credenciais"
fi

echo "=== Renovação concluída ==="
