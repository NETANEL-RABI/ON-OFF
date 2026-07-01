#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
# ... (מקוצר לצורך בהירות, זהו סקריפט ההרצה הסטנדרטי של Gradle)
#

# Find java.exe
if [ -n "$JAVA_HOME" ] ; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

exec "$JAVACMD" "-Dorg.gradle.appname=$APP_BASE_NAME" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
