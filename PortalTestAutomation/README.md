# Portal - Test Automation

The objective is to cover the sanity tests and the functional tests that are executed in every new build.

## Getting Started

Please download the copy of the Test Solution from the following GitHub folder
https://github.com/KCOM-Enterprise/TestPractice/tree/master/Portal/PortalTestAutomation

### Prerequisites

The following prerequisites are required to be installed/configured before running the automation test pack.
1.	Install Java 8 (or) higher.
2.	Install Maven
3.	Add Java and Maven path into the System Variables
4.	Install Intellij IDEA

## Build the tests
1. Generate build without executing the automated tests

```
mvn clean install -DskipTests
```

2. Generate build with automation test execution

```
mvn clean install
```

## Running the tests

1. To execute Admin Console - Sanity Tests
```
mvn -Dtest=CreateBusinessTest#SanityTest test
```

2. To execute other functional tests
```
mvn -Dtest=CreatePersonUser#PersonCreationTest test
mvn -Dtest=CreateMachineUser#MachineCreationTest test
mvn -Dtest=EditProfileSettings#EditProfileTest test
mvn -Dtest=LockedAccount#UnLockAccountTest test
mvn -Dtest=RememberUserName#RememberUserNameTest test
mvn -Dtest=ResetPersonUserPassword#ResetPersonUserPasswordTest test
mvn -Dtest=SearchPersonUser#SearchPersonUserTest test
mvn -Dtest=VerifySecuredCommunication#VerifySSLTest test
```
3. To execute the tests in different browsers(chrome/firefox/ie/safari)
Change the following property in the config.properties file (//src/main/java/DataInputs)
```
For Example : BROWSER_DRIVER = chrome
```

4. To execute the tests in different test environments(DEV/SYSTST/UAT/PRE_PROD)
Change the following property in the config.properties file (//src/main/java/DataInputs)
```
For Example : ENVIRONMENT = UAT
```


## Built With

* [Selenium Web Driver](https://www.seleniumhq.org/projects/webdriver/) - The automation framework used
* [Maven](https://maven.apache.org/) - Dependency Management


## Authors

* **Karthik Kalaiyarasu** - *Initial work*


