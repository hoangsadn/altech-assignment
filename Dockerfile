FROM openjdk:17-alpine

# copy wildcards to avoid this command failing if there's no target/lib directory
COPY build/libs* /opt

ENV PORT 8088
ENV CLASSPATH /opt
EXPOSE 8088

WORKDIR /opt
CMD ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseZGC", "-XX:MaxRAMPercentage=95.0", "-jar", "electronic-store-0.0.1-SNAPSHOT.jar"]