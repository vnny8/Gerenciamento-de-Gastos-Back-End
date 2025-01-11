#!/bin/bash

# Parar e remover o contêiner do PostgreSQL
docker stop my-postgres >/dev/null 2>&1 && docker rm my-postgres >/dev/null 2>&1

# Parar e remover o contêiner do pgAdmin
docker stop my-pgadmin >/dev/null 2>&1 && docker rm my-pgadmin >/dev/null 2>&1

# Opcional: remover a rede 'my-network' se necessário
docker network rm my-network >/dev/null 2>&1

echo "Containers parados e removidos:"
echo "- my-postgres"
echo "- my-pgadmin"
echo "Rede 'my-network' removida (se não estiver em uso)."
