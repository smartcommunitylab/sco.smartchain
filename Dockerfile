FROM maven:3-jdk-8 AS mvn
WORKDIR /tmp
COPY ./pom.xml /tmp
COPY ./src /tmp/src
RUN ls -la
RUN mvn dependency:go-offline
RUN mvn install -DskipTests

FROM adoptopenjdk/openjdk11:alpine
ENV FOLDER=/tmp/target
ARG VER=0.1
ENV APP=smart-chain-backend-1.0.0.jar
ARG USER=smartchain
ARG USER_ID=3002
ARG USER_GROUP=smartchain
ARG USER_GROUP_ID=3002
ARG USER_HOME=/home/${USER}

RUN  addgroup -g ${USER_GROUP_ID} ${USER_GROUP}; \
     adduser -u ${USER_ID} -D -g '' -h ${USER_HOME} -G ${USER_GROUP} ${USER} ;

WORKDIR  /home/${USER}/app
RUN chown ${USER}:${USER_GROUP} /home/${USER}/app
# COPY --chown=aac-org:aac-org ./init.sh /tmp/server/target/init.sh
# #COPY ./app.jar /tmp/server/target/app.jar
COPY --from=mvn --chown=smartchain:smartchain ${FOLDER}/${APP} /home/${USER}/app/smart-chain.jar

USER smartchain
CMD ["java", "-XX:MaxRAMPercentage=50", "-jar", "smartchain.jar"]
