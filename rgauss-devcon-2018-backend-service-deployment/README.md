# Overview

This project contains the artifacts to deploy rgauss DevCon 2018 backend service via Docker and Kubernetes.

# Build Docker Image

You must first build the jar artifact.  See [rgauss-devcon-2018-backend-service](../rgauss-devcon-2018-backend-service).

To build the Docker image run the script which gets the service jar:

```bash
./build-prep.sh
```

if building to deploy within Minikube:

```bash
eval $(minikube docker-env)
```

build the service Docker image:

```bash
docker build -t alfresco/rgauss-devcon-2018-backend-service:0.1-SNAPSHOT .
```

Note that you also need the [inception-rest-tika](../rgauss-devcon-2018-backend-service) image.

# Run via Helm

## Prerequisites

### Alfresco DBP Deployment

See [alfresco-dbp-deployment](https://github.com/Alfresco/alfresco-dbp-deployment).

It may be helpful to disable the components not needed for this example:

```bash
helm install alfresco-dbp \
--set alfresco-content-services.repository.environment.SYNC_SERVICE_URI="http://$ELBADDRESS:$INFRAPORT/syncservice" \
--set alfresco-activiti-cloud-gateway.enabled=false \
--set alfresco-activiti-cloud-registry.enabled=false \
--set alfresco-activiti-cloud-sso-idm.enabled=false
```

### Alfresco Event Gateway PoC

See [alfresco-event-gateway-poc](../alfresco-event-gateway-poc)

and be sure to set the `EVENTGATEWAYRELEASE` environment variable:

```bash
export EVENTGATEWAYRELEASE=smelly-cat
```

## Running
Get the deployed process definition ID from the [process import](../rgauss-devcon-2018-process):

```bash
export TAG_VERIFICATION_PROCESS_DEFINITION_ID=TagVerification:5:163
```

Then run the helm commands:

```bash
cd helm
helm depedency update rgauss-devcon-2018-backend-service
helm install rgauss-devcon-2018-backend-service \
--set eventgateway.kafka.host="$EVENTGATEWAYRELEASE-kafka" \
--set eventgateway.zookeeper.host="$EVENTGATEWAYRELEASE-zookeeper" \
--set alfresco.repository.baseUrl="http://$DBPRELEASE-alfresco-content-services-repository/alfresco" \
--set alfresco.repository.username="admin" \
--set alfresco.repository.password="admin" \
--set alfresco.processServices.baseUrl="http://$DBPRELEASE-alfresco-process-services/activiti-app" \
--set alfresco.processServices.username="admin@app.activiti.com" \
--set alfresco.processServices.password="admin" \
--set alfresco.processServices.processDefinitionId="$TAG_VERIFICATION_PROCESS_DEFINITION_ID"
```
