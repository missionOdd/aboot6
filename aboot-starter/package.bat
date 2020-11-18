@echo off

rem ======================================================================
rem mvn package script
rem default local profile
rem
rem author: mission
rem date: 2019-10-01
rem ======================================================================

set PROFILE=%1
if "%PROFILE%" == "" (
    echo profile:prod
    mvn clean package -Pprod -DskipTests
    echo profile:prod
    pause
) else (
    echo profile:%PROFILE%
    mvn clean package -P%PROFILE% -DskipTests
    echo profile:%PROFILE%
    pause
)