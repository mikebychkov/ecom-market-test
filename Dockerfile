FROM openjdk:17-jdk
RUN mkdir /app
ADD ./build/libs/demo-1.jar /app/service.jar
CMD java -jar /app/service.jar