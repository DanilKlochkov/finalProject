ARG BASEIMAGEREGISTRY=""
FROM ${BASEIMAGEREGISTRY}openjdk:17-slim

COPY target/*.jar /application.jar

RUN groupadd -g 1001 appuser && useradd -r -u 1001 -g appuser appuser
USER appuser

ENTRYPOINT ["java", "--enable-preview", "-jar", "/application.jar"]