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
import pageobjects.MachineUser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import static Utilities.ExceptionHandler.HandleExceptions;

/**
 * Created by kalaiyak on 02/12/2017.
 * Copyrights : KCOM
 */
public class EditMachineUser extends ModuleBase{
// region Declarations
    private static WebDriver driver;
    private static HSSFRow row;
    protected static HSSFCell cell;
    private static HSSFWorkbook wb;
    private static String sheetName;

    public static String status;
    public static String machineUserName;
    public static String business;
    public static String location;
    public static String nlc;
    public static String machineType;
    public static String validationLevel;
    public static String role[] = new String[10];
    public static boolean executed = false;
// endregion
    /*
    Main Function - Edit Machine User
     */
    @Test
    public void EditMachineTest() throws Exception{
        try {
            // Read Data from Excel file
            InputStream ExcelFileToRead = new FileInputStream (GetDataInput ());
            wb = new HSSFWorkbook(ExcelFileToRead);

            // Mention the Sheet Index
            sheetName = "EditMachine";
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
                machineUserName = row.getCell ( 4 ).getStringCellValue ();
                location = row.getCell ( 5 ).getStringCellValue ();
                nlc = row.getCell ( 6 ).getStringCellValue ();
                machineType = row.getCell ( 7 ).getStringCellValue ();
                validationLevel = row.getCell ( 8 ).getStringCellValue ();
                role[0] = row.getCell ( 9 ).getStringCellValue ();
                role[1] = row.getCell ( 10 ).getStringCellValue ();

                // Initialize Test First
                if(!executed) {
                    driver = TestInitialize ( driver, "[" + userName+ "] -  Edit Machine User" );
                    ReusableMethods.Login(userName, password, driver);
                    executed = true;
                }

                // 2. Search the existing machine user
                ReusableMethods.SearchMachineUser(driver, machineUserName, business);

                // 3. Edit the Machine User & Generate Tokens
                EditMachine ();

                // 4. Logout
                if(!rows.hasNext()) {
                    ReusableMethods.Logout ( driver );
                }
        }

        }catch(Exception exception){
            HandleExceptions(exception, driver, row);
        }
        finally{
            TestCleanup ( driver, sheetName );
        }
    }

    /**
     * Edit Machine User
     * @throws Exception Generic Exception
     */
    public static void EditMachine() throws Exception {
        try {
            MachineUser.btnEdit ( driver ).click ();
            logger.log ( LogStatus.INFO, "Navigating to Edit User Page" );

            ReusableMethods.AssignRolesToUser(driver, false, role);

            /*if(!MachineUser.lblStatus ( driver ).getText ().equalsIgnoreCase ( status ))
                MachineUser.slrStatus ( driver ).click ();

            ReplaceText ( MachineUser.txtLocation ( driver ), location );
            ReplaceText ( MachineUser.txtNLC ( driver ), nlc );
            ReusableMethods.Select ( MachineUser.selMachineType ( driver ), machineType );
            ReusableMethods.Select ( MachineUser.selValidationLevel ( driver ), validationLevel );

            // Scroll to the bottom of the page
            //JavascriptExecutor jse = (JavascriptExecutor)driver;
            //jse.executeScript("window.scrollBy(0,250)", "");
            GenerateToken ();*/

            logger.log ( LogStatus.INFO, "The Status/Location/NLC/Machine Type/Validation Level of the Machine User [" + machineUserName + "] has been updated." );

        } catch (Exception exception) {
            throw exception;
        }
    }

    /**
     * Generate Token for the machine user
     * @throws Exception Generic Exception
     */
    public static void GenerateToken() throws Exception {
        MachineUser.btnGenerateToken ( driver ).click ();
        do{
            Thread.sleep ( 500 );
        }while(MachineUser.lblToken ( driver ).getText ().equals ( Constants.GENERATE_TOKEN ));
        if(MachineUser.lblToken ( driver ).getText ().equals ( Constants.GENERATE_TOKEN )) {
            logger.log ( LogStatus.FAIL, "The Tokens are not generated for the Machine User." );
        }
        else {
            logger.log ( LogStatus.PASS, "The Token [" + MachineUser.lblToken ( driver ).getText ()
                    +"] generated for the Machine User - [" +machineUserName+ "]." + logger.addScreenCapture ( CaptureScreenshot ( driver ) ));
        }
    }
}
