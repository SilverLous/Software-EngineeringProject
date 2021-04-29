FROM maven:ibmjava-alpine
RUN mkdir /app
WORKDIR /app
COPY . /app
RUN mvn clean install
RUN mvn tomcat7:redeploy 