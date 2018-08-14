package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by KKalaiyarasu on 23/08/2016.
 * THIS CLASS CONTAINS THE ELEMENT IDENTIFIERS SPECIFIC TO LOGIN PAGE
 * AND SESSIONS HANDLING
 */
public class Repo {
    private static WebElement element = null;
    private static List<WebElement> elementList = null;

    // Find User Name
    public static WebElement txtUserName(WebDriver driver){
        element = driver.findElement( By.id ("idToken1"));
        return element;
    }

    // Find Password
    public static WebElement txtPassword(WebDriver driver){
        element = driver.findElement(By.id("idToken2"));
        return element;
    }

    // Find Confirm Password
    public static WebElement txtPasswordConfirm(WebDriver driver){
        element = driver.findElement(By.xpath ("//INPUT[@id='idToken3']"));
        return element;
    }

    // Find Log In Button
    public static WebElement btnLogIn(WebDriver driver){
        element = driver.findElement(By.id("loginButton_0"));
        return element;
    }

    // Remember me
    public static WebElement chkRememberMe(WebDriver driver){
        element = driver.findElement(By.id("remember"));
        return element;
    }

    // Forgot password link
    public static WebElement lnkForgotPassword(WebDriver driver){
        element = driver.findElement(By.xpath ( "//a[text()='Forgot your password?']" ));
        return element;
    }

    // Reset Password Textbox
    public static WebElement txtResetPassword(WebDriver driver){
        element = driver.findElement(By.xpath ( "//INPUT[@id='input-cn']" ));
        return element;
    }

    // Submit button
    public static WebElement btnSubmit(WebDriver driver){
        element = driver.findElement(By.xpath ( "//INPUT[@type='submit' and @value ='Submit']" ));
        return element;
    }

    // Email id
    public static WebElement txtResetEmail(WebDriver driver){
        element = driver.findElement(By.xpath ( "//INPUT[@id='input-mail']" ));
        return element;
    }

    // Return to Login page
    public static WebElement lnkReturnToLoginPage(WebDriver driver){
        element = driver.findElement(By.xpath ("//A[@id='anonymousProcessReturn']" ));
        return element;
    }

    // Find Home Page
    public static WebElement btnHome(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//li/a/span[contains(text(), 'home')]"));
        return element;
    }

    // My profile
    public static WebElement btnMyProfile(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//li/a/i[@class='menu__icon icon-profile']" ) );
        return element;
    }

    // Telephone
    public static WebElement txtTelephone(WebDriver driver){
        element = driver.findElement ( By.xpath ( "(//INPUT[@autocomplete='off'])[1]" ) );
        return element;
    }

    // Old Password
    public static WebElement txtOldPassword(WebDriver driver){
        element = driver.findElement ( By.xpath ( "(//INPUT[@autocomplete='off'])[2]" ) );
        return element;
    }

    // New Password
    public static WebElement txtNewPassword(WebDriver driver){
        element = driver.findElement ( By.xpath ( "(//INPUT[@autocomplete='off'])[3]" ) );
        return element;
    }

    // Retype Password
    public static WebElement txtRetypePassword(WebDriver driver){
        element = driver.findElement ( By.xpath ( "(//INPUT[@autocomplete='off'])[4]" ) );
        return element;
    }

    // Update Profile Password
    public static WebElement btnUpdateProfilePassword(WebDriver driver){
        element = driver.findElement ( By.xpath ( "(//BUTTON[@tabindex='0'])[2]" ) );
        return element;
    }

    // Settings
    public static WebElement mnuSettings(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//SPAN[@class='menu__dropdown-title'][text()='settings']" ) );
        return element;
    }

    // Save Profile button
    public static WebElement btnSaveProfile(WebDriver driver){
        element = driver.findElement ( By.xpath ( "(//BUTTON[@tabindex='0'])[1]" ) );
        return element;
    }

    // Logout
    public static WebElement btnLogout(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//span[@class='menu__dropdown-title'][text()='log out']" ) );
        return element;
    }

    // Logout Text
    public static WebElement lblLogout(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//H1[text()='You have been logged out.']" ) );
        return element;
    }

    // Return to Login page
    public static WebElement lnkReturnLogin(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//A[@data-return-to-login-page=''][text()='Return to Login Page']" ) );
        return element;
    }

    // Edit button
    public static WebElement btnEdit(WebDriver driver){
        element = driver.findElement ( By.cssSelector ( "button.btn.btn-secondary" ) );
        return element;
    }

    // Manage
    public static WebElement mnuManage(WebDriver driver){
        element = driver.findElement ( By.xpath ( "(//A[@class='menu__link'])[2]" ) );
        return element;
    }

