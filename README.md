# Test Project Built by sriram

## Specifications:
 - Postgres DB
 - Flyway Migration
 - Java 17
 - Spring boot 3.1.5

Local Development:
- Pull postgres image using ```docker pull postgres```
- Start DB instance using ```docker run --name postgres -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres```
- Create Books DB using ```CREATE DATABASE books```
- Swagger can be accessed through [here](http://localhost:8080/swagger-ui.html)

Deployment:
- Docker-Compose up
- TBD
