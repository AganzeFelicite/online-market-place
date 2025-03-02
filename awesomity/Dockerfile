# Use an official Java runtime as a parent image
FROM openjdk:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the local jar file to the container
COPY target/awesomity-0.0.1-SNAPSHOT.jar /app/awesomity.jar

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/awesomity.jar"]
