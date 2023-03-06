FROM openjdk:17-oracle
VOLUME /tmp
EXPOSE 2525
ADD target/kishopconsumer-0.0.1.jar kishopconsumer-0.0.1.jar
ENTRYPOINT ["java","-jar","/kishopconsumer-0.0.1.jar"]