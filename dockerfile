From openjdk:8
COPY target/TranscriptAnalytics-0.0.1-SNAPSHOT.jar TranscriptAnalytics-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","TranscriptAnalytics-0.0.1-SNAPSHOT.jar"]