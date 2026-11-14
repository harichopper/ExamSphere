@echo off
title ExamSphere — Test Suite

REM ── Locate Java ──────────────────────────────────────────────────────────
where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Java is not found on the PATH.
    pause
    exit /b 1
)

REM ── Locate Maven ─────────────────────────────────────────────────────────
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
echo [ERROR] Maven not found at %MVN_PORTABLE% and not on PATH.
pause
exit /b 1

:found_mvn
echo [INFO] Running ExamSphere test suite...
echo.
"%MVN%" clean test
set "RESULT=%ERRORLEVEL%"
echo.
if %RESULT% equ 0 (
    echo [SUCCESS] All tests passed.
) else (
    echo [FAILURE] One or more tests failed. Check output above for details.
)
echo.
pause
exit /b %RESULT%
