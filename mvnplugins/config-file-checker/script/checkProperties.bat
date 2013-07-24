@echo off
if "%JAVA_HOME%".==. goto NO_JAVA_HOME
set PROPERTY_HOME=%CLARITY_HOME%
set TEMPLATE_HOME=../config
if NOT "%1"=="" set PROPERTY_HOME=%1
if NOT "%2"=="" set TEMPLATE_HOME=%2
if "%PROPERTY_HOME%"=="" goto NO_CLARITY_HOME
"%JAVA_HOME%/bin/java.exe" -cp ./config-file-checker/config-file-checker.jar com.clarity.tools.PropertiesDiff %PROPERTY_HOME% %TEMPLATE_HOME% modules
goto END
:NO_JAVA_HOME
echo Please set JAVA_HOME environment variable to your JRE/JDK installation location
goto END
:NO_CLARITY_HOME
echo Please set the CLARITY_HOME variable or pass the Clarity home location as a parameter
:END
set TEMPLATE_HOME=
set PROPERTY_HOME=
