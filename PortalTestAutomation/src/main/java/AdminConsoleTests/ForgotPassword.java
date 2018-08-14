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
import pageobjects.Repo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Utilities.ExceptionHandler.HandleExceptions;

/**
 * Created by kalaiyak on 02/12/2017.
 * Copyrights : KCOM
 */
public class ForgotPassword extends ModuleBase{

    // region Declarations
    private static WebDriver driver;
    private static HSSFRow row;
    protected static HSSFCell cell;
    private static HSSFWorkbook wb;
    private static String sheetName;

    public static String business;
    public static String userId;
    public static String password;
    public static String email;

    // endregion
    /*
    Main Function - Reset Person using Forgot Password option
     */
    // FIXME: 13/08/2018 - This test cannot be executed until plain text version of the email is configured.
    @Test
    public void ForgotPasswordTest() throws Exception{
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
                password = row.getCell ( 1 ).getStringCellValue ();
                business = row.getCell ( 2 ).getStringCellValue ();
                userId = row.getCell ( 3 ).getStringCellValue ();
                email = row.getCell ( 4 ).getStringCellValue ();

                // Initialize Test First
                driver = TestInitialize ( driver, "["+ userName + "] - Forgot Password" );

                // 1. Forgot Password
                ReusableMethods.ForgotPassword ( userId, driver );

                // 2. Read Email notification to capture the auto-generated password for the newly created user
                ResetPasswordThroEmailLink ();

                // 3. Re-login as person user with new auto-generated password
                ReusableMethods.Login ( userId, Constants.CREATE_PERSON_STANDARD_PASSWORD, driver );

                // 4. Logout
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
     * Reset the Person user password through the link available in the email
     * @throws Exception Generic Exception
     */
    public static void ResetPasswordThroEmailLink() throws Exception{
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
}
