package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Object Reposity Related to Machine User
 * Created by kalaiyak on 05/12/2017.
 * Copyrights : KCOM
 */
public class MachineUser {

    private static WebElement element = null;
    private static List<WebElement> elementList = null;

    // Business
    public static WebElement Search_By_Business(WebDriver driver){
        element = driver.findElement( By.xpath ("//select[@name='searchBusiness']"));
        return element;
    }

    // User type
    public static WebElement Search_By_UserType(WebDriver driver){
        element = driver.findElement( By.xpath ("//select[@name='usertype']"));
        return element;
    }

    // User Name
    public static WebElement Search_By_UserName(WebDriver driver){
        element = driver.findElement( By.xpath ("//div/rdg-users-search//div/input"));
        return element;
    }

    // Machine Type
    public static WebElement Search_By_MachineType(WebDriver driver){
        element = driver.findElement( By.xpath ("//div[1]/rdg-sf-data-select//div/select"));
        return element;
    }

    // Search button
    public static WebElement btnSearch(WebDriver driver) {
        element = driver.findElement ( By.id ( "searchbtn" ) );
        return element;
    }

    // Search Result - Business Id
    public static WebElement clBusinessId(WebDriver driver){
        element = driver.findElement ( By.xpath ("(//div[@colid='businessId'])[2]")  );
        return element;
    }

    // Drag and Drop Location
    public static WebElement drgDropLocation(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//div[2]/rdg-user-edit-roles/rdg-drag-drop//div[1]/div/ul/li" ) );
        return element;
    }

    // Edit
    public static WebElement btnEdit(WebDriver driver){
        element = driver.findElement ( By.xpath ( "(//rdg-sf-button[@text='Edit'])" ) );
        return element;
    }

    // Edit Machine
    public static WebElement btnEditMachine(WebDriver driver){
        //element = driver.findElement ( By.xpath ("//rdg-sf-button[@icon='pen']") );
        element = driver.findElement ( By.xpath ( "//div/rdg-sf-button[1]/div/button" ) );
        return element;
    }

    // Update Roles Button
    public static WebElement btnUpdateRoles(WebDriver driver){
        element = driver.findElement ( By.xpath ( "(//BUTTON[@tabindex='0'])[4]") );
        return element;
    }

    // Reset Password
    public static WebElement btnResetPassword(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//rdg-sf-button[@text='reset password']") );
        return element;
    }

    // Cancel Button
    public static WebElement btnCancel(WebDriver driver){
        element = driver.findElement ( By.xpath ( "(//BUTTON[@tabindex='0'])[1]") );
        return element;
    }

    // Close Button
    public static WebElement btnClose(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//rdg-sf-button[@text='Close']" ));
        return element;
    }

    // Status Label
    public static WebElement lblStatus(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//LABEL[@class='status-label']") );
        return element;
    }

    // Status Slider
    public static WebElement slrStatus(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//DIV[@class='slider round']") );
        return element;
    }

    // Location
    public static WebElement txtLocation(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "(//INPUT[@autocomplete='off'])[1]" ) );
        return element;
    }

    // NLC
    public static WebElement txtNLC(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "(//INPUT[@autocomplete='off'])[2]" ) );
        return element;
    }

    // User Name
    public static WebElement txtUserName(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "(//INPUT[@autocomplete='off'])[3]" ) );
        return element;
    }

    // Machine Type
    public static WebElement selMachineType(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//rdg-sf-select[1]/div/div[2]/div/select" ) );
        return element;
    }

    // Validation Level
    public static WebElement selValidationLevel(WebDriver driver){
        element = driver.findElement ( By.xpath ( "id('user-form')/ng-component/rdg-sf-select[2]//div[2]/div/select" ) );
        return element;
    }

    // Generate Token
    public static WebElement btnGenerateToken(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "(//BUTTON[@tabindex='0'])[2]" ) );
        return element;
    }

    // Token value
    public static WebElement lblToken(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "//DIV[@class='col-sm-12 token-container']" ) );
        return element;
    }
}