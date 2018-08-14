package AdminConsoleTests;

import Utilities.Constants;
import Utilities.ModuleBase;
import Utilities.ReusableMethods;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import pageobjects.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Random;

import static Utilities.ExceptionHandler.HandleExceptions;
import static Utilities.ReusableMethods.ResetPasswordThroEmailLink;

/**
 * Reset Password for the locked account using forgot Password option
 * Created by kalaiyak on 02/12/2017.
 * Copyrights : KCOM
 */
// FIXME: 10/08/2018 Waiting for POR-3264 to be resolved, the password from the HTML Version of the email is not readable.
public class LockedAccountForgotPassword extends ModuleBase{

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
    Main Function - Reset Password for the locked person user account
     */
    @Test
    public void UnlockAccountUsingForgotPasswordTest() throws Exception{
        try {
            // Read Data from Excel file
            InputStream ExcelFileToRead = new FileInputStream (GetDataInput ());
            wb = new HSSFWorkbook(ExcelFileToRead);

            // Mention the Sheet Name
            sheetName = "LockedAccount";
            HSSFSheet sheet = wb.getSheet (sheetName);
            Random random = new Random();

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
                driver = TestInitialize ( driver, "["+ userName + "] - Lock Account & Reset Pw using Forgot Pw" );

                // 1. Attempt to Lock the user account
                //LoginAttempts(userId, driver);

                // 2. Forgot Password
                ReusableMethods.ForgotPassword ( userId, driver );

                // 3. Click Reset Password link
                ResetPasswordThroEmailLink(driver);

                // 4. First Time login after password reset
                ReusableMethods.FirstTimeLogin ( userId, genPassword, Constants.CREATE_PERSON_STANDARD_PASSWORD + random.nextInt(100) + 1 , driver );

                // 5. Logout again as a new user
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
     * Attempt to Login multiple times and capture the error message
     * @param UserName Name of the Login user
     * @param driver WebDriver
     * @throws Exception Generic Exception
     */
    public static void LoginAttempts(String UserName, WebDriver driver) throws Exception{
        try {
            String msg = Constants.EMPTY;
            for(int i = 1; i <=5; i ++)
            {
                ReplaceText ( Repo.txtUserName ( driver ), UserName );
                ReplaceText ( Repo.txtPassword ( driver ), Constants.PASS );
                Repo.btnLogIn ( driver ).click ();
                msg = Repo.msgErrorMessage ( driver ).getText ( );
                logger.log ( LogStatus.INFO, "Login Attempt - " + i + " [Error Message : " + msg + "]");
            }
            if(msg.contains ( "Your account is locked" )) {
                logger.log ( LogStatus.INFO, "The User Account is Locked" + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );
            }
            else
                logger.log ( LogStatus.FAIL, "The User Account is NOT locked on 4 (or) more atttempts"
                        + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );

        }catch(Exception e) {
            throw e;
        }
    }
}
