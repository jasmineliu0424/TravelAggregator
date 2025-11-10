# Assumptions / prerequisites:
# - Docker is installed on the machine building images.
# - Each service is a standalone Maven-based Spring Boot project located in a subdirectory.
# - Default Java version target is 17.
# - To build a specific service, pass the build argument SERVICE_DIR (default: carhire_search).
# - You can override runtime PORT env var when running the container.
#
# Reflection:
# - All services are Java Spring Boot apps using Maven; build + run steps are uniform (mvn package then java -jar).
# - Service configuration (port, external endpoints) is provided via properties/env vars, so the same image/entrypoint can be reused with different runtime environment variables.

FROM maven:3.8.8-eclipse-temurin-17 AS builder

# service directory (subfolder containing the service's pom.xml); override with --build-arg SERVICE_DIR=...
ARG SERVICE_DIR=carhire_search
WORKDIR /workspace

# copy only the service directory to reduce context size; if services share parent poms you may need to copy more
COPY . ${SERVICE_DIR}

# build the service (skip tests for faster images; remove -DskipTests to run tests)
RUN mvn -f ${SERVICE_DIR}/pom.xml -DskipTests package

# Runtime image
FROM eclipse-temurin:17-jre

ARG SERVICE_DIR=carhire_search

# default port can be overridden at runtime with -e PORT=...
ENV PORT=18100
WORKDIR /app

# copy built artifacts (target folder) from builder
COPY --from=builder /workspace/${SERVICE_DIR}/target /app/target

# Expose the default port (informational; actual mapping happens at docker run)
EXPOSE ${PORT}

# Start command:
# - finds the non-original jar in /app/target and runs it with server.port set from PORT env var
ENTRYPOINT ["sh","-c","java -Djava.security.egd=file:/dev/./urandom -jar $(ls /app/target/*.jar | grep -v 'original' | head -n1) --server.port=${PORT}"]