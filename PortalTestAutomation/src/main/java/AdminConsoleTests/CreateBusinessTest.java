package AdminConsoleTests;

import Utilities.Constants;
import Utilities.ModuleBase;
import Utilities.ReusableMethods;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import pageobjects.Repo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import static Utilities.ExceptionHandler.HandleExceptions;
import static Utilities.ReusableMethods.ReturnToLoginPage;

/**
 * Created by kalaiyak on 02/12/2017.
 * Copyrights : KCOM
 */
public class CreateBusinessTest extends ModuleBase{

    // region Declarations
    private static WebDriver driver;
    private static HSSFRow row;
    protected static HSSFCell cell;
    private static HSSFWorkbook wb;
    public static String businessName;
    public static String role[] = new String[10];
    public static String GUUsername;
    public static String GUPassword;
    public static String Status;
    public static String Location;
    public static String NLC;
    public static String McUserName;
    public static String McPassword;
    public static String MachineType;
    public static String VLevel;
    public static String MachineRoles;
    public static String Mroles[], Proles[];
    public static String PUsername;
    public static String FirstName;
    public static String SurName;
    public static String Email;
    public static String Phone;
    public static String PersonRoles;
    public static String App[] = new String[10];
    public static String genPassword;

    private static String sheetName;
    // endregion

