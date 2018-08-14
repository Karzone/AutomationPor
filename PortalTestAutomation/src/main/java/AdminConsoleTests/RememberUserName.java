package AdminConsoleTests;

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

import static Utilities.ExceptionHandler.HandleExceptions;

/**
 * Created by kalaiyak on 02/12/2017.
 * Copyrights : KCOM
 */
public class RememberUserName extends ModuleBase{

    // region Declarations
    private static WebDriver driver;
    private static HSSFRow row;
    protected static HSSFCell cell;
    private static HSSFWorkbook wb;
    private static String sheetName;

    // endregion
    /*
    Main Function - Remember the username in the Login page
     */
    @Test
    public void RememberUserNameTest() throws Exception{
        try {
            // Read Data from Excel file
            InputStream ExcelFileToRead = new FileInputStream (GetDataInput ());
            wb = new HSSFWorkbook(ExcelFileToRead);

            // Mention the Sheet Name
            sheetName = "LockedAccount";
            HSSFSheet sheet = wb.getSheet (sheetName);

            Iterator rows = sheet.rowIterator();
            rows.next();
            while (rows.hasNext()) {
                row = (HSSFRow) rows.next ();
                // Read Every Column of the Data Row
                String userName = row.getCell ( 0 ).getStringCellValue ();
                String password = row.getCell ( 1 ).getStringCellValue ();

                // Initialize Test First
                driver = TestInitialize ( driver, "["+ userName + "] - Remember Username" );

                // 1. Login into the Admin Console
                ReusableMethods.LoginAndRemember (userName, password, driver);

                // 2. Logout of the console
                ReusableMethods.Logout ( driver );

                // 3. Verify the Remember me option
                Repo.lnkReturnLogin ( driver ).click ();
                VerifyRememberMe ( userName );
                ReusableMethods.Login ( userName, password, driver );

                // 4. Logout again as a new user
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
     * Verify the Remember me option in the login page
     * @param userName Name of the login user
     * @throws Exception Generic Exception
     */
    public static void VerifyRememberMe(String userName) throws Exception{
        try{
                if(Repo.chkRememberMe ( driver ).isSelected ()){
                    if(Repo.txtUserName ( driver ).getAttribute ( "value" ).toUpperCase ().equals ( userName.toUpperCase ())) {
                        logger.log(LogStatus.PASS, "The username [" + userName + "] is retained in the login page." + logger.addScreenCapture(CaptureScreenshot(driver)));
                        Repo.chkRememberMe(driver).click();
                    }
            }
            else
                logger.log ( LogStatus.FAIL, "The username [" + userName + "] is NOT retained in the login page." );
        }catch(Exception exception){
            throw exception;
        }
    }
}
