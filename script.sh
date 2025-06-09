#!/bin/bash
echo "Criando imagens..."

docker build -t jotabr092/proc-jur-service:1.0 -f .dockerfile .

echo "Push de imagens..."

docker push jotabr092/proc-jur-service:1.0

echo "Criando secrets..."

kubectl create secret generic postgres-env --from-env-file=.env.postgres
kubectl create secret generic default-env --from-env-file=.env
kubectl create secret generic tls-secret --from-file=src/main/resources/keystore.p12

echo "Criando serviços no cluster kubernetes..."

kubectl apply -f ./services.yml

echo "Criando deploy..."

kubectl apply -f ./deployment.yml