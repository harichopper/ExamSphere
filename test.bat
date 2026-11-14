@echo off
title ExamSphere — Test Suite
color 0B
cls

echo.
echo  =========================================
echo   ExamSphere - Test Suite
echo   OASIS INFOBYTE Java Internship - Task 4
echo  =========================================
echo.

REM ── Locate Java ───────────────────────────────────────────────────────────
where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo  [ERROR] Java not found. Install Java 17+ and add to PATH.
    pause
    exit /b 1
)

REM ── Locate Maven ──────────────────────────────────────────────────────────
set "MVN_PORTABLE=%USERPROFILE%\.maven\maven-3.9.14\bin\mvn.cmd"
set "MVN="

if exist "%MVN_PORTABLE%" (
    set "MVN=%MVN_PORTABLE%"
    goto :found_mvn
)
where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    set "MVN=mvn"
    goto :found_mvn
)
echo  [ERROR] Maven not found at %MVN_PORTABLE% and not on PATH.
pause
exit /b 1

:found_mvn
echo  [INFO] Running 33 automated tests...
echo.
"%MVN%" clean test
set "RESULT=%ERRORLEVEL%"
echo.
if %RESULT% equ 0 (
    echo  ==========================================
    echo   ALL TESTS PASSED
    echo  ==========================================
) else (
    echo  ==========================================
    echo   TESTS FAILED - check output above
    echo  ==========================================
)
echo.
pause
exit /b %RESULT%
