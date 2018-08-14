
EXECUTING TEST IN THE GENERATED UI_TESTAUTOMATION.JAR FILE
The following are examples command lines to run Selenium Webdriver Demo:

java -classpath UI_TestAutomation.jar Demo.sample --driver=firefox
java -classpath UI_TestAutomation.jar Demo.sample --driver=chrome
java -classpath UI_TestAutomation.jar Demo.sample --driver=ie
java -classpath UI_TestAutomation.jar Demo.sample --driver=firefox
java -classpath UI_TestAutomation.jar Demo.sample  (if driver parameter is not specified firefox is used by default)



JENKINS INTEGRATION
Example Jenkins windows batch command build-steps are below for reference:

-- Copy Demo from fixed repository to build location
CD /../Jenkins
MKDIR Build_%BUILD_ID%\TestScripts
XCOPY C:\Jenkins\Tests_Repository C:\Jenkins\Build_%BUILD_ID%\TestScripts /s /i

-- Execute Demo
CD \Jenkins\Build_%BUILD_ID%\TestScripts
java -classpath UI_TestAutomation.jar Demo.sample --driver=firefox
java -classpath UI_TestAutomation.jar Demo.sample --driver=chrome

--Adding Dependencies
http://stackoverflow.com/questions/11958024/using-custom-jar-libraries-in-command-prompt-and-intellij-idea
