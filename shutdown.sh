#!/bin/bash
port=$1
if [ -z "${port}" ]
then
port=8005
fi
exec 3<>/dev/tcp/127.0.0.1/${port}
echo -ne "SHUTDOWN">&3
exec /bin/sleep 3s
exec 3<&-
exec 3>&-
