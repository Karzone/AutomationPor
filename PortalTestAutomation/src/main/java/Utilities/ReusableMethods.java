package Utilities;

import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import pageobjects.Business;
import pageobjects.MachineUser;
import pageobjects.PersonUser;
import pageobjects.Repo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Step Definitions - Common Re-usable functions
 * Created by kalaiyak on 05/12/2017.
 * Copyrights : KCOM
 */
public class ReusableMethods extends ModuleBase {
    //region Reusable Actions
    /**
     * Repo Method
     * @param UserName User Name
     * @param Password Password
     * @param driver WebDriver
     * @throws Exception Exception
     */
    public static void Login(String UserName, String Password, WebDriver driver) throws Exception{
        try {
            WebDriverWait wait = new WebDriverWait(driver, 120);
            WebElement element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id ("idToken1")));
            element.clear ();
            ReplaceText ( element, UserName ); // User Name
            ReplaceText( Repo.txtPassword ( driver ), ( Password )); // Password
            logger.log ( LogStatus.INFO, "Enter Login Credentials for the user ["+ UserName + "]." );

            Repo.btnLogIn ( driver ).click (); // Login Button
            logger.log ( LogStatus.INFO, "Click on Login button." );

            //element = (new WebDriverWait(driver, 120)).until(ExpectedConditions.elementToBeClickable(Repo.btnHome(driver)));

