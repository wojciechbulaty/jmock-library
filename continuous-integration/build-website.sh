#!/bin/sh
#
# Script to build and deploy the jMock website.

BUILDID=$(date --utc +'%Y%m%d%H%M')

SRCDIR=src/framework
WEBSITE=${WEBSITE:-dcontrol@www.codehaus.org:/www/jmock.codehaus.org/}

WEBDIR=website/output
JAVADOCDIR=$WEBDIR/docs/javadoc

# Create clean output directory
rm -r $WEBDIR
mkdir $WEBDIR


# Generate the skinned and styled site content
(cd website; ruby ./skinner.rb)

# Generate project reports

# Javadoc
$JAVA_HOME/bin/javadoc \
	-windowtitle 'jMock API Documentation' \
	-d $JAVADOCDIR \
	-link http://www.junit.org/junit/javadoc/3.8.1 \
	-link http://java.sun.com/j2se/1.4.2/docs/api \
	-sourcepath $SRCDIR \
	-subpackages org.jmock


# Copy content to web server
scp -r $WEBDIR/* $WEBSITE
echo "the live website has been updated"
