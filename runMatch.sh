#! /bin/sh
docker run -it -d --rm --name server-mulino -P --env=DISPLAY --env=QT_X11_NO_MITSHM=1 --net=host --volume=/tmp/.X11-unix:/tmp/.X11-unix:rw server-mulino

docker run -it --rm -d --name run-client-jarl-white -e COLOR="-w" --net=host --memory=2g --cpus=1 jarl-mulino-client

docker run -it -d --rm --name run-client-jarl-black -e COLOR="-b" --net=host --memory=2g --cpus=1 jarl-mulino-client
