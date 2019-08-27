#!/usr/bin/env bash
whoami
#docker login -u _json_key -p "$(cat ../key.json)" https://gcr.io/
cd ~/.docker/
ls -lart
cat config.json
mv config.json config.json_old

ls -lart