package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by kalaiyak on 05/12/2017.
 * Copyrights : KCOM
 */
public class PersonUser  {
    private static WebElement element = null;
    private static List<WebElement> elementList = null;

    // User Name
    public static WebElement txtUserName(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "(//INPUT[@autocomplete='off'])[1]" ) );
        return element;
    }

    // First Name
    public static WebElement txtFirstName(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "(//INPUT[@autocomplete='off'])[2]" ) );
        return element;
    }

    // Sur Name
    public static WebElement txtSurName(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "(//INPUT[@autocomplete='off'])[3]" ) );
        return element;
    }

    // Email
    public static WebElement txtEmail(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "(//INPUT[@autocomplete='off'])[4]" ) );
        return element;
    }

    // Telephone
    public static WebElement txtPhone(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "(//INPUT[@autocomplete='off'])[5]" ) );
        return element;
    }

    // Save Button
    public static WebElement btnSave(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "(//button)[1]" ) );
        return element;
    }

    // Close Button
    public static WebElement btnClose(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "(//button)[2]" ) );
        return element;
    }

    // Application Tile labels
    public static List<WebElement> lblApplications(WebDriver driver){
        elementList = driver.findElements ( By.xpath ( "//div[@class='tile-title']/label") );
        return elementList;
    }

    // Status Label
    public static WebElement lblStatus(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//LABEL[@class='status-label']" ));
        return element;
    }

    // Search Element
    public static WebElement cllSearchElement(WebDriver driver){
        element = driver.findElement ( By.xpath ( "(//div[@col-id='id'])[2]" ));
        return element;
    }
}