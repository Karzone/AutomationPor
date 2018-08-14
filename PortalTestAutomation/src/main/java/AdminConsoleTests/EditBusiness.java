package AdminConsoleTests;

import Utilities.ModuleBase;
import Utilities.ReusableMethods;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import pageobjects.Business;
import pageobjects.MachineUser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import static Utilities.ExceptionHandler.HandleExceptions;

/**
 * Created by kalaiyak on 02/12/2017.
 * Copyrights : KCOM
 */
public class EditBusiness extends ModuleBase{

    // region Declarations
    private static WebDriver driver;
    private static HSSFRow row;
    private static HSSFWorkbook wb;
    private static String sheetName;
    // endregion

    /**
     * Main Function
     * @param args Arguments
     * @throws Exception exception
     */
    @Test
    public void EditBusinessTest() throws Exception{
        try {
            // Read Data from Excel file
            InputStream ExcelFileToRead = new FileInputStream (GetDataInput ());
            wb = new HSSFWorkbook(ExcelFileToRead);

            // Mention the Sheet Index
            sheetName = "EditBusiness";
            HSSFSheet sheet = wb.getSheet (sheetName);

            Iterator rows = sheet.rowIterator();
            rows.next();
            while (rows.hasNext()) {
                row = (HSSFRow) rows.next ();

                // Read Every Column of the Data Row
                String userName = row.getCell ( 0 ).getStringCellValue ();
                String password = row.getCell ( 1 ).getStringCellValue ();
                String businessId = row.getCell ( 2 ).getStringCellValue ();
                String businessName = row.getCell ( 3 ).getStringCellValue ();
                String newBusinessName = row.getCell ( 4 ).getStringCellValue ();

                // Initialize Test First
                driver = TestInitialize ( driver, "[" + userName + "] - Edit Business" );

                // 1. Login into the Admin Console
                ReusableMethods.Login(userName, password, driver);

                // 2. Navigate to Search Business
                ReusableMethods.SelectBusinessesMenu ( driver );

                // 3. Search the saved business
                ReusableMethods.FindElementInGrid ( driver, businessName );

                // 4. Amend Business Name
                EditBusinessName(newBusinessName);

                // 5. Logout
                ReusableMethods.Logout(driver);

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
     * Modify the Business Name
     * @param businessName New Business name
     * @throws Exception Generic Exception
     */
    public static void EditBusinessName(String businessName) throws Exception{
        try{
            MachineUser.btnEdit ( driver ).click ();
            logger.log ( LogStatus.INFO, "Navigating to Edit User Page"  );
            Business.txtBusiness ( driver ).clear ();
            Business.txtBusiness ( driver ).sendKeys ( businessName );
            Business.btnSave ( driver ).click ();
            logger.log ( LogStatus.INFO, "The Business Name changed to [" + businessName + "]."  );
        }catch(Exception exception){
            throw exception;
        }
    }
}
