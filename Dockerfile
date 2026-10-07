FROM tomcat:9.0-jdk8-temurin

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY web/ /usr/local/tomcat/webapps/ROOT/
COPY src/ /tmp/src/

RUN mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes && \
    find /tmp/src -name "*.java" > /tmp/sources.txt && \
    javac \
    -cp "/usr/local/tomcat/lib/*:/usr/local/tomcat/webapps/ROOT/WEB-INF/lib/*" \
    -d /usr/local/tomcat/webapps/ROOT/WEB-INF/classes \
    @/tmp/sources.txt

EXPOSE 8080

CMD ["catalina.sh", "run"]