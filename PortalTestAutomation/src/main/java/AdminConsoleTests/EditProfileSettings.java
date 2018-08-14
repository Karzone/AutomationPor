package AdminConsoleTests;

import Utilities.ModuleBase;
import Utilities.ReusableMethods;
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
public class EditProfileSettings extends ModuleBase{

    // region declarations
    private static WebDriver driver;
    private static HSSFRow row;
    private static HSSFWorkbook wb;
    private static String sheetName;

    public static String telephone;
    public static String newPassword;


    // endregion

    /*
    Main Function
     */
    @Test
    public void EditProfileTest() throws Exception{
        try {
            // Read Data from Excel file
            InputStream ExcelFileToRead = new FileInputStream (GetDataInput ());
            wb = new HSSFWorkbook(ExcelFileToRead);

            // Mention the name of the Excel Spreadsheet
            String sheetName = "EditProfile";
            HSSFSheet sheet = wb.getSheet (sheetName);

            Iterator rows = sheet.rowIterator();
            rows.next();
            while (rows.hasNext()) {
                row = (HSSFRow) rows.next ();
                // Add as many variables needed - as in the spreadsheet
                String userName = row.getCell ( 0 ).getStringCellValue ();
                String password = row.getCell ( 1 ).getStringCellValue ();
                telephone = row.getCell ( 2 ).getStringCellValue ();
                newPassword = row.getCell ( 3 ).getStringCellValue ();

                // Initialize Test First
                driver = TestInitialize ( driver, userName + " - Edit User Profile Settings " );

                // Login in to the admin console
                ReusableMethods.Login ( userName, password, driver );

                // Edit Profile Settings
                EditProfile(userName, telephone, password, newPassword);

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
     * Edit Profile - Settings of the user
     * @throws Exception Generic Exception
     */
    public static void EditProfile(String userName, String telephone, String oldPassword, String newPassword) throws Exception{
        try{
            // Navigate to Edit Profile Page
            //driver.findElement ( By.xpath ("//SPAN[@class='menu__title'][contains(text(),'" + userName.toLowerCase () + "')]") ).click ();
            Repo.btnMyProfile (driver).click ();
            Repo.mnuSettings ( driver ).click ();

            // Change Telephone number
            if(!telephone.isEmpty ()){
                ReplaceText ( Repo.txtTelephone ( driver ), telephone);

                // Save the settings
                Repo.btnSaveProfile ( driver ).click ();
            }

            // Change Password
            if(!newPassword.isEmpty ()){
                ReplaceText ( Repo.txtOldPassword ( driver ), oldPassword );
                ReplaceText ( Repo.txtNewPassword ( driver ), newPassword );
                ReplaceText ( Repo.txtRetypePassword ( driver ), newPassword );

                // Save the new password
                Repo.btnUpdateProfilePassword ( driver ).click ();
            }

        }catch(Exception exception){

        }
    }
}
