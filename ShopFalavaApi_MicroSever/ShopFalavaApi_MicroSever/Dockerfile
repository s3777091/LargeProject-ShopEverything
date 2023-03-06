FROM openjdk:17-oracle
VOLUME /Falava/tmp
EXPOSE 2323
ADD Falava/target/kishop-0.0.1.jar kishop-0.0.1.jar
ENTRYPOINT ["java","-jar","/kishop-0.0.1.jar"]