FROM maven:ibmjava-alpine AS build
RUN mkdir /app
WORKDIR /app
COPY . .
RUN mvn package -Dmaven.test.skip 


FROM arm64v8/tomcat:10
COPY --from=build /app/target/team7Parkhaus.war /usr/local/tomcat/webapps