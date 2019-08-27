#!/usr/bin/env bash

docker login -u _json_key -p "$(cat ../key.json)" https://gcr.io/
cd ~/.docker/
ls -lart
cat config.json
