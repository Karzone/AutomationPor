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
import pageobjects.MachineUser;
import pageobjects.PersonUser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import static Utilities.ExceptionHandler.HandleExceptions;

/**
 * Created by kalaiyak on 02/12/2017.
 * Copyrights : KCOM
 */
public class EditPersonUser extends ModuleBase{

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

    // endregion
    /*
    Main Function - Edit Person user
     */
    @Test
    public void EditPersonTest() throws Exception{
        try {
            // Read Data from Excel file
            InputStream ExcelFileToRead = new FileInputStream (GetDataInput ());
            wb = new HSSFWorkbook(ExcelFileToRead);

            // Mention the Sheet Index
            sheetName = "EditPerson";
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

                // Initialize Test First
                driver = TestInitialize ( driver, "["+ userName + "] - Edit Person User" );

                // 1. Login into the Admin Console
                ReusableMethods.Login(userName, password, driver);

                // 2. Search the existing person user
                ReusableMethods.SearchPersonUser ( driver, userId, business );

                // 3. Edit Person User
                EditPersonUser ();

                // 5. Logout of the console
                ReusableMethods.Logout ( driver );

                // 6. TODO : Read Email from Outlook to generate password for the newly created user
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
     * Edit Person User
     * @throws Exception Generic Exception
     */
    public static void EditPersonUser() throws Exception{
        try{
            MachineUser.btnEdit ( driver ).click ();
            logger.log ( LogStatus.INFO, "Navigating to Edit User Page"  );

            ReplaceText ( PersonUser.txtUserName ( driver ), firstName);
            ReplaceText ( PersonUser.txtFirstName ( driver ), surName);
            ReplaceText ( PersonUser.txtSurName ( driver ), email);
            ReplaceText ( PersonUser.txtEmail ( driver ), phone);

            logger.log ( LogStatus.INFO, "Edit Person User [" + userId + "] has been updated." + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );
            PersonUser.btnSave ( driver ).click ();
        }catch(Exception exception){
            throw exception;
        }

    }
}
