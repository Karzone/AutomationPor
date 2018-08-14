package AdminConsoleTests;

import Utilities.ModuleBase;
import Utilities.ReusableMethods;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import pageobjects.Repo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import static Utilities.ExceptionHandler.HandleExceptions;

/**
 * Created by kalaiyak on 02/12/2017.
 * Copyrights : KCOM
 */
public class CreateMachineUser extends ModuleBase{
// region Declarations
    private static WebDriver driver;
    private static HSSFRow row;
    protected static HSSFCell cell;
    private static HSSFWorkbook wb;
    private static String sheetName;

    public static String business;
    public static String status;
    public static String location;
    public static String nlc;
    public static String machineUserName;
    public static String machineUserpassword;
    public static String machineType;
    public static String validationLevel;
    public static String role[] = new String[10];
// endregion
    /*
    Main Function - Create Person user
     */
    @Test
    public void MachineCreationTest() throws Exception{
        try {
            // Read Data from Excel file
            InputStream ExcelFileToRead = new FileInputStream (GetDataInput ());
            wb = new HSSFWorkbook(ExcelFileToRead);

            // Mention the Sheet Index
            sheetName = "CreateMachine";
            HSSFSheet sheet = wb.getSheet (sheetName);

            Iterator rows = sheet.rowIterator();
            rows.next();
            while (rows.hasNext()) {
                row = (HSSFRow) rows.next ();
                // Read Every Column of the Data Row
                String userName = row.getCell ( 0 ).getStringCellValue ();
                String password = row.getCell ( 1 ).getStringCellValue ();
                business = row.getCell ( 2 ).getStringCellValue ();
                status   = row.getCell ( 3 ).getStringCellValue ();
                location = row.getCell ( 4 ).getStringCellValue ();
                nlc = row.getCell ( 5 ).getStringCellValue ();
                machineUserName = row.getCell ( 6 ).getStringCellValue ();
                machineUserpassword = row.getCell ( 7 ).getStringCellValue ();
                machineType = row.getCell ( 8 ).getStringCellValue ();
                validationLevel = row.getCell ( 9 ).getStringCellValue ();
                role[0] = row.getCell ( 10 ).getStringCellValue ();
                role[1] = row.getCell ( 11 ).getStringCellValue ();
                role[2] = row.getCell ( 12 ).getStringCellValue ();
                role[3] = row.getCell ( 13 ).getStringCellValue ();

                // Initialize Test First
                driver = TestInitialize ( driver, "[" + userName+ "] - Create Machine User Workflow" );

                // 1. Login as Machine user
                ReusableMethods.Login(userName, password, driver);

                // 2. Create Machine User
                CreateMachineUser();

                // 3. Assign Roles to the Newly Created Machine user
                ReusableMethods.AssignRolesToUser(driver, false, role);


                // 5. Logout of the console
                ReusableMethods.Logout ( driver );

                // 6. Relogin and deactivate the machine user
                ReusableMethods.Relogin ( driver, userName, password );
                ReusableMethods.SearchMachineUser(driver, machineUserName, business);
                ReusableMethods.DeactivatePerson (driver, false, machineUserName, "INACTIVE"  );

                // 7. Logout of the console
                ReusableMethods.Logout ( driver );

                driver.quit ();
        }

        }catch(Exception exception){
            HandleExceptions(exception, driver, row);
        }
        finally{
            TestCleanup ( driver, sheetName );
        }
    }

    /**
     * Create Machine User
     * @throws Exception Generic Exception
     */
    public static void CreateMachineUser() throws Exception
    {
        try{
            ReusableMethods.SelectUsersMenu ( driver );
            WebDriverWait wait = new WebDriverWait(driver, 15);
            WebElement element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated( By.xpath ("//BUTTON[@dropdowntoggle='']" )));
            element = wait.until(
                    ExpectedConditions.elementToBeClickable ( By.xpath ("//BUTTON[@dropdowntoggle='']" )));
            Repo.btnCreateUser ( driver ).click ();
            Repo.btnCreateMachineUser ( driver ).click ();

            // Business Id
            if(business != "") ReusableMethods.Select( Repo.selBusinessId ( driver ), business );
            if(status.equalsIgnoreCase ( "ACTIVE")) Repo.sdrStatus ( driver).click ();
            Repo.txtLocation ( driver ).sendKeys ( location );
            Repo.txtNLC ( driver ).sendKeys ( nlc );
            Repo.txtmachineUserName ( driver ).sendKeys ( machineUserName );
            Repo.txtMachinePassword ( driver ).sendKeys ( machineUserpassword );
            Repo.txtMachineConfirmPassword ( driver ).sendKeys ( machineUserpassword );
            ReusableMethods.Select ( Repo.selMachineType (driver), machineType );
            ReusableMethods.Select(Repo.selValidationLevel ( driver ), validationLevel );
            Repo.btnSave ( driver ).click ();
            logger.log ( LogStatus.PASS, "The Machine User [" + machineUserName + "] Created Successfully : " + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );

        }catch(Exception exception){
            throw exception;
        }
    }
}
