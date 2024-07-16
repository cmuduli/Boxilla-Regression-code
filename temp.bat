@echo off


call popd
call cd C:\Test_Workstation\SeleniumAutomation
IF EXIST "test-output\Screenshots" rmdir /s /q "test-output\Screenshots"
REM mkdir %BUILD_NUMBER%
java -cp C:\Test_Workstation\SeleniumAutomation\lib\*;C:\Test_Workstation\SeleniumAutomation\bin -Drelease=%LAST% -Dbrowser=%Browser% -Demerald=%emerald% -Demeraldse=%emeraldse% org.testng.TestNG testngSE.xml
REM call ant
REM call ant GenerateSeleniumReport
REM IF EXIST C:\Test_Workstation\SeleniumAutomation\Screenshots xcopy "C:\Test_Workstation\SeleniumAutomation\Screenshots" "C:\Test_Workstation\SeleniumAutomation\Screenshots\" /E
exit 0