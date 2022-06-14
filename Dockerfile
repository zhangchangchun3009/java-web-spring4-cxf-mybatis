#syntax:docker/dockerfile:1
ARG MAVEN_VERSION=3.8.5-jdk-8
ARG JDK_VERSION=8-alpine
FROM --platform=linux/amd64 maven:${MAVEN_VERSION} AS maven_build
WORKDIR /build
COPY ./settings.xml ./.m2/
COPY ./pom.xml .
COPY ./scm.common/pom.xml ./scm.common/
COPY ./scm.web/pom.xml ./scm.web/

RUN mvn -f pom.xml -s ./.m2/settings.xml -Dmaven.repo.local=./.m2 dependency:go-offline install

COPY scm.common/src ./scm.common/src/
COPY scm.web/src ./scm.web/src/
RUN mvn clean package install -f ./scm.common/pom.xml -s ./.m2/settings.xml -Dmaven.test.skip=true -Dmaven.repo.local=./.m2
RUN mvn clean package install -f ./scm.web/pom.xml -s ./.m2/settings.xml -P dev -Dmaven.test.skip=true -Dmaven.repo.local=./.m2

FROM --platform=linux/amd64 openjdk:${JDK_VERSION} AS app_run
WORKDIR /app
COPY --from=maven_build /build/scm.web/target/scm.web.jar .
ENV JAVA_OPTS="-Xms1024M -Xmx1024M"
ENV SPRING_PROFILES_ACTIVE="dev"
ENV DS_MYSQL_DS1_WRITE_URL="jdbc:mysql://mysql:3306/scm?autoReconnect=true&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf-8&connectTimeout=120000&socketTimeout=120000"
ENV DS_MYSQL_DS1_WRITE_USERNAME="develop"
ENV DS_MYSQL_DS1_WRITE_PASSWORD="admin"
ENV DS_MYSQL_DS1_READ1_URL="jdbc:mysql://mysql:3306/scm?autoReconnect=true&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf-8&connectTimeout=120000&socketTimeout=120000"
ENV DS_MYSQL_DS1_READ1_USERNAME="develop"
ENV DS_MYSQL_DS1_READ1_PASSWORD="admin"
CMD java -jar $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -Dscm.datasource.jdbc.ds1.write.url=$DS_MYSQL_DS1_WRITE_URL -Dscm.datasource.jdbc.ds1.write.username=$DS_MYSQL_DS1_WRITE_USERNAME -Dscm.datasource.jdbc.ds1.write.password=$DS_MYSQL_DS1_WRITE_PASSWORD -Dscm.datasource.jdbc.ds1.read1.url=$DS_MYSQL_DS1_READ1_URL -Dscm.datasource.jdbc.ds1.read1.username=$DS_MYSQL_DS1_READ1_USERNAME -Dscm.datasource.jdbc.ds1.read1.password=$DS_MYSQL_DS1_READ1_PASSWORD scm.web.jar
EXPOSE 80
EXPOSE 443