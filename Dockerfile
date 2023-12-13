FROM openjdk:17
COPY target/ds_monitoring-0.0.1-SNAPSHOT.jar ds_monitoring.jar
EXPOSE 8082
CMD ["java", "-jar", "ds_monitoring.jar"]