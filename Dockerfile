FROM maven:3.8.7-openjdk-18-slim AS maven

WORKDIR /usr/src/app
COPY . /usr/src/app

RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim

ENV TZ=America/Sao_Paulo

RUN apt-get update && apt-get install -y curl tzdata && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    dpkg-reconfigure -f noninteractive tzdata && \
    echo $TZ > /etc/timezone

ENV APP_HOME /app
ENV APP_FILE movement_stock*.jar
ENV JAVA_OPTS="-Duser.timezone=America/Sao_Paulo"

WORKDIR ${APP_HOME}

COPY --from=maven /usr/src/app/target/${APP_FILE} ${APP_HOME}

CMD ["sh", "-c", "java -jar ${APP_FILE}"]
