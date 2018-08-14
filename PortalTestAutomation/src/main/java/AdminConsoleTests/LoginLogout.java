package AdminConsoleTests;

import Utilities.ModuleBase;
import Utilities.ReusableMethods;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import static Utilities.ExceptionHandler.HandleExceptions;

/**
 * Created by kalaiyak on 02/12/2017.
 * Copyrights : KCOM
 */
public class LoginLogout extends ModuleBase{

    // region declarations
    private static WebDriver driver;
    private static HSSFRow row;
    private static HSSFWorkbook wb;
    private static String sheetName;

    // endregion

    /*
    Main Function
     */
    @Test
    public void LoginLogoutTest() throws Exception{
        try {

            // Read Data from Excel file
            InputStream ExcelFileToRead = new FileInputStream (GetDataInput ());
            wb = new HSSFWorkbook(ExcelFileToRead);

            // Mention the name of the Excel Spreadsheet
                // NOTE: Please create a new spreadsheet, if the function is not already available
            String sheetName = "CreatePerson";
            HSSFSheet sheet = wb.getSheet (sheetName);

            Iterator rows = sheet.rowIterator();
            rows.next();
            while (rows.hasNext()) {
                row = (HSSFRow) rows.next ();
                // Add as many variables needed - as in the spreadsheet
                String userName = row.getCell ( 0 ).getStringCellValue ();
                String password = row.getCell ( 1 ).getStringCellValue ();

                // Initialize Test First
                driver = TestInitialize ( driver, "Login - Logout Test Scenario" );

                // Login in to the admin console
                ReusableMethods.Login ( userName, password, driver );


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
}
