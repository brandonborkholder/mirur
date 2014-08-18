@echo off

set EHOME=%ECLIPSE_HOME%
IF %EHOME:~-1%==\ SET EHOME=%EHOME:~0,-1%

"%JAVA_HOME%\bin\java" -Declipse.home="%EHOME%"  -jar "%PG_JAR%" @proguard.pro

xcopy target\obfuscated-classes target\classes /E /Y

rmdir /Q /S target\obfuscated-classes