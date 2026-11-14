@echo off
title ExamSphere — Online Examination System

REM ── Locate Java ──────────────────────────────────────────────────────────
where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Java is not found on the PATH.
    echo         Please install Java 17+ and add it to your PATH.
    echo         Download: https://adoptium.net/
    pause
    exit /b 1
)

REM ── Locate Maven (portable path first, then PATH) ─────────────────────────
set "MVN_PORTABLE=%USERPROFILE%\.maven\maven-3.9.14\bin\mvn.cmd"
if exist "%MVN_PORTABLE%" (
    set "MVN=%MVN_PORTABLE%"
    goto :found_mvn
)
where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    set "MVN=mvn"
    goto :found_mvn
)
echo [ERROR] Maven not found. Trying to run the fat JAR directly...

REM ── Fallback: run fat JAR if already built ────────────────────────────────
set "JAR=target\online-examination-system-1.0.0.jar"
if exist "%JAR%" (
    echo [INFO] Launching from pre-built JAR: %JAR%
    java -jar "%JAR%"
    exit /b %ERRORLEVEL%
)
echo [ERROR] No JAR found either. Please run:
echo         mvn clean package
echo         then re-run this script.
pause
exit /b 1

:found_mvn
echo [INFO] Using Maven: %MVN%
echo [INFO] Building and launching ExamSphere...
"%MVN%" -q clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Build failed. Run  mvn clean package  for details.
    pause
    exit /b 1
)
java -jar target\online-examination-system-1.0.0.jar
exit /b %ERRORLEVEL%
