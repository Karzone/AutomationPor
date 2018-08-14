package AdminConsoleTests;

import Utilities.ModuleBase;
import Utilities.ReusableMethods;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.WebDriver;
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
public class ResetPersonUserPassword extends ModuleBase{

    // region Declarations
    private static WebDriver driver;
    private static HSSFRow row;
    protected static HSSFCell cell;
    private static HSSFWorkbook wb;
    private static String sheetName;

    public static String business;
    public static String userId;
    public static String genPassword = "";

    // endregion
    /*
    Main Function - Reset Person user Password
     */
    @Test
    public void ResetPersonUserPasswordTest() throws Exception{
        try {
            // Read Data from Excel file
            InputStream ExcelFileToRead = new FileInputStream (GetDataInput ());
            wb = new HSSFWorkbook(ExcelFileToRead);

            // Mention the Sheet Name
            sheetName = "ResetPassword";
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

                // Initialize Test First
                driver = TestInitialize ( driver, "["+ userName + "] - Reset Person User Password" );

                // 1. Login into the Admin Console
                ReusableMethods.Login(userName, password, driver);

                // 2. Search the existing person user
                ReusableMethods.SearchPersonUser ( driver, userId, business );

                // 3. Reset Password for the user
                ReusableMethods.ResetPassword (driver);

                // 4. Logout of the console
                ReusableMethods.Logout ( driver );

                // 5. Read Email notification to capture the auto-generated password for the newly created user
                genPassword = ReusableMethods.ReadPasswordFromEmail();

                // 6. Re-login as person user with new auto-generated password
                Repo.lnkReturnLogin ( driver ).click ();
                ReusableMethods.Login ( userId, genPassword, driver );

                // 7. Logout again as a new user
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
}
