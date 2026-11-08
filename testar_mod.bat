@echo off
chcp 65001 >nul
echo ===================================================
echo   Iniciando Minecraft 26.3 com o mod Queijo Mineiro
echo ===================================================
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.5.8-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "%~dp0"
call .\gradlew.bat runClient
pause
