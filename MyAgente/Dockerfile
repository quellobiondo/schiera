FROM openjdk:8
COPY build/libs/MyAgente-1.0-standalone.jar /usr/src/myapp/
WORKDIR /usr/src/myapp

ENV COLOR="-w"

CMD java -jar MyAgente-1.0-standalone.jar ${COLOR}
