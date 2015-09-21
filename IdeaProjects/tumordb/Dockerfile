#Version 0.7
FROM ubuntu:14.04
MAINTAINER Lauren Wolfe “lulumialu@gmail.com”

ENV TITAN_VER titan-0.5.4-hadoop2

RUN apt-get install -y python3-software-properties
RUN apt-get install -y software-properties-common
RUN apt-get update
RUN apt-get -y dist-upgrade
RUN apt-get install -y wget unzip
RUN apt-get install -y default-jre
RUN apt-get install -y default-jdk
RUN wget -O /tmp/titan.zip http://s3.thinkaurelius.com/downloads/titan/$TITAN_VER.zip

WORKDIR /opt/

RUN unzip /tmp/titan.zip
RUN rm /tmp/titan.zip

COPY src/main/conf/hadoop-common-2.2.0.jar /lib/
COPY src/main/conf/hadoop/ /opt/$TITAN_VER/conf/hadoop/
COPY src/main/conf/ /opt/$TITAN_VER/conf/
COPY src/main/groovy/ /opt/$TITAN_VER/
COPY dirs.sh /opt/$TITAN_VER/

WORKDIR /opt/$TITAN_VER/

EXPOSE 8182