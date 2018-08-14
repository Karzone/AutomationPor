package AdminConsoleTests;

import Utilities.Constants;
import Utilities.ModuleBase;
import Utilities.ReusableMethods;
import com.relevantcodes.extentreports.LogStatus;
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
public class DeactivatePersonUser extends ModuleBase{

    // region declarations
    private static WebDriver driver;
    private static HSSFRow row;
    private static HSSFWorkbook wb;
    private static String sheetName;

    // endregion

    /*
    Main Function - To Deactivate the Person user
     */
    @Test
    public void UserDeactivationTest() throws Exception{
        try {
            // Read Data from Excel file
            InputStream ExcelFileToRead = new FileInputStream (GetDataInput ());
            wb = new HSSFWorkbook(ExcelFileToRead);

            // Mention the name of the Excel Spreadsheet
                // NOTE: Please create a new spreadsheet, if the function is not already available
            sheetName = "SearchPerson";
            HSSFSheet sheet = wb.getSheet (sheetName);

            Iterator rows = sheet.rowIterator();
            rows.next();
            while (rows.hasNext()) {
                row = (HSSFRow) rows.next ();
                // Add as many variables needed - as in the spreadsheet
                String userName = row.getCell ( 0 ).getStringCellValue ();
                String password = row.getCell ( 1 ).getStringCellValue ();
                String business = row.getCell ( 2 ).getStringCellValue ();
                String userId = row.getCell ( 3 ).getStringCellValue ();
                String Person_pw = row.getCell ( 4 ).getStringCellValue ();

                // Initialize Test First
                driver = TestInitialize ( driver, userName + " - Deactivate/Activate Person User" );

                // Login in to the admin console
                ReusableMethods.Login ( userName, password, driver );

                // Search the Person user
                ReusableMethods.SearchPersonUser ( driver, userId, business);

                // Deactivate the Person user
                ReusableMethods.DeactivatePerson ( driver, true, userId, "INACTIVE" );

                // Logout of the console
                ReusableMethods.Logout ( driver );

                // Login as the deactivated person user
                Repo.lnkReturnLogin ( driver ).click ();

                // Verify if the account is disabled
                VerifyAccount(userId, Person_pw);

                // Login as Admin user to activate the account
                ReusableMethods.Login ( userName, password, driver );

                // Search the Person user
                ReusableMethods.SearchPersonUser ( driver, userId, business);

                // Deactivate the Person user
                ReusableMethods.DeactivatePerson ( driver, true, userId, "ACTIVE" );

                // Logout of the console
                ReusableMethods.Logout ( driver );

                // Login as the deactivated person user
                Repo.lnkReturnLogin ( driver ).click ();
                ReusableMethods.Login ( userId, Person_pw, driver );

                // Logout of the console
                ReusableMethods.Logout ( driver );

                // quit the driver for the first iteration
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
     * Verify the Account
     * @param userId User Id
     * @throws Exception Generic Exception
     */
    public static void VerifyAccount(String userId, String Password) throws Exception{
        try{
            ReplaceText ( Repo.txtUserName ( driver ), userId ); // User Name
            ReplaceText( Repo.txtPassword ( driver ), ( Password )); // Password

            Repo.btnLogIn ( driver ).click (); // Login Button
            logger.log ( LogStatus.INFO, "Click on Login button." );

                String msg = Repo.msgErrorMessage ( driver ).getText ();
                if(msg.equals ( Constants.USER_NOT_ACTIVE )){
                    logger.log ( LogStatus.INFO, "The User Account is deactivated." );
                }
        }catch(Exception exception){
            throw exception;
        }

    }
}
