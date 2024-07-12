# Use an official JDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Gradle build files and the source code
COPY build.gradle settings.gradle /app/
COPY src /app/src

# Install Gradle
RUN apt-get update && apt-get install -y wget \
    && wget https://services.gradle.org/distributions/gradle-7.3.3-bin.zip \
    && unzip gradle-7.3.3-bin.zip -d /opt \
    && ln -s /opt/gradle-7.3.3/bin/gradle /usr/bin/gradle \
    && rm gradle-7.3.3-bin.zip

# Build the application
RUN gradle build --no-daemon

# Expose the port the app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "build/libs/cafe-0.0.1-SNAPSHOT.jar"]