    /**
     * Create Business Workflow
     * @throws Exception exception
     */
    @Test
    public void SanityTest() throws Exception{
        try {

            // Read Data from Excel file
            InputStream ExcelFileToRead = new FileInputStream(GetDataInput ());
            wb = new HSSFWorkbook(ExcelFileToRead);

            // Mention the Sheet Index
            sheetName = "CreateBusiness";
            HSSFSheet sheet = wb.getSheet (sheetName);

            Iterator rows = sheet.rowIterator();
            rows.next();
            while (rows.hasNext()) {
                row = (HSSFRow) rows.next ();
                // region getData
                // Read Every Column of the Data Row
                String userName = row.getCell ( 0 ).getStringCellValue ();
                String password = row.getCell ( 1 ).getStringCellValue ();
                String businessId = row.getCell ( 2 ).getStringCellValue ();
                businessName = row.getCell ( 3 ).getStringCellValue ();
                String businessUid = row.getCell ( 4 ).getStringCellValue ();
                role[0] = row.getCell ( 5 ).getStringCellValue ();
                role[1] = row.getCell ( 6 ).getStringCellValue ();
                role[2] = row.getCell ( 7 ).getStringCellValue ();
                GUUsername = row.getCell ( 8 ).getStringCellValue ();
                GUPassword = row.getCell ( 9 ).getStringCellValue ();
                Status = row.getCell ( 10 ).getStringCellValue ();
                Location = row.getCell ( 11 ).getStringCellValue ();
                NLC = row.getCell ( 12 ).getStringCellValue ();
                McUserName = row.getCell ( 13 ).getStringCellValue ();
                McPassword = row.getCell ( 14 ).getStringCellValue ();
                MachineType = row.getCell ( 15 ).getStringCellValue ();
                VLevel = row.getCell ( 16 ).getStringCellValue ();
                MachineRoles = row.getCell ( 17 ).getStringCellValue ();
                Mroles = MachineRoles.split ( ";" );
                PUsername = row.getCell ( 18 ).getStringCellValue ();
                FirstName = row.getCell ( 19 ).getStringCellValue ();
                SurName = row.getCell ( 20 ).getStringCellValue ();
                Email = row.getCell ( 21 ).getStringCellValue ();
                Phone = row.getCell ( 22 ).getStringCellValue ();
                PersonRoles = row.getCell ( 23 ).getStringCellValue ();
                Proles = PersonRoles.split ( ";" );
                App[0] = row.getCell ( 24 ).getStringCellValue ();
                App[1] = row.getCell ( 25 ).getStringCellValue ();
                // endregion

                // Initialize Test First
                driver = TestInitialize ( driver, "[" + userName + "] - Create Business Workflow");

                // 1. Login into the Admin Console
                ReusableMethods.Login(userName, password, driver);

                // 2. Create Business
                ReusableMethods.SelectBusinessesMenu ( driver );
                CreateBusiness ( businessId, businessName, businessUid );

                // 3. Assign Application Role(s) to the Business
                ReusableMethods.AssignApplicationsToBusiness (driver, role);

                // 4. Search the saved business
                ReusableMethods.SelectBusinessesMenu ( driver );
                ReusableMethods.FindBusiness (driver, businessName );

                // 5. Logout
                ReusableMethods.Logout(driver);

                // 6. Login as GlobalUser admin to create users
                ReturnToLoginPage(driver);
                ReusableMethods.Login ( GUUsername, GUPassword, driver );

                // 7. Create Machine for the new Business
                CreateMachineForNewBusiness ();

                // 8. Verify and Assign the Roles to the Machine user
                ReusableMethods.VerifyAndAssignRolesToUser(driver, false, Mroles, MachineRoles);

                // 9. Create New Person  user for the new business
                CreatePersonForNewBusiness();

                // 10. Verify and Assign the Roles to the Person User
                ReusableMethods.VerifyAndAssignRolesToUser(driver, true, Proles, PersonRoles);

                // 11. Logout of the console
                ReusableMethods.Logout ( driver );

                // 12. Read Email notification to capture the auto-generated password for the newly created user
                genPassword = ReusableMethods.ReadPasswordFromEmail();

                // 13. Re-login as the newly created person user

                ReturnToLoginPage(driver);
                Thread.sleep ( 5000 );
                ReusableMethods.FirstTimeLogin ( PUsername, genPassword, Constants.CREATE_PERSON_STANDARD_PASSWORD,driver );

                // 14. Verify & Navigate to the application accesses for the user
                ReusableMethods.VerifyApplications(App, driver);

                // 15. Logout again as a new user
                ReusableMethods.Logout ( driver, PUsername );

                // 16. Re-login as admin user to deactivate the account
                ReusableMethods.Relogin ( driver, GUUsername, GUPassword );

                // 17. Deactivate person user account
                ReusableMethods.SearchPersonUser ( driver, PUsername, businessName );
                ReusableMethods.DeactivatePerson ( driver, true, PUsername, "INACTIVE" );

                // 18. Deactivate machine user account
                ReusableMethods.SearchMachineUser(driver, McUserName, businessName);
                ReusableMethods.DeactivatePerson ( driver, false, McUserName, "INACTIVE" );

                // 19. Logout again as a new user
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

     /*
     * Create a Business
     * @param businessId Id
     * @param businessName Business Name
     * @param businessUid Business Uid
     * @throws Exception exception
     */
    public static void CreateBusiness(String businessId, String businessName, String businessUid) throws Exception{
     try{
         // Page Refresh - Work Around
         // driver.navigate().refresh();
         //((JavascriptExecutor) driver).executeScript("return window.stop");
         Thread.sleep(3000);
         Repo.btnCreateBusiness ( driver ).click ();
         WebDriverWait wait = new WebDriverWait(driver, 15);
         WebElement element = wait.until(
                 ExpectedConditions.visibilityOfElementLocated( By.xpath ("//div[@class='col-sm-6']/rdg-sf-string[1]/div/div/div/div/input" )));
         element = wait.until(
                 ExpectedConditions.elementToBeClickable ( By.xpath ("//div[@class='col-sm-6']/rdg-sf-string[1]/div/div/div/div/input" )));
         logger.log ( LogStatus.INFO, "Click Create Business button." );

         Repo.txtBusinessId (  driver).sendKeys(businessId );
         Repo.txtBusinessName ( driver ).sendKeys(businessName);
         Repo.txtBusinessUid ( driver ).sendKeys(businessUid );
         Repo.btnSave ( driver ).click();
         Thread.sleep(3000);
         logger.log ( LogStatus.INFO, "The Business [" + businessName + "] Created Successfully." );

     }catch(Exception e){
         throw e;
     }
    }

    /**
     * Create Machine User for the newly created business
     * @throws Exception Generic Exception
     */
    public static void CreateMachineForNewBusiness() throws Exception
    {
        try{
            ReusableMethods.SelectUsersMenu ( driver );

            Repo.btnCreateUser ( driver ).click ();
            Repo.btnCreateMachineUser ( driver ).click ();

            // Business Id
            if(businessName != "") ReusableMethods.Select( Repo.selBusinessId ( driver ), businessName );
            if(Status.equalsIgnoreCase ( "ACTIVE")) Repo.sdrStatus ( driver).click ();
            Repo.txtLocation ( driver ).sendKeys(Location );
            Repo.txtNLC ( driver ).sendKeys(NLC);
            Repo.txtmachineUserName ( driver ).sendKeys( McUserName);
            Repo.txtMachinePassword ( driver ).sendKeys( McPassword);
            Repo.txtMachineConfirmPassword ( driver ).sendKeys(McPassword );
            ReusableMethods.Select ( Repo.selMachineType (driver), MachineType );
            ReusableMethods.Select(Repo.selValidationLevel ( driver ), VLevel );
            Repo.btnSave ( driver ).click ();
            logger.log ( LogStatus.PASS, "The Machine User [" + McUserName + "] Created Successfully : " + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );

        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Create Person User for the newly created business
     * @throws Exception
     */
    public static void CreatePersonForNewBusiness() throws Exception
    {
        try{
            ReusableMethods.SelectUsersMenu ( driver );
            Thread.sleep ( 2000 );
            Repo.btnCreateUser ( driver ).click ();
            Repo.btnCreatePersonUser ( driver ).click ();
            WebDriverWait wait = new WebDriverWait(driver, 10);

            // Business Id
            if(businessName != "") ReusableMethods.Select( Repo.selBusinessId ( driver ), businessName );
            Repo.txtPersonUserName ( driver).sendKeys(PUsername );
            Repo.txtFirstName ( driver ).sendKeys(FirstName );
            Repo.txtSurName ( driver ).sendKeys(SurName );
            Repo.txtEmail ( driver ).sendKeys(Email );
            Repo.txtPhone ( driver ).sendKeys(Phone);
            Repo.btnSave ( driver ).click ();
            logger.log ( LogStatus.PASS, "Person User [" +PUsername + "] Created Successfully : " + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );

        }catch(Exception exception){
            throw exception;
        }
    }
}
