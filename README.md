# foreign-currency

Before you compile and deploy please read some notes here.

1. This project don't use Dockerfile manually. The Dockerfile is generated using plugin. You can see that at the pom.xml
2. The default app port is 8780 (see the aplication.properties)
3. The default database port is 5430 (see docker-compose.yml)
4. You need to compile the source code to create docker image locally
5. To access api documentation please go through localhost:8780/swagger-ui.html after deployment
