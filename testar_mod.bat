@echo off
chcp 65001 >nul
echo ===================================================
echo   Iniciando Minecraft 1.21.4 com o mod Queijo Mineiro
echo ===================================================
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.13.9-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "%~dp0"
call .\gradlew.bat runClient
pause
