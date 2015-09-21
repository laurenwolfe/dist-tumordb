#!/usr/bin/env bash
docker stop graph
docker rm graph
cd /local
rm -rf dist-tumordb
rm -rf appdata
mkdir -p appdata/es
mkdir -p appdata/hadoop
mkdir -p appdata/cassandra/data
mkdir -p appdata/cassandra/commitlog
mkdir -p appdata/cassandra/saved_caches
git clone https://github.com/laurenwolfe/dist-tumordb.git
cd dist-tumordb/IdeaProjects/tumordb
docker build -t lulumialu/tumordist .
docker run -i -t -p 8182:8182 -v /local/appdata:/opt/titan-0.5.4-hadoop2 -v /local/appdata:/opt/titan-0.5.4-hadoop2 --name graph lulumialu/tumordist