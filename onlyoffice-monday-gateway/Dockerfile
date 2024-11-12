FROM openjdk:21-jdk
ENV APP_FILE onlyoffice-monday-gateway-1.0.0.jar
ENV APP_HOME /usr/apps
COPY target/$APP_FILE $APP_HOME/
WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $APP_FILE"]