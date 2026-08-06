@echo off
REM =========================================================
REM  PLUS33 Franchise Backend — Quick Start Script
REM  Starts the Spring Boot server on http://localhost:8080
REM =========================================================

SET JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.12.8-hotspot
SET MAVEN_HOME=C:\tools\apache-maven-3.9.6
SET PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo.
echo  =====================================================
echo    PLUS33 CAFE FRANCAIS - Franchise Backend Server
echo  =====================================================
echo.
echo  Java:   %JAVA_HOME%
echo  Maven:  %MAVEN_HOME%
echo  URL:    http://localhost:8080
echo  H2 DB:  http://localhost:8080/h2-console
echo.

cd /d "%~dp0"
mvn spring-boot:run

pause
