#! /bin/shell

#======================================================================
# mvn package script
# default local profile
#
# author: mission
# date: 2018-10-01
#======================================================================

PROFILE=$1
if [[ -z "$PROFILE" ]]; then
    PROFILE=prod
fi
echo "profile:${PROFILE}"
mvn clean package -P${PROFILE} -DskipTests
echo "profile:${PROFILE}"