FROM maven:ibmjava-alpine AS build
RUN mkdir /app
WORKDIR /app
COPY . .
RUN mvn clean install
RUN mvn tomcat7:redeploy 
RUN mvn package 


FROM arm64v8/tomcat
COPY --from=build /app/target/file.war /usr/local/tomcat/webapps