    // Users
    public static WebElement mnuUsers(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//SPAN[@class='menu__dropdown-title'][text()='users']" ) );
        return element;
    }

    // businesses
    public static WebElement mnuBusinesses(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//SPAN[@class='menu__dropdown-title'][text()='businesses']" ) );
        return element;
    }

    // Drag and Drop Location
    public static WebElement drgBusinessDropLocation(WebDriver driver){
        //element = driver.findElement ( By.xpath ( "//div/ul[@id = 'assignedList']" ) );
        element = driver.findElement ( By.xpath ( "//div[2]/rdg-drag-drop//div[1]/div/ul/li" ) );
        return element;
    }

    // Create User
    public static WebElement btnCreateUser(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//BUTTON[@dropdowntoggle='']" ) );
        return element;
    }

    // Create Person User
    public static WebElement btnCreatePersonUser(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//A[@class='dropdown-item'][text()='New person']" ) );
        return element;
    }


    // Business Id - Drop Down
    public static WebElement selBusinessId(WebDriver driver){
        //element = driver.findElement ( By.xpath ("id('user-form')/rdg-sf-select//div/select" ));
        element = driver.findElement ( By.xpath ("//select[@name='businessId']" ));
        return element;
    }

    // Username
    public static WebElement txtPersonUserName(WebDriver driver){
        element = driver.findElement ( By.xpath("//input[@name='id']"));
        return element;
    }

    // First Name
    public static WebElement txtFirstName(WebDriver driver){
        element = driver.findElement ( By.xpath("//input[@name='firstName']"));
        return element;
    }

    // Sur Name
    public static WebElement txtSurName(WebDriver driver){
        element = driver.findElement ( By.xpath("//input[@name='surname']"));
        return element;
    }

    //  Email
    public static WebElement txtEmail(WebDriver driver){
        element = driver.findElement ( By.xpath("//input[@name='emailAddress']"));
        return element;
    }

    //  Phone
    public static WebElement txtPhone(WebDriver driver){
        element = driver.findElement ( By.xpath("//input[@name='phoneNumber']"));
        return element;
    }

    // Save button
    public static WebElement btnSave(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//rdg-sf-button[@text='Save']" ) );
        return element;
    }

    // Create Machine User
    public static WebElement btnCreateMachineUser(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//A[@class='dropdown-item'][text()='New machine']" ) );
        return element;
    }

    // Status
    public static WebElement sdrStatus(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//DIV[@class='slider round']" ) );
        return element;
    }

    // Location
    public static WebElement txtLocation(WebDriver driver){
        element = driver.findElement ( By.xpath ("//input[@name='location']"));
        return element;
    }

    // NLC
    public static WebElement txtNLC(WebDriver driver){
        element = driver.findElement ( By.xpath ("//input[@name='nlc']"));
        return element;
    }

    // M/c Username
    public static WebElement txtmachineUserName(WebDriver driver){
        element = driver.findElement ( By.xpath ("//input[@name='id']"));
        return element;
    }

    //  Password
    public static WebElement txtMachinePassword(WebDriver driver){
        element = driver.findElement ( By.xpath ("//input[@name='password']"));
        return element;
    }

    //  Confirm Password
    public static WebElement txtMachineConfirmPassword(WebDriver driver){
        element = driver.findElement ( By.xpath ("//input[@name='confirmPassword']"));
        return element;
    }

    // Machine Type
    public static WebElement selMachineType(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//select[@name='machineTypeId']" ) );
        return element;
    }

    // Validation Level
    public static WebElement selValidationLevel(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//select[@name='validationLevel']" ) );
        return element;
    }

    // Create Business
    public static WebElement btnCreateBusiness(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//BUTTON[@tabindex='0']" ));
        return element;
    }

    // Business UId
    public static WebElement txtBusinessId(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//input[@name='id']") );
        return element;
    }

    // Business Name
    public static WebElement txtBusinessName(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//input[@name='description']" ) );
        return element;
    }

    // Business Uid
    public static WebElement txtBusinessUid(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//input[@name='businessUid']" ) );
        return element;
    }


    // Business table Header
    public static WebElement hdrBusiness(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//div[@class='ag-header-cell-sorted-asc']//span[.='Business']" ) );
        return element;
    }

    // Roles Group
    public static List<WebElement> grpRoles(WebDriver driver){
        elementList = driver.findElements ( By.xpath ( "//LI[@class='list-group-item ng-star-inserted']" ) );
        return elementList;
    }

    // Error Message
    public static WebElement msgErrorMessage(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//SPAN[@class='message']" ) );
        return element;
    }
}
