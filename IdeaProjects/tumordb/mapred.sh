#!/usr/bin/env bash
docker stop graph
docker rm graph
cd /local
rm -rf dist-tumordb
git clone https://github.com/laurenwolfe/dist-tumordb.git
cd dist-tumordb/IdeaProjects/tumordb
docker build -t lulumialu/tumordist .
docker run -i -t -p 8182:8182 -v /local/appdata:/opt/titan-0.5.4-hadoop2/appdata -v /local/pwfiles:/opt/titan-0.5.4-hadoop2/pwfiles --name graph lulumialu/tumordist