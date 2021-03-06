package AdminConsoleTests;

import Utilities.Constants;
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
public class LoadPersonUsers extends ModuleBase{

    // region Declarations
    private static WebDriver driver;
    private static HSSFRow row;
    protected static HSSFCell cell;
    private static HSSFWorkbook wb;
    private static String sheetName;

    public static String business;
    public static String userId;
    public static String firstName;
    public static String surName;
    public static String email;
    public static String phone;
    public static String role[] = new String[10];
    public static String app[] = new String[3];
    public static String genPassword = "";
    public static boolean executed = false;
    // endregion
    /*
    Main Function - Create Person user
     */
    @Test
    public void LoadPersonUsersTest() throws Exception{
        try {

            // Read Data from Excel file
            InputStream ExcelFileToRead = new FileInputStream (GetDataInput ());
            wb = new HSSFWorkbook(ExcelFileToRead);

            // Mention the Sheet Index
            sheetName = "CreatePersonLoad";
            HSSFSheet sheet = wb.getSheet (sheetName);

            Iterator rows = sheet.rowIterator();
            rows.next();
            while (rows.hasNext()) {
                row = (HSSFRow) rows.next ();
                // Read Every Column of the Data Row
                String userName = row.getCell ( 0 ).getStringCellValue ();
                String password = row.getCell ( 1 ).getStringCellValue ();
                business = row.getCell ( 2 ).getStringCellValue ();
                userId = row.getCell ( 3 ).getStringCellValue ();
                firstName = row.getCell ( 4 ).getStringCellValue ();
                surName = row.getCell ( 5 ).getStringCellValue ();
                email = row.getCell ( 6 ).getStringCellValue ();
                phone = row.getCell ( 7 ).getStringCellValue ();
                role[0] = row.getCell ( 8 ).getStringCellValue ();
                role[1] = row.getCell ( 9 ).getStringCellValue ();
                role[2] = row.getCell ( 10 ).getStringCellValue ();
                role[3] = row.getCell ( 11 ).getStringCellValue ();
                app[0] = row.getCell ( 12 ).getStringCellValue ();
                app[1] = row.getCell ( 13 ).getStringCellValue ();
                app[2] = row.getCell ( 14 ).getStringCellValue ();

                // 1. Initialize Test First
                if(!executed) {
                    driver = TestInitialize ( driver, "["+ userName + "] - Create Person User Workflow" );
                    ReusableMethods.Login(userName, password, driver);
                    executed = true;
                }

                // 2. Create New Person User
                CreatePersonUser();

                // 3. Assign Roles to the newly created user
                ReusableMethods.AssignRolesToUser(driver, true, role);

                // 4. Logout
                ReusableMethods.Logout ( driver );

                // 6. Read Email notification to capture the auto-generated password for the newly created user
                genPassword = ReusableMethods.ReadPasswordFromEmail();

                // 7. Re-login as the newly created person user
                Repo.lnkReturnLogin ( driver ).click ();
                ReusableMethods.FirstTimeLogin ( userId, genPassword, Constants.CREATE_PERSON_STANDARD_PASSWORD, driver );

                ReusableMethods.Logout ( driver );
                ReusableMethods.Relogin(driver, userName, password);

                // 8. Logout again as a new user
                if(!rows.hasNext()) {
                    ReusableMethods.Logout ( driver );
                }
            }

        }catch(Exception exception){
            HandleExceptions(exception, driver, row);
        }
        finally{
            // Logout again as a new user
            //ReusableMethods.Logout ( driver );
            TestCleanup ( driver, sheetName );
        }
    }

    /**
     * Create Person User
     *
     */
    public static void CreatePersonUser() throws Exception
    {
        try{
            ReusableMethods.SelectUsersMenu ( driver );
            WebDriverWait wait = new WebDriverWait(driver, 15);
            WebElement element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated( By.xpath ("//BUTTON[@dropdowntoggle='']" )));
            element = wait.until(
                    ExpectedConditions.elementToBeClickable ( By.xpath ("//BUTTON[@dropdowntoggle='']" )));
            Repo.btnCreateUser ( driver ).click ();
            Repo.btnCreatePersonUser ( driver ).click ();

            // Business Id
            if(business != "")ReusableMethods.Select( Repo.selBusinessId ( driver ), business );
            Repo.txtPersonUserName ( driver).sendKeys ( userId );
            Repo.txtFirstName ( driver ).sendKeys ( firstName );
            Repo.txtSurName ( driver ).sendKeys ( surName );
            Repo.txtEmail ( driver ).sendKeys ( email );
            Repo.txtPhone ( driver ).sendKeys ( phone );
            Repo.btnSave ( driver ).click ();
            logger.log ( LogStatus.PASS, "Person User [" +userId + "] Created Successfully : " );

        }catch(Exception exception){
            throw exception;
        }
    }
}
