@echo off
@setlocal enabledelayedexpansion
set CURRENT_DIR=%cd%
cd ..
set CURRENT_DIR=%cd%
set LIB_DIR=%CURRENT_DIR%\lib
set CLASSPATH=
set CLASSPATH2=
set CLASSPATH3=
set COUNT=1
for %%f in (%LIB_DIR%/*.jar) do (
if !COUNT! LSS 50 (
  set CLASSPATH=!CLASSPATH!lib\%%~nxf;
) else (
  if !COUNT! LSS 100 (
   set CLASSPATH2=!CLASSPATH2!lib\%%~nxf;
  ) else (
   set CLASSPATH3=!CLASSPATH3!lib\%%~nxf;
  )
)
set /a COUNT=!COUNT!+1
)
set MAINCLASS=pers.zcc.scm.web.launch.AppBootstrap
set JAVA_OPTS=-D"spring.profiles.active=dev" -D"file.encoding=UTF-8" -X"ms1024m" -X"mx1024m"
java %JAVA_OPTS% -classpath ".;%CLASSPATH%%CLASSPATH2%%CLASSPATH3%" %MAINCLASS%
echo started