PID=$(ps -ef | grep MyBootApplication | grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ]
then
  echo MyBoot is already stopped
else
  echo kill -9 $PID
  kill -9 $PID
fi