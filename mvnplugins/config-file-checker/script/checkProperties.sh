if [ -z "$JAVA_HOME" ]; then
   echo Please set JAVA_HOME environment variable to your JRE/JDK installation location
   exit
fi

PROPERTY_HOME=$CLARITY_HOME
TEMPLATE_HOME=../config
if [ -n "$1" ]; then
   PROPERTY_HOME=$1
fi

if [ -n "$2" ]; then
   TEMPLATE_HOME=$2
fi
if [ -z "$PROPERTY_HOME" ]; then
   echo Please set the CLARITY_HOME variable or pass the Clarity home location as a parameter
   exit
fi

"$JAVA_HOME/bin/java" -cp config-file-checker/config-file-checker.jar com.clarity.tools.PropertiesDiff $PROPERTY_HOME $TEMPLATE_HOME modules