            Thread.sleep(10000);
            Wait<WebDriver> fWait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(100))
                    .pollingEvery(Duration.ofMillis(600)).ignoring(NoSuchElementException.class);
            fWait.until(ExpectedConditions.elementToBeClickable(Repo.btnHome(driver)));

            // Verify Repo
            if (Repo.btnHome(driver).isEnabled())
            {
                logger.log ( LogStatus.PASS, "The User [" + UserName + "] Logged in successfully." );
            }
            else
                logger.log ( LogStatus.FAIL, "Login Unsuccessful : " + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );
        }catch(Exception e) {
            throw e;
        }
    }

    /**
     * Re-login after logout
     * @param driver WebDriver
     * @param userName login username
     * @param password Login password
     */
    public static void Relogin(WebDriver driver, String userName, String password) throws Exception{
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(Repo.lnkReturnLogin ( driver )).click().perform();
            ReusableMethods.Login ( userName, password, driver );
        }catch(Exception ex){
            throw ex;
        }
    }

    /**
     * Return to Login page
     * @param driver WebDriver
     */
    public static void ReturnToLoginPage(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, 15);
        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated( By.xpath ( "//A[@data-return-to-login-page=''][text()='Return to Login Page']" )));
        Repo.lnkReturnLogin ( driver ).click ();
    }

    /**
     * Login and Remember the Username in the login page
     * @param UserName The name of the user logging in
     * @param Password The password
     * @param driver WebDriver
     * @throws Exception Generic Exception
     */
    public static void LoginAndRemember(String UserName, String Password, WebDriver driver) throws Exception{
        try {
            Repo.txtUserName ( driver ).sendKeys ( UserName ); // User Name
            Repo.txtPassword ( driver ).sendKeys ( Password ); // Password
            logger.log ( LogStatus.INFO, "Enter Login Credentials for the user ["+ UserName + "]." );

            // Click on Remember me option
            if(!Repo.chkRememberMe ( driver ).isSelected ())
                Repo.chkRememberMe ( driver ).click ();
            logger.log ( LogStatus.INFO, "Check the Remember me option." );

            Repo.btnLogIn ( driver ).click (); // Login Button
            logger.log ( LogStatus.INFO, "Click on Login button." );

            // Verify Repo
            if (Repo.btnHome ( driver ).isDisplayed ())
            {
                logger.log ( LogStatus.PASS, "The User [" + UserName + "] Logged in successfully." + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );
            }
            else
                logger.log ( LogStatus.FAIL, "Login Unsuccessful : " + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );
        }catch(Exception e) {
            throw e;
        }
    }

    /**
     * Login for the first time after the user creation
     * @param UserName User Login name
     * @param genPassword Auto Generated default password
     * @param Password Login password
     * @param driver WebDriver
     * @throws Exception Generic Exception
     */
    public static void FirstTimeLogin(String UserName, String genPassword, String Password, WebDriver driver) throws Exception{
        try {
            WebDriverWait wait = new WebDriverWait(driver, 120);
            WebElement element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated( By.id ("idToken1")));
            element.clear ();
            ReplaceText ( Repo.txtUserName ( driver ), UserName ); // User Name
            ReplaceText( Repo.txtPassword ( driver ), ( genPassword )); // Password

            Repo.btnLogIn ( driver ).click (); // Login Button
            logger.log ( LogStatus.INFO, "First Time Login for the user [" + UserName + "]."+ logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );

            // First Time login password change
            Repo.txtUserName ( driver ).sendKeys (  genPassword ); // Auto-Gen Password
            Repo.txtPassword ( driver ).sendKeys (  Password ); // Password
            Repo.txtPasswordConfirm ( driver ).sendKeys ( Password); // Confirm password
            Repo.btnSubmit ( driver ).click ();

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//li/a/span[contains(text(), 'home')]")));

            // Verify Repo
            if (Repo.btnHome ( driver ).isDisplayed ())
            {
                logger.log ( LogStatus.PASS, "The User [" + UserName + "] Logged in successfully." + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );
            }
            else
                logger.log ( LogStatus.FAIL, "Login Unsuccessful : " + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );

        }catch(Exception e) {
            throw e;
        }
    }

    /**
     * Logout the session
     * @param driver Web Driver
     * @param userName User logged in
     * @throws Exception Generic Exception
     */
    public static void Logout(WebDriver driver, String userName) throws Exception{
        try{
            WebDriverWait wait = new WebDriverWait(driver, 15);
            WebElement element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated( By.xpath ( "//li/a/i[@class='menu__icon icon-profile']" )));

            if(userName.equals(Constants.EMPTY)) Repo.btnMyProfile ( driver ).click ();
            else driver.findElement(By.xpath("//span[contains(text(), '" + userName + "')]")).click();

            Repo.btnLogout ( driver ).click ();
            // element = wait.until ( ExpectedConditions.visibilityOfElementLocated ( By.xpath ( "//H1[text()='You have been logged out.']" ) ) );
            logger.log ( LogStatus.PASS, "The User has logged out successfully." + logger.addScreenCapture ( CaptureScreenshot ( driver ))) ;
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Logout the session
     * @param driver Web Driver
     * @throws Exception Generic Exception
     */
    public static void Logout(WebDriver driver) throws Exception{
        try{
            WebDriverWait wait = new WebDriverWait(driver, 15);
            WebElement element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated( By.xpath ( "//li/a/i[@class='menu__icon icon-profile']" )));

            Repo.btnMyProfile ( driver ).click ();

            Repo.btnLogout ( driver ).click ();
            // element = wait.until ( ExpectedConditions.visibilityOfElementLocated ( By.xpath ( "//H1[text()='You have been logged out.']" ) ) );
            logger.log ( LogStatus.PASS, "The User has logged out successfully." + logger.addScreenCapture ( CaptureScreenshot ( driver ))) ;
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Forgot Password
     * @param userName Username for whom the password need to be reset
     * @throws Exception Generic Exception
     */
    public static void ForgotPassword(String userName, WebDriver driver) throws Exception{
        try{
            // Click on the 'Forgot your password' link in the login page
            Repo.lnkForgotPassword (driver).click ();

            //Re-captcha
            driver.findElement(By.cssSelector("iframe")).click();

            // Enter Reset password and submit
            Repo.txtResetPassword ( driver ).sendKeys ( userName );
            Repo.btnSubmit ( driver ).click ();

            logger.log ( LogStatus.INFO, "The Person user [" + userName + "] password is reset using the username and email."
                    + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );

            // Back to the Login page
            Repo.lnkReturnToLoginPage ( driver ).click ();

        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Reset the Person user password through the link available in the email
     * @throws Exception Generic Exception
     */
    public static void ResetPasswordThroEmailLink(WebDriver driver) throws Exception{
        try{
            String message = GetMailContent("Link");
            String link = Constants.EMPTY;
            Pattern pattern = Pattern.compile(Pattern.quote("href='") + "(.*?)" + Pattern.quote("'>link"));
            Matcher matcher = pattern.matcher ( message );
            while(matcher.find ()) {
                link = matcher.group ( 1 );
            }
            logger.log ( LogStatus.INFO, "Reset the password using the link - [" + link + "]");
            driver.get ( link );

            // Enter new password and submit
            pageobjects.ForgotPassword.txtPassword ( driver ).click ();
            pageobjects.ForgotPassword.txtPassword ( driver ).sendKeys ( Constants.CREATE_PERSON_STANDARD_PASSWORD);
            Thread.sleep ( 2000 );
            pageobjects.ForgotPassword.txtConfirmPassword ( driver ).click ();
            pageobjects.ForgotPassword.txtConfirmPassword ( driver ).sendKeys ( Constants.CREATE_PERSON_STANDARD_PASSWORD);
            Thread.sleep ( 2000 );

            SetFocus ( driver, Repo.btnSubmit ( driver ));
            Repo.btnSubmit ( driver ).click ();

            logger.log ( LogStatus.INFO, "The Password has been reset successfully."
                    + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );

        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Select Drop Down Item
     * @param dropdown Drop Down
     * @param itemToSelect Item to select in the Drop Down
     */
    public static void Select(WebElement dropdown, String itemToSelect){
        try{
            Select selDropDown = new Select ( dropdown );
            selDropDown.selectByVisibleText ( itemToSelect );

        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Select Business Menu Item
     * @param driver Driver
     */
    public static void SelectBusinessesMenu(WebDriver driver){
        try{
            Repo.mnuManage ( driver ).click ();
            Repo.mnuBusinesses ( driver ).click ();
            logger.log ( LogStatus.INFO, "Select Manaage > Businesses menu." );
        }
        catch(Exception e){
            throw e;
        }
    }

    /**
     * Select Manage > Users
     * @param driver Driver
     */
    public static void SelectUsersMenu(WebDriver driver){
        try{
            Repo.mnuManage (driver).click ();
            Repo.mnuUsers ( driver ).click ();
            logger.log ( LogStatus.INFO, "Select Manaage > Users menu." );
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Search Person User based on User Name
     * @param userName Name of the user/Id
     * @param business Name of the business
     * @throws Exception Generic Exception
     */
    public static void SearchPersonUser(WebDriver driver, String userName, String business) throws Exception{
        try{
            // Select Search Page
            SelectUsersMenu ( driver );

            // Select Business [Leave the Business as Empty "", if using Business User Admin]
            if(business != "") ReusableMethods.Select ( MachineUser.Search_By_Business(driver), business );

            logger.log ( LogStatus.INFO, "Search Person User [" + userName + "]  "  );

            new WebDriverWait(driver, 10).until(
                    webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

            // Search Username
            ReplaceText(Business.txtQuickSearch(driver), userName);

            // Click Search button
            Business.btnSearch(driver).click();

            // Open Searched Record
            DoubleClick(driver, PersonUser.cllSearchElement(driver));

        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Search Machine User based on User Name
     * @param driver WebDriver
     * @param userName Name of the user/Id
     * @param business Name of the business
     * @throws Exception Generic Exception
     */
    public static void SearchMachineUser(WebDriver driver, String userName, String business) throws Exception{
        try{

            // Select Search Page
            SelectUsersMenu ( driver );

            // Select Business [Leave the Business as Empty "", if using Business User Admin]
            if(business != "") Select ( MachineUser.Search_By_Business(driver), business );

            // Select User Type as 'Machine
            ReusableMethods.Select ( MachineUser.Search_By_UserType ( driver ), "Machine" );

            new WebDriverWait(driver, 10).until(
                    webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

            // Search Username
            ReplaceText(Business.txtQuickSearch(driver), userName);

            int test = driver.findElements(By.xpath("//div[@col-id='id']")).size();
            System.out.print(test);
            while(driver.findElements(By.xpath("//div[@col-id='id']")).size() <=1){
                    Thread.sleep(1000);
            }
            ReplaceText(Business.txtQuickSearch(driver), userName);
            // Click Search button
            Business.txtQuickSearch(driver).sendKeys(Keys.RETURN);
            //Business.grcSearchElement(driver).click();

            // Open Searched Record
            DoubleClick(driver, PersonUser.cllSearchElement(driver));

        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Find Business
     * @param driver WebDriver
     * @param businessName Name of the Business
     */
    public static void FindBusiness(WebDriver driver, String businessName) throws Exception{
        try{
            ReplaceText(Business.txtQuickSearch(driver), businessName);
            Business.btnSearch(driver).click();

            DoubleClick(driver, Business.grcSearchElement(driver));
            Business.btnClose(driver).click();

        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Find/Search the Table Element
     * @param driver WebDriver
     * @param businessName
     */
    public static void FindElementInGrid(WebDriver driver, String businessName) throws Exception {
        try {
            // Sort based on business
            pageobjects.Repo.hdrBusiness ( driver ).click ();

            // TODO : WorkAround : Page Refresh
            driver.navigate ().refresh ();

            // Sort again based on business
            pageobjects.Repo.hdrBusiness ( driver ).click ();

            List<WebElement> allpages = driver.findElements ( By.xpath ( "//div[@class='container']//a" ) );
            List<WebElement> itemsToFind = driver.findElements ( By.xpath ( "//div[@class='ag-body-container']//div[@colid='description']" ) );
            int pageSize = allpages.size () - 2;
            boolean isFound = false;
            int i = 1;
            do {
                i = i + 1;
                for (int j = 0; j < itemsToFind.size (); j++) {
                    if (itemsToFind.get ( j ).getText ().equals ( businessName )) {
                        Thread.sleep ( 3000 );

                        // Highlight Element
                        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript ( "arguments[0].style.border='3px solid red'", itemsToFind.get ( j ) );
                        DoubleClick ( driver, itemsToFind.get ( j ) );
                        Thread.sleep ( 3000 );

                        isFound = true;
                        break;
                    }
                }
                if (isFound) {
                    break;
                } else {
                    allpages = driver.findElements ( By.xpath ( "//div[@class='container']//a" ) );
                    allpages.get ( i ).click ();
                    itemsToFind = driver.findElements ( By.xpath ( "//div[@class='ag-body-container']//div[@colid='description']" ) );
                }
            } while (!isFound);
        }catch(IndexOutOfBoundsException ex){
            logger.log ( LogStatus.FAIL, "The business [" + businessName + "] is not available." );
            throw ex;
        }
        catch (Exception exception) {
            throw exception;
        }
    }

    /**
     * Assign Roles to the Users
     * @param driver Driver
     * @param roles Roles To assign
     * @throws Exception Generic Exception
     */
    public static void AssignRolesToUser(WebDriver driver, boolean isPerson, String[] roles) throws Exception{
        try{
            logger.log ( LogStatus.INFO, "Navigating to Edit User Page"  );

            int i = 0;
            while(roles[i] !="" && roles[i] !=null) {
                DragAndDrop ( driver, driver.findElement ( By.xpath ( "//li/SPAN[contains(text(), '" + roles[i] + "')]" ) ),
                        MachineUser.drgDropLocation ( driver ) );
                logger.log ( LogStatus.INFO, "The Role [" + roles[i] + "] Added to the user "  );
                i = i+1;
            }
            logger.log ( LogStatus.PASS, "The Role(s) are updated for the user "  ); //Screenshots disabled due to performance issues

            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.elementToBeClickable(PersonUser.btnSave(driver)));

            PersonUser.btnSave(driver).click();

            while(PersonUser.btnSave(driver).isEnabled()){
                Thread.sleep(1000);
            }

            wait.until(ExpectedConditions.elementToBeClickable(PersonUser.btnClose(driver)));

            PersonUser.btnClose ( driver ).click ();
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Verify the Roles and Assign Roles to the Users
     * @param driver Driver
     * @param rolesToAssign Roles To assign
     * @param rolesToVerify Roles that need to be verified
     * @throws Exception Generic Exception
     */
    public static void VerifyAndAssignRolesToUser(WebDriver driver, boolean isPerson, String[] rolesToAssign, String rolesToVerify) throws Exception{
        try{
            WebDriverWait wait = new WebDriverWait(driver, 10);

            // Verify the roles
            List<WebElement> roleItems = Repo.grpRoles ( driver );

            boolean isRoleVerified = true;
            for(WebElement e : roleItems){
                if(!rolesToVerify.toLowerCase ().contains ( e.getText ().toLowerCase () ) ){
                    isRoleVerified = false;
                }
            }

            if(isRoleVerified){
                logger.log ( LogStatus.INFO, "The Role(s) available to the user are verified and " +
                        "they are based on the application roles assigned to the business.");
            }
            else
            {
                logger.log ( LogStatus.FAIL, "The Role(s) assigned to the user is not " +
                        "as per the application roles assigned to the business.");
            }

            // Assign roles
            for(int i = 0; i < rolesToAssign.length; i++) {
                if (rolesToAssign[i] != "" && rolesToAssign[i] != null) {
                    DragAndDrop ( driver, driver.findElement ( By.xpath ( "//li/span[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), " +
                            "translate('" + rolesToAssign[i] + "','ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'))]") ),
                            MachineUser.drgDropLocation ( driver ) );
                    logger.log ( LogStatus.INFO, "The Role [" + rolesToAssign[i] + "] Added to the user " );
                }
            }
            logger.log ( LogStatus.PASS, "The Role(s) are updated for the user " + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );

            wait.until(ExpectedConditions.elementToBeClickable(PersonUser.btnSave(driver)));

            PersonUser.btnSave(driver).click();

            while(PersonUser.btnSave(driver).isEnabled()){
                Thread.sleep(1000);
            }

            wait.until(ExpectedConditions.elementToBeClickable(PersonUser.btnClose(driver)));

            PersonUser.btnClose ( driver ).click ();
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Assign Applications to business
     * @param driver Driver
     * @param roles Application Roles to Assign
     * @throws Exception Generic Exception
     */
    public static void AssignApplicationsToBusiness(WebDriver driver, String[] roles) throws Exception{
        try{
            //Business.btnEdit ( driver ).click ();
            logger.log ( LogStatus.INFO, "Navigating to Edit User Page"  );
            int i = 0;
            while(roles[i] !="" && roles[i] !=null) {
                //simulateDragAndDrop(driver, driver.findElement ( By.xpath ( "//li/span[contains(text(), '" + roles[i] + "')]" )), Repo.drgBusinessDropLocation ( driver ));
                DragAndDrop ( driver, driver.findElement ( By.xpath ( "//li/span[contains(text(), '" + roles[i] + "')]" ) ),
                        Repo.drgBusinessDropLocation ( driver ) );
                logger.log ( LogStatus.INFO, "The Role [" + roles[i] + "] Added to the Business "  );
                i = i+1;
            }
            logger.log ( LogStatus.PASS, "The Application Role(s) are updated for the business " );
            Business.btnSave ( driver ).click ();

            while(PersonUser.btnSave(driver).isEnabled()){
                Thread.sleep(1000);
            }

            Business.btnClose ( driver ).click ();
            Business.btnClose ( driver ).click ();
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Verify the application accesses
     * @param appsToVerify Application Roles to Verify
     * @param driver WebDriver
     * @throws Exception Generic Exception
     */
    public static void VerifyApplications(String[] appsToVerify, WebDriver driver) throws Exception
    {
        try{
            boolean hasElement = false; boolean isFound = false;
            for(int i = 0; i<appsToVerify.length; i++) {
                if(appsToVerify[i] != Constants.EMPTY && appsToVerify[i] !=null)
                    hasElement = true;
            }
            if(hasElement) {
                List<WebElement> apps = PersonUser.lblApplications ( driver );
                List<String> applications = new ArrayList<String> ();

                for (int i = 0; i < apps.size (); i++) {
                    applications.add ( apps.get ( i ).getText () );
                }

                int sizeAppElements = applications.size ();
                int nonEmptyApps = 0;
                for (int j = 0; j < appsToVerify.length; j++) {
                    if (appsToVerify[j] != Constants.EMPTY && appsToVerify[j] != null) {
                        for(String appsList : applications){
                            if(appsList.contains ( appsToVerify[j] )) {isFound = true;break;}
                        }
                        if (isFound) {
                            logger.log ( LogStatus.PASS, "The Person user has access to [" + appsToVerify[j] + "] application." );

                            // Navigate to the application
                            driver.findElement ( By.xpath ( "//LABEL[contains(text(),'" + appsToVerify[j] + "')]" ) ).click ();

                            // Check if the URL has got application name
                            if (driver.getCurrentUrl ().toLowerCase ().contains ( appsToVerify[j].toLowerCase () ) ||
                                    (appsToVerify[j].toLowerCase ().equals("mec") && driver.getCurrentUrl ().toLowerCase ().contains ( "dtd" )))
                            {
                                logger.log(LogStatus.INFO, "The Page successfully navigated to the [" + appsToVerify[j] + "] URL - [" + driver.getCurrentUrl() + "].");
                            }
                            else {
                                logger.log(LogStatus.FAIL, "The Page navigated to incorrect URL - [" + driver.getCurrentUrl() + "].");
                            }

                            // navigate back to the Home page
                            driver.navigate ().back ();
                            isFound = false;
                        } else
                            logger.log ( LogStatus.FAIL, "The Person user does not have access to [" + appsToVerify[j] + "] application." );
                        nonEmptyApps = nonEmptyApps + 1;
                    }
                }

                // Check, if the user has got proper application access
                if (sizeAppElements != nonEmptyApps) {
                    logger.log ( LogStatus.FAIL, "The Person user either do not have access to proper application(s) or " +
                            "have incorrect access to the application(s)" + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );
                }
            }
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Deactivate Person User
     * @param userId UserName
     * @throws Exception Generic Exception
     */
    public static void DeactivatePerson(WebDriver driver, boolean isPerson, String userId, String status) throws Exception{
        try{
            String userType;
            WebDriverWait wait = new WebDriverWait(driver, 120);
            MachineUser.btnEdit ( driver ).click ();
            String lblStatus = PersonUser.lblStatus ( driver ).getText ();
            if(!lblStatus.equalsIgnoreCase ( status )) MachineUser.slrStatus ( driver ).click ();
            if(isPerson){
                userType = "Person";
            }else {userType = "Machine";}
            PersonUser.btnSave ( driver ).click ();
            logger.log ( LogStatus.INFO, "The " + userType +" User [" + userId + "] has been made " + status + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );

            while(PersonUser.btnSave(driver).isEnabled()){
                Thread.sleep(1000);
            }

            wait.until(ExpectedConditions.elementToBeClickable(PersonUser.btnClose(driver)));

            PersonUser.btnClose ( driver ).click ();
            PersonUser.btnClose ( driver ).click ();
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Reset password for the user
     * @param driver Driver
     * @throws Exception Generic Exception
     */
    public static void ResetPassword(WebDriver driver) throws Exception {
        try {
            MachineUser.btnEdit ( driver ).click ();
            logger.log ( LogStatus.INFO, "Navigating to Edit User Page" );
            MachineUser.btnResetPassword ( driver ).click (); // Reset password
            logger.log ( LogStatus.INFO, "The Person user password has been reset successfully. ");

            MachineUser.btnClose ( driver ).click ();
        } catch (Exception exception) {
            throw exception;
        }
    }

    /**
     * Read the auto generated password from the E-mail notification
     * @return Password
     * @throws Exception Generic Exception
     */
    public static String ReadPasswordFromEmail() throws Exception{
        try{
            String message = GetMailContent("temporary password for your account is");
            String genPassword = Constants.EMPTY;
            Pattern pattern = Pattern.compile(Pattern.quote("password for your account is ") + "(.*?)" + Pattern.quote("\r\n"));
            Matcher matcher = pattern.matcher ( message );
            while(matcher.find ()) {
                genPassword = matcher.group ( 1 );
            }
            logger.log ( LogStatus.INFO, "The New Auto-Generated Password [" + genPassword + "] has been captured.");
            return genPassword;
        }catch(Exception exception){
            throw exception;
        }
    }
}
