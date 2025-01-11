#!/bin/bash

# Verificar e criar a rede 'my-network' se não existir
docker network inspect my-network >/dev/null 2>&1 || docker network create my-network

# Criar um volume Docker para persistência de dados do PostgreSQL
docker volume inspect my-postgres-data >/dev/null 2>&1 || docker volume create my-postgres-data

# Iniciar o contêiner do PostgreSQL com o volume
docker run --name my-postgres --network=my-network -p 5433:5432 -e POSTGRES_PASSWORD=12345 -v my-postgres-data:/var/lib/postgresql/data -d postgres

# Aguarde o PostgreSQL iniciar
echo "Aguardando PostgreSQL iniciar..."
sleep 5

# Criar o banco de dados `gerenciamento_de_gastos`
docker exec my-postgres psql -U postgres -c "CREATE DATABASE gerenciamento_de_gastos;"

# Iniciar o contêiner do pgAdmin
docker run --name my-pgadmin --network=my-network -p 15432:80 -e PGADMIN_DEFAULT_EMAIL=viniciuspadilhavieira@hotmail.com -e PGADMIN_DEFAULT_PASSWORD=12345 -d dpage/pgadmin4

echo "Containers iniciados:"
echo "- PostgreSQL (my-postgres) na porta 5433"
echo "- pgAdmin (my-pgadmin) na porta 15432"
