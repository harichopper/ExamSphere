@echo off
title ExamSphere — Online Examination System
color 0A
cls

echo.
echo  =========================================
echo   ExamSphere - Online Examination System
echo   OASIS INFOBYTE Java Internship - Task 4
echo  =========================================
echo.

REM ── Locate Java ───────────────────────────────────────────────────────────
where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo  [ERROR] Java not found on PATH.
    echo          Install Java 17+ from https://adoptium.net/
    echo          and add it to your system PATH.
    echo.
    pause
    exit /b 1
)

for /f "tokens=*" %%i in ('java -version 2^>^&1') do (
    echo  [OK] %%i
    goto :java_done
)
:java_done

echo.

REM ── Locate Maven ──────────────────────────────────────────────────────────
set "MVN_PORTABLE=%USERPROFILE%\.maven\maven-3.9.14\bin\mvn.cmd"
set "MVN="

if exist "%MVN_PORTABLE%" (
    set "MVN=%MVN_PORTABLE%"
    echo  [OK] Maven found: %MVN_PORTABLE%
    goto :found_mvn
)

where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    set "MVN=mvn"
    echo  [OK] Maven found on PATH
    goto :found_mvn
)

REM ── No Maven — try pre-built JAR ──────────────────────────────────────────
echo  [WARN] Maven not found. Checking for pre-built JAR...
set "JAR=%~dp0target\online-examination-system-1.0.0.jar"
if exist "%JAR%" (
    echo  [OK]  JAR found: %JAR%
    echo.
    echo  Launching ExamSphere...
    start "" javaw -jar "%JAR%"
    echo  [OK]  Application started!
    timeout /t 3 >nul
    exit /b 0
)

echo  [ERROR] No JAR found. Run this command first:
echo.
echo     %MVN_PORTABLE% clean package
echo.
pause
exit /b 1

:found_mvn
echo.
echo  Building ExamSphere (this takes ~15 seconds on first run)...
echo.
"%MVN%" clean package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo.
    echo  [ERROR] Build failed!
    echo          Run the following for details:
    echo          "%MVN%" clean package
    echo.
    pause
    exit /b 1
)

echo  [OK]  Build successful!
echo.
echo  Launching ExamSphere...
set "JAR=%~dp0target\online-examination-system-1.0.0.jar"
start "" javaw -jar "%JAR%"
echo  [OK]  ExamSphere is starting...
echo.
echo  Demo login: student / student123
echo.
timeout /t 3 >nul
exit /b 0
