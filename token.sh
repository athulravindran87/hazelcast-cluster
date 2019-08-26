#!/usr/bin/env bash
whoami
docker login -u _json_key -p "$(cat ../key.json)" https://gcr.io/