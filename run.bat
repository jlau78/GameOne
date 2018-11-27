rem @echo off

rem Run the GameOne demo

if "%JAVA_HOME%"=="" echo "Please set your $JAVA_HOME environment variable for this program to run" 

set JAVA_OPTS="-Djava.util.logging.config.file=./config/logger.properties -verbose:gc  -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:log/gc.log"

"%JAVA_HOME%\bin\java" -server -Xms500M -Xmx500M %JAVA_OPTS% -cp GameOne-0.1.jar com.king.Application


exit

