@echo off

call pushd \\10.10.10.61\share

set "release1="
set "release2="
setlocal enableDelayedExpansion
for /f %%i in ('java -jar C:\Test_Workstation\SeleniumAutomation\lib\versionGetter.jar') do set release1=%%i
for /f %%i in ('java -jar C:\Test_Workstation\SeleniumAutomation\lib\versionGetter.jar -soak') do (
set release2=%%i
)

echo New bat file
echo This is first recent file %release1%
echo This is second recent file %release2%

call popd
call cd C:\Test_Workstation\SeleniumAutomation
IF EXIST "test-output\Screenshots" rmdir /s /q "test-output\Screenshots"

java -cp C:\Test_Workstation\SeleniumAutomation\lib\*;C:\Test_Workstation\SeleniumAutomation\bin -Dbrowser=%browser% -Dcount=%count% -Drelease1=%release1% -Drelease2=%release2% -Demerald=%emerald% -Demeraldse=%emeraldse% org.testng.TestNG soaktestFull.xml
REM call ant
REM call ant GenerateSeleniumReport
REM IF EXIST C:\Test_Workstation\SeleniumAutomation\Screenshots xcopy "C:\Test_Workstation\SeleniumAutomation\Screenshots" "C:\Test_Workstation\SeleniumAutomation\Screenshots\" /E