@echo off

call pushd \\10.10.10.61\share

REM call cd C:\Jimit\Boxilla\Version
REM to upload a specific version add it as a parameter to the jar call below
for /f %%i in ('java -jar C:\Test_Workstation\SeleniumAutomation\lib\versionGetter.jar') do set LAST=%%i
REM for /f %%i in ('dir /b/a-d/od/t:c') do set LAST=%%i REM last accessed
echo New bat file
echo Most recent build is %LAST%
REM echo %LAST:~8,4%
call popd
call cd C:\Test_Workstation\SeleniumAutomation
IF EXIST "test-output\Screenshots" rmdir /s /q "test-output\Screenshots"
REM mkdir %BUILD_NUMBER%
java -cp C:\Test_Workstation\SeleniumAutomation\lib\*;C:\Test_Workstation\SeleniumAutomation\bin -Drelease=%LAST% -Dbrowser=%Browser% org.testng.TestNG REST_API.xml
REM call ant
REM call ant GenerateSeleniumReport
REM IF EXIST C:\Test_Workstation\SeleniumAutomation\Screenshots xcopy "C:\Test_Workstation\SeleniumAutomation\Screenshots" "C:\Test_Workstation\SeleniumAutomation\Screenshots\" /E