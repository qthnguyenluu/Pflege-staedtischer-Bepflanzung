# Pflege-staedtischer-Bepflanzung
- This is my Database Project at uni
- This document provides a guide for the implementation of a database project consisting of multiple phases.

Table of Contents
Phase 1: ER-Model Creation
Phase 2: Transformation to a Relational Model
Phase 3: Database Implementation in SQL
Phase 4: RESTful Web Services Implementation


# Overview of Phase 4: RESTful Web Services Implementation
In this phase, a RESTful Web Service is developed, providing a Level 2 REST API. This service enables interaction with the database using cURL commands.

- Requirements

The application must be developed under the latest LTS release of Eclipse Temurin and must be executable (included in the API template).
Gradle must be used as the build tool.
- The program must be startable as a project in Docker Compose using the following command:

./gradlew :blatt4:runContainerized

- The system could be stopped with the command:

./gradlew :blatt4:spring:composeDown

- Execution
Docker Compose application must be installed on your system.

- Testing
Starting the system with:

./gradlew :blatt4:spring:runContainerized

- The application must run on port 8080 via HTTP.
- Note: The API can be tested using cURL or Swagger UI (via http://localhost:8090).






