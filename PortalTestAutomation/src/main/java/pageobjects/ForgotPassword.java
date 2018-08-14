package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by kalaiyak on 05/12/2017.
 * Copyrights : KCOM
 */
public class ForgotPassword {
    private static WebElement element = null;
    private static List<WebElement> elementList = null;

    // New Password
    public static WebElement txtPassword(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "//INPUT[@id='input-password']" ) );
        return element;
    }

    // Confirm Password
    public static WebElement txtConfirmPassword(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "//INPUT[@id='input-confirmPassword']" ) );
        return element;
    }

    public static WebElement altPasswordReset(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//div[contains(text(), 'Your password had been successfully reset!')]" ) );
        return element;
    }
}