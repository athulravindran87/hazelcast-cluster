#!/usr/bin/env bash

HOME=/Users/Athul/dev/hazelcast-cluster/
MODULES=('discovery-server' 'hazelcast-server' 'hazelcast-client' )
MODULELIST="discovery-server|hazelcast-server|hazelcast-client"
THISACTION=$1
cd $HOME/
gcloud config configurations activate default
gcloud auth configure-docker
echo $id
function executeCommand
{
    echo "Action is "$THISACTION
    case $THISACTION in

    build)
    buildDocker
    ;;

esac
}

function buildDocker()
{
for item in ${MODULES[*]}
do
echo $PWD
echo gcloud auth print-access-token | docker login -u oauth2accesstoken --password-stdin https://gcr.io/v2/
cd $HOME/$item
echo "current directory" $PWD
echo "building module###################" $item "###########################\n\n\n"
echo "executing command: docker build -t gcr.io/hazelcast-cluster/$item:v1 -f deploy/Dockerfile ."
#docker build -t gcr.io/hazelcast-cluster/$item:v1 -f deploy/Dockerfile .
echo "executing command: gcloud docker -- push  gcr.io/hazelcast-cluster/$item:v1"
#docker push  gcr.io/hazelcast-cluster/$item:v1
echo "###############################################################\n \n "
done
}

executeCommand $THISACTION

