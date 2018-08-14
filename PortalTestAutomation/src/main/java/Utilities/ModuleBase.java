package Utilities;

import com.github.javafaker.Faker;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.sun.mail.imap.IMAPFolder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ModuleBase {

    // region Global Variables
    private static Log log = LogFactory.getFactory().getInstance( ModuleBase.class);
    private static Faker generator = new Faker();

    // Browser Stack Variables
    public static final String USERNAME = "kcomtest1";
    public static final String AUTOMATE_KEY = "Rxeq7tfmpnHkns9Rg8Dq";
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
    // endregion

    // Enable Logger
    protected static ExtentReports reports = new ExtentReports ( System.getProperty ( "user.dir" ) + "/TestReports/AutomationTestReport.html", false );
    public static ExtentTest logger;
    public static String environment;

    protected static WebDriver getDriver(String[] args) {

        if(args.length == 0) {
            System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
            ChromeOptions options = new ChromeOptions ();
            options.addArguments("--start-maximized");
            options.addArguments ( "--disable-extensions" );
            //options.addArguments("--disable-infobars");
            options.setExperimentalOption("useAutomationExtension", false);
            options.setExperimentalOption("excludeSwitches",
                    Collections.singletonList("enable-automation"));
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            options.setExperimentalOption ( "prefs", prefs );
            return new ChromeDriver (options  ); // Default Driver
        }

        if (args[0].startsWith("--")) {

            String[] kv = args[0].split("=");

            if (kv.length < 2) {
                log.info("Driver configuration option \"" + args[0] + "\" is not of the form \"--driver=<name>\"");
                System.exit(1);
            }

            if (args[0].startsWith("--driver")) {
                try {
                    switch (kv[1]) {
                        case "firefox":
                            System.setProperty("webdriver.gecko.driver","resources/geckodriver.exe");
                            FirefoxOptions firefoxOptions = new FirefoxOptions();
                            String firefoxPath = GetProperty("FIREFOX_PATH");
                            firefoxOptions.setBinary(firefoxPath.replace("\\", "\\\\"));
                            firefoxOptions.setCapability("marionette", true);
                            return new FirefoxDriver(firefoxOptions);
                        case "chrome":
                            System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
                            ChromeOptions options = new ChromeOptions ();
                            options.addArguments("--disable-infobars");
                            options.addArguments ( "--disable-extensions" );
                            options.setExperimentalOption("useAutomationExtension", false);
                            options.setExperimentalOption("excludeSwitches",
                                    Collections.singletonList("enable-automation"));
                            Map<String, Object> prefs = new HashMap<String, Object>();
                            prefs.put("credentials_enable_service", false);
                            prefs.put("profile.password_manager_enabled", false);
                            options.setExperimentalOption ( "prefs", prefs );
                            return new ChromeDriver(options);
                        case "ie":
                            System.setProperty("webdriver.ie.driver", "resources/IEDriverServer.exe");
                            DesiredCapabilities cap = new DesiredCapabilities();
                            cap.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
                            cap.setCapability ("requireWindowFocus", true);
                            return new InternetExplorerDriver();
                    }
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }
        }

        return null;
    }

    /**
     * Helper Method to Set all Chrome Options for the Playback
     * @return WebDriver
     * @throws Exception Exception
     */
    protected static WebDriver SetChromeOptions(WebDriver driver) throws Exception{
        try {
            // Non-Admin User Sessions Initializations in Chrome Browser
            System.setProperty ( "webdriver.chrome.driver", "resources/chromedriver.exe" );
            ChromeOptions options = new ChromeOptions ();
            options.addArguments("--start-maximized");
            options.addArguments ( "--disable-extensions" );
            options.addArguments("disable-infobars");
            driver.manage ().timeouts ().implicitlyWait ( 60, TimeUnit.SECONDS );

            return driver;
        }catch(Exception exception){
            throw exception;
        }
    }
    /**
     * Initialize the Test
     * Must be the first statement inside the Main method
     * @param driver WebDriver
     * @param args Represents Browser
     * @param testName Name of the Test
     * @throws Exception Exception
     */
    protected static WebDriver TestInitialize(WebDriver driver, String testName) throws Exception
    {
        try {
            if(GetProperty("IS_REMOTE").equalsIgnoreCase(Constants.NO)){
                String browser = GetProperty("BROWSER_DRIVER");
                String args[] = {"--driver=" +browser};
                driver = getDriver ( args );
                Locale.setDefault(Locale.ENGLISH);
                logger = reports.startTest ( testName );
                driver.manage ().timeouts ().implicitlyWait ( 120, TimeUnit.SECONDS );

                driver.manage().deleteAllCookies();
                driver.manage().window().maximize();

                // Load ExtentReports Configuration file
                reports.loadConfig ( new File ( Constants.PATH_EXTENT_CONFIG ) );

                // Get the environment details
                environment = GetProperty ( "ENVIRONMENT" );
                reports.addSystemInfo ( "Environment", environment );

                // Browser Information
                Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities ();
                logger.log ( LogStatus.INFO, "Environment : [" + environment
                        + "]       Browser : [" + capabilities.getBrowserName ().toUpperCase () + " "
                        + capabilities.getVersion () + "]");

                // Get Application URL
                LaunchTest ( driver, GetApplicationUrl ( environment ) );
                logger.log ( LogStatus.INFO, "The Web page Launched successfully." );
            }
            else
            {
                driver = InitBrowserStack(driver, testName);
            }
            return driver;
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Initialize the Test
     * Must be the first statement inside the Main method
     * @param driver WebDriver
     * @param args Represents Browser
     * @param testName Name of the Test
     * @throws Exception Exception
     */
    protected static WebDriver InitBrowserStack(WebDriver driver, String testName) throws Exception
    {
        try {
            // Run BrowserStack Local Binary
            Runtime.getRuntime().exec("resources/BrowserStackLocal.exe --key i3quzj5F4eSkSgFWTzPA");

            // Desired Capabilities
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("browser", GetProperty("BROWSER"));
            capabilities.setCapability("browser_version", GetProperty("BROWSER_VERSION"));
            capabilities.setCapability("os", GetProperty("OS"));
            capabilities.setCapability("os_version", GetProperty("OS_VERSION"));
            capabilities.setCapability("resolution", "1024x768");
            //capabilities.setCapability("browserstack.debug", true);;
            //capabilities.setCapability("browserstack.video", GetProperty("CAPTURE_VIDEO"));
            capabilities.setCapability("browserstack.local", "true");

            driver = new RemoteWebDriver(new URL(URL), capabilities);

            Locale.setDefault(Locale.ENGLISH);
            logger = reports.startTest ( testName );
            driver.manage ().timeouts ().implicitlyWait ( 120, TimeUnit.SECONDS );
            driver.manage().timeouts().setScriptTimeout(120,TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);

            driver.manage().deleteAllCookies();
            driver.manage().window().maximize();

            // Load ExtentReports Configuration file
            reports.loadConfig ( new File ( Constants.PATH_EXTENT_CONFIG ) );

            // Get the environment details
            environment = GetProperty ( "ENVIRONMENT" );
            reports.addSystemInfo ( "Environment", environment );

            // Browser Information

            logger.log ( LogStatus.INFO, "Environment : [" + environment
                    + "]       Browser : [" + capabilities.getBrowserName ().toUpperCase () + " "
                    + capabilities.getVersion () + "]");

            // Get Application URL
            LaunchTest ( driver, GetApplicationUrl ( environment ) );
            logger.log ( LogStatus.PASS, "The Web page Launched successfully." );
            return driver;
        }catch(Exception exception){
            throw exception;
        }
    }


    /**
     * Read the value of the property from property file
     * @param key Key supplied to read the value
     * @return The value of the key is returned
     * @throws Exception Generic Exception
     */
    protected static String GetProperty(String key) throws Exception{
        // Property File Path
        File propertyFile = new File(Constants.PATH_CONFIG);
        FileInputStream fileInput = null;

        try{
            fileInput = new FileInputStream(propertyFile);
        }catch(FileNotFoundException exception){
            throw exception;
        }

        Properties property = new Properties();
        try {
            property.load(fileInput);
        } catch (IOException ex) {
            throw ex;
        }
        // Return the property value based on the key
        return property.getProperty ( key.toUpperCase () );
    }


    /**
     * Get the application URL based on the environment
     * @param environment Environment
     * @return URL
     * @throws Exception Generic Exception
     */
    protected static String GetApplicationUrl(String environment) throws Exception{
        String appURL = Constants.EMPTY;
        try{
            switch(environment){
                case "SYSTST" :
                    appURL = GetProperty ( "SYSTST_URL" );
                    break;
                case "DEV" :
                    appURL = GetProperty ( "DEV_URL" );
                    break;
                case "UAT" :
                    appURL = GetProperty ( "UAT_URL" );
                    break;
                case "PRE_PROD":
                    appURL = GetProperty ( "PRE_PROD_URL" );
                    break;
                default:
                    break;
            }
        }catch(Exception exception){
            throw exception;
        }
        return appURL;
    }

    /**
     * Get the Data Input Spreadsheet
     * @return URL
     * @throws Exception Generic Exception
     */
    protected static String GetDataInput() throws Exception{
        environment = GetProperty ( "ENVIRONMENT" );
        try{
            switch(environment){
                case "SYSTST" :
                    return System.getProperty ( "user.dir" ) + "\\src\\main\\java\\DataInputs\\Admin_Console_TestData.xls";
                case "DEV" :
                    return System.getProperty ( "user.dir" ) + "\\src\\main\\java\\DataInputs\\Admin_Console_TestData_DEV.xls";
                case "UAT" :
                    return System.getProperty ( "user.dir" ) + "\\src\\main\\java\\DataInputs\\Admin_Console_TestData_UAT.xls";
                case "PRE_PROD":
                    return System.getProperty ( "user.dir" ) + "\\src\\main\\java\\DataInputs\\Admin_Console_TestData_Pre_Prod.xls";
                default:
                    return Constants.EMPTY;
            }
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Test Cleanup Method
     * Must be called inside the finally clause
     * If there are more than a driver used in Main method,
     * then, the secondary driver must be closed explicitly
     * @param driver WebDriver
     * @param sheetName Excel Worksheet Name
     * @throws Exception exception
     */
    protected static void TestCleanup(WebDriver driver, String sheetName) throws Exception{
        try{
            // Quit the App
            if(driver != null) driver.quit ();

            // Generate data for the next run
            GenerateData ( sheetName );

            // Finalize Report
            reports.endTest ( logger );

            // Write logging info to report
            reports.flush ();
    }catch(Exception exception){
        throw exception;
    }
    }

    // Method to Capture the Test Instances in Screenshot during the test
    // With highlighting the elements in the page
    public static String CaptureScreenshot(WebDriver driver, WebElement element) throws Exception
    {
        Thread.sleep(5000);

        // Capture Test Name through Reflection
        String className = new Exception().getStackTrace ()[1].getClassName ();

        // Highlight Element
        JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
        jsExecutor.executeScript ( "arguments[0].style.border='3px solid red'", element);

        // Capture Screenshot
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

        // Generate File path
        String filePath = System.getProperty("user.dir")+"/TestReports/Screenshots/" + className + "_" +GetCurrentDateTime ()+".png";
        // Copy the file to the desired location
        FileUtils.copyFile(scrFile, new File(filePath));

        /* TODO: Needs to be Programmatically handled */
        /* FIXME(KK) : Wait until the file is fully copied */
        //Thread.sleep(5000);
        return filePath;
    }

    // Method to Capture the Test Instances in Screenshot during the test
    public static String CaptureScreenshot(WebDriver driver) throws Exception
    {
        Thread.sleep(5000);
        // Capture the Test Name through Reflection
        String className = new Exception().getStackTrace ()[1].getClassName ();

        // Capture Screenshot
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

        // Generate File path
        String filePath = System.getProperty("user.dir")+"/TestReports/Screenshots/" + className + "_" + GetCurrentDateTime ()+".png";

        // Copy the file to the desired location
        FileUtils.copyFile(scrFile, new File(filePath));

        /* TODO: Needs to be Programmatically handled */
        /* FIXME(KK) : Wait until the file is fully copied */
        //Thread.sleep(5000);
        return filePath;
    }

    /**
     * Get Current Date Time Value
     * @return Current Data/Time
     * @throws Exception
     */
    private static String GetCurrentDateTime() throws Exception{
        // Get the Current Date-Time to add it to the file name
        DateTimeFormatter dateTime = DateTimeFormatter.ofPattern ("yyyyMMddHHmmss");
        return dateTime.format(java.time.LocalDateTime.now ());
    }

    // Common Method used to close the popups and child windows
    public static void ClosePopup(WebDriver driver) throws Exception
    {
        // Wait until the page loads, if it exceeds it throws TIMEOUT Exception
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        // Handle Popups
        String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
        String subWindowHandler = null;

        Set<String> handles = driver.getWindowHandles(); // get all window handles
        for (String handle : handles) {
            subWindowHandler = handle;
        }
        // == operator compares if 2 strings occupy the same memory location
        if(!subWindowHandler.equals(parentWindowHandler)) {
            driver.switchTo().window(subWindowHandler); // switch to popup window
            // perform operations on popup
            driver.close();
        }

        // Switch back to Parent Window
        driver.switchTo().window(parentWindowHandler);  // switch back to parent window
    }

    /**
     * Clear Cache
     */
    public static void ClearCache() throws Exception{
        Runtime run = Runtime.getRuntime ();
        Process process = run.exec ( "free -m" );
        process.waitFor ();
    }

    /**
     * Launch the Test
     * @param driver WebDriver
     * @param URL Application URL
     * @throws Exception exception
     */
    protected static void LaunchTest(WebDriver driver, String URL){
        driver.get(URL);
    }

    /**
     * Double Click on an element
     * @param driver Driver
     * @param element Element
     */
    public static void DoubleClick(WebDriver driver, WebElement element){
        try{
            Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
            String browserName = cap.getBrowserName().toLowerCase();
            if(!browserName.equals ( "firefox" )) {
                Actions action = new Actions ( driver );
                action.moveToElement ( element ).doubleClick ().build ().perform ();
            }
            else {
                ((JavascriptExecutor) driver).executeScript ( "var evt = document.createEvent('MouseEvents');"
                        + "evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                        + "arguments[0].dispatchEvent(evt);", element );
            }
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Set Text to a Textbox
     * @param driver Driver
     * @param element Element
     */
    public static void SetText(WebDriver driver, WebElement element, String text){
        try{
            Actions action = new Actions ( driver );
            action.moveToElement ( element ).click ().perform ();
            action.moveToElement ( element ).sendKeys ( text ).perform ();
        }catch(Exception exception){
            throw exception;
        }
    }


    /**
     * Drag and Drop
     * @param driver WebDriver
     * @param itemToDrag The Item to Drag
     * @param dropArea The Drop Location of the Item
     */
    public static void DragAndDrop(WebDriver driver, WebElement itemToDrag, WebElement dropArea) {
        try {
            Actions actions = new Actions ( driver );
            actions.dragAndDrop ( itemToDrag, dropArea ).build ().perform ();
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Simulate Drag and Drop in the Unsupported Browsers like IE 11.0
     * @param driver WebDriver
     * @param elementToDrag Element to Drag
     * @param target Element to Drop
     * @throws Exception Generic Exception
     */
    public static void simulateDragAndDrop(WebDriver driver, WebElement elementToDrag,
                                     WebElement target) throws Exception {

        String xto=Integer.toString(target.getLocation().x);
        String yto=Integer.toString(target.getLocation().y);
        ((JavascriptExecutor)driver).executeScript("function simulate(f,c,d,e){var b,a=null;for(b in eventMatchers)if(eventMatchers[b].test(c)){a=b;break}if(!a)return!1;document.createEvent?(b=document.createEvent(a),a==\"HTMLEvents\"?b.initEvent(c,!0,!0):b.initMouseEvent(c,!0,!0,document.defaultView,0,d,e,d,e,!1,!1,!1,!1,0,null),f.dispatchEvent(b)):(a=document.createEventObject(),a.detail=0,a.screenX=d,a.screenY=e,a.clientX=d,a.clientY=e,a.ctrlKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=1,f.fireEvent(\"on\"+c,a));return!0} var eventMatchers={HTMLEvents:/^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,MouseEvents:/^(?:click|dblclick|mouse(?:down|up|over|move|out))$/}; " +
                        "simulate(arguments[0],\"mousedown\",0,0); simulate(arguments[0],\"mousemove\",arguments[1],arguments[2]); simulate(arguments[0],\"mouseup\",arguments[1],arguments[2]); ",
                elementToDrag,xto,yto);
            }

    /**
     * Dynamically Generate Data for the next run
     * @param sheetName Name of the Data sheet
     * @throws IOException
     */
    public static void GenerateData(String sheetName) throws Exception {
        try{
            logger.log ( LogStatus.INFO, "Generating Data for the next Iteration..." );

            HSSFWorkbook wb;
            InputStream ExcelFileToRead = new FileInputStream ( GetDataInput ());
            wb = new HSSFWorkbook(ExcelFileToRead);

            HSSFSheet sheet = wb.getSheet (sheetName);
            HSSFRow row;
            String userId, business, businessUid, machineUserName, personUserName;

            Iterator rows = sheet.rowIterator ();
            rows.next ();

            switch(sheetName){
                case "CreatePerson" :
                    while (rows.hasNext ()) {
                        row = (HSSFRow) rows.next ();
                        userId = row.getCell ( 3 ).getStringCellValue ();
                        row.getCell ( 3 ).setCellValue ( IncrementNumber(userId) );
                        row.getCell ( 4 ).setCellValue ( generator.name().firstName() );
                        row.getCell ( 5 ).setCellValue ( generator.name ().lastName() );
                    }
                    break;

                case "CreateMachine" :
                    while (rows.hasNext ()) {
                        row = (HSSFRow) rows.next ();
                        userId = row.getCell ( 6 ).getStringCellValue ();
                        row.getCell ( 5 ).setCellValue ( generator.address().city() + generator.address().citySuffix ());
                        row.getCell ( 6 ).setCellValue ( IncrementNumber(userId) );
                    }
                    break;

                case "CreateBusiness" :
                    while (rows.hasNext ()) {
                        row = (HSSFRow) rows.next ();
                        userId = row.getCell ( 2 ).getStringCellValue ();
                        business = row.getCell ( 3 ).getStringCellValue ();
                        businessUid = row.getCell ( 4 ).getStringCellValue ();
                        machineUserName = row.getCell ( 13 ).getStringCellValue ();
                        personUserName = row.getCell ( 18 ).getStringCellValue ();
                        row.getCell ( 2 ).setCellValue ( IncrementNumber (userId) );
                        row.getCell ( 3 ).setCellValue ( IncrementNumber ( business ) );
                        row.getCell ( 4 ).setCellValue ( Increment_With_Zero(businessUid));
                        //row.getCell ( 4 ).setCellValue ( generator.bothify ( businessUid ).substring ( 0, 2 ));
                        row.getCell ( 11 ).setCellValue ( generator.address().city() + generator.address().citySuffix() );
                        row.getCell ( 13 ).setCellValue ( IncrementNumber ( machineUserName ) );
                        row.getCell ( 18 ).setCellValue ( IncrementNumber ( personUserName ) );
                        row.getCell ( 19 ).setCellValue ( generator.name().firstName() );
                        row.getCell ( 20 ).setCellValue ( generator.name().lastName() );

                    }
                    break;

                case "EditPerson":
                    while(rows.hasNext ()){
                        row = (HSSFRow) rows.next ();
                        row.getCell ( 4 ).setCellValue ( generator.name().firstName());
                        row.getCell ( 5 ).setCellValue ( generator.name().lastName() );
                    }
                    break;

                case "EditMachine":
                    while(rows.hasNext ()){
                        row = (HSSFRow) rows.next ();
                        row.getCell ( 5 ).setCellValue ( generator.address().city() + generator.address().citySuffix() );
                    }
                    break;

                default:
                    break;
            }

            logger.log ( LogStatus.INFO, "Data Generation for " + sheetName + " successful." );

            FileOutputStream fileOut = new FileOutputStream ( GetDataInput() );

            //write this workbook to an Output stream.
            wb.write ( fileOut );
            fileOut.flush ();
            fileOut.close ();
        }catch(Exception exception){
            logger.log ( LogStatus.WARNING, "Data could not be generated since the file was opened. " +
                    "Please close the Test Data spreadsheet and re-run the test." );
            throw exception;
        }
    }

    /**
     * Increment num with leading zeros and append to the original prefix
     * @param input Input text to modify
     * @return incremented value
     */
    public static String IncrementNumber(String input){
        String[] parts = input.split ( "_" );

        String incremented = String.format("%0" + parts[1].length() + "d",
                Integer.parseInt(parts[1]) + 1);

        incremented = parts[0] + "_" + incremented;
        return incremented;
    }

    /**
     * Increment numbers with leading zeros
     * @param input Input text to increment
     * @return incremented value
     */
    public static String Increment_With_Zero(String input){
        String incremented = String.format("%0" + input.length() + "d",
                Integer.parseInt(input) + 2);
        return incremented;
    }

    /**
     * Replace existing value in a textbox
     * @param element WebElement
     * @param textToReplace Text To Replace
     * @throws Exception Generic Exception
     */
    public static void ReplaceText(WebElement element, String textToReplace) throws Exception{
        try{
            element.clear ();
            element.sendKeys ( textToReplace );
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Set the Focus to the element
     * @param driver WebDriver
     * @param element WebElement
     * @throws Exception Generic Exception
     */
    public static void SetFocus(WebDriver driver, WebElement element) throws Exception{
        try{
            Actions builder = new Actions(driver);
            builder.moveToElement(element).build().perform();
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Read the Email notification content from the configured email id
     * @param searchFilter "Link" or "Password
     * @return Email content message
     * @throws Exception Generic Exception
     */
    public static String GetMailContent(String searchFilter) throws Exception{
        try{
            String msgBody = Constants.EMPTY;
            Properties props = new Properties();
            props.put("mail.store.protocol","imaps");

            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("smtp.gmail.com",GetProperty ( "EMAIL" ),GetProperty ( "EMAIL_PW" ));

            IMAPFolder folder = (IMAPFolder) store.getFolder("inbox");
            folder.open( Folder.READ_WRITE);

            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen,false);
            Message message[] = folder.search(unseenFlagTerm);
            String emailContent = Constants.EMPTY;
            int j = message.length-1;
            for (int i=j;i>=0;i--) {
                try {
                    emailContent = getTextFromMessage(message[i]);
                    if (emailContent.contains ( searchFilter ))
                    {
                        logger.log ( LogStatus.INFO, "Email notification received successfully with the subject [" + message[i].getSubject() + "]."  );
                        msgBody = emailContent;
                        break;
                    }
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }

            folder.close(false);
            store.close();
            return msgBody;
        }catch(Exception exception){
            throw exception;
        }
    }

    /**
     * Get Text from Email Message
     * @param message
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    private static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    /**
     * Get Text from MIME Multipart
     * @param mimeMultipart
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    private static String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }

}