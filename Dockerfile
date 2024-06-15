# Base image with OpenJDK 17
FROM openjdk:17-oracle

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Install Tesseract and its dependencies
RUN apt-get update && \
    apt-get install -y tesseract-ocr libtesseract-dev && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy Tesseract trained data files
COPY kor.traineddata /usr/share/tesseract-ocr/4.00/tessdata/
COPY kor_vert.traineddata /usr/share/tesseract-ocr/4.00/tessdata/
COPY eng.traineddata /usr/share/tesseract-ocr/4.00/tessdata/

# Copy the Spring Boot application jar file
COPY epson-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
