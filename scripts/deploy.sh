##!/usr/bin/env bash
#
#REPOSITORY=/home/ubuntu/app
#
#echo "> 현재 구동 중인 애플리케이션 pid 확인"
#
#CURRENT_PID=$(pgrep -fla java | grep hayan | awk '{print $1}')
#
#echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"
#
#if [ -z "$CURRENT_PID" ]; then
#  echo "현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
#else
#  echo "> kill -15 $CURRENT_PID"
#  kill -15 $CURRENT_PID
#  sleep 5
#fi
#
#echo "> 새 애플리케이션 배포"
#
#JAR_NAME=$(ls -tr $REPOSITORY/*SNAPSHOT.jar | tail -n 1)
#
#echo "> JAR NAME: $JAR_NAME"
#
#echo "> $JAR_NAME 에 실행권한 추가"
#
#chmod +x $JAR_NAME
#
#echo "> $JAR_NAME 실행"
#
#nohup java -jar -Duser.timezone=Asia/Seoul $JAR_NAME >> $REPOSITORY/nohup.out 2>&1 &

#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/app
cd $REPOSITORY

JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/simya-0.0.1-SNAPSHOT.jar

CURRENT_PID=$(pgrep -fla java | grep hayan | awk '{print $1}')

if [ -z $CURRENT_PID ]
then
  echo "> 종료할것 없음."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH 배포"
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &