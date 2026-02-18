FROM amazoncorretto:25-headless

#Build Args
ARG SERVICE_VERSION
ARG SERVICE_NAME

# Service env variable
ENV ARTIFACT_NAME=$SERVICE_NAME
ENV ARTIFACT_VERSION=$SERVICE_VERSION

RUN mkdir -p /opt/$ARTIFACT_NAME
RUN mkdir -p /opt/$ARTIFACT_NAME/logs

WORKDIR /opt/$ARTIFACT_NAME

COPY application/target/$ARTIFACT_NAME-$ARTIFACT_VERSION.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]