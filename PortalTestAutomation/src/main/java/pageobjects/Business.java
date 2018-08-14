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
public class Business {

    private static WebElement element = null;
    private static List<WebElement> elementList = null;

    // Edit button
    public static WebElement btnEdit(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "//rdg-sf-button[@text='Edit']"  ) );
        return element;
    }

    // Quick Search Text
    public static WebElement txtQuickSearch(WebDriver driver) {
        element = driver.findElement ( By.id("quickSearch") );
        return element;
    }

    // Search Button
    public static WebElement btnSearch(WebDriver driver) {
        element = driver.findElement ( By.xpath("//button[@class='btn undefined']") );
        return element;
    }

    // Search Button
    public static WebElement grcSearchElement(WebDriver driver) {
        element = driver.findElement ( By.xpath("(//div[@class='ag-cell ag-cell-not-inline-editing ag-cell-with-height ag-cell-no-focus ag-cell-value'])[2]") );
        return element;
    }

    // Cancel Button
    public static WebElement btnCancel(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "(//BUTTON[@tabindex='0'])[1]" ) );
        return element;
    }

    // Business Name Text box
    public static WebElement txtBusiness(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "//INPUT[@autocomplete='off']" ) );
        return element;
    }

    // Save button
    public static WebElement btnSave(WebDriver driver){
        element = driver.findElement ( By.xpath ( "//rdg-sf-button[@text='Save']" ) );
        return element;
    }

    // Close Button
    public static WebElement btnClose(WebDriver driver) {
        element = driver.findElement ( By.xpath ( "//rdg-sf-button[@text='Close']" ) );
        return element;
    }
}
