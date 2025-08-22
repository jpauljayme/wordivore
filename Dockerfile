FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Cache deps
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Build
COPY src ./src
RUN mvn -q -DskipTests package

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
# create non-root user
RUN useradd --system --create-home --shell /usr/sbin/nologin appuser
USER appuser
WORKDIR /home/appuser


# copy fat jar (adjust name if you don't use -SNAPSHOT)
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/home/appuser/app.jar"]
#ENTRYPOINT ["/bin/sh","-c","java -Dserver.port=$PORT -jar /home/appuser/app.jar"]
#