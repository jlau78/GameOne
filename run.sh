# Run the GameOne demo

JAVA_OPTS="-Djava.util.logging.config.file=./config/logger.properties -verbose:gc  -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:log/gc.log"

if [[ -z "${JAVA_HOME}" ]]
then
	echo "Please set your $JAVA_HOME environment variable for this program to run"
	echo ""
fi

${JAVA_HOME}/bin/java -Xms500M -Xmx500M ${JAVA_OPTS} -cp "GameOne-0.1.jar" com.king.Application

exit 0
