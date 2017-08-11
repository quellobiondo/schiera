FROM openjdk:8
COPY build/distributions/serverMulino.tar /usr/src/serverMulino/
WORKDIR /usr/src/serverMulino

ENV TURN_TIME="60"

RUN tar -xvf serverMulino.tar

# Espongo le porte per client white e client black
EXPOSE 5801 5801
EXPOSE 5802 5802

CMD serverMulino/bin/serverMulino ${TURN_TIME}
