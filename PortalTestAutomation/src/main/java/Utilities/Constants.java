package Utilities;

/**
 * Created by KKalaiyarasu on 01/09/2016.
 * Copyrights : KCOM
 */
public class Constants{
    // Results
    public static String PASS = "PASS";
    public static String FAIL = "FAIL";

    // Browsers
    public static String[] CHROME = {"--driver=chrome"};
    public static String[] FIREFOX = {"--driver=firefox"};
    public static String[] IE = {"--driver=ie"};

    // Other Constants
    public static String YES = "Yes";
    public static String NO = "No";
    public static String EMPTY = "";
    public static String TAG_TH = "th";
    public static String TAG_TD = "td";

    public static String GENERATE_TOKEN = "GENERATE A TOKEN";
    public static String USER_NOT_ACTIVE = "User not Active";

    // Known Paths
    public static String PATH_DATA_SHEET = System.getProperty ( "user.dir" ) + "\\src\\main\\java\\DataInputs\\Admin_Console_TestData.xls";
    public static String PATH_CONFIG = System.getProperty ( "user.dir" ) + "\\src\\main\\java\\DataInputs\\config.properties";
    public static String PATH_EXTENT_CONFIG = System.getProperty ( "user.dir" ) + "\\extent-config.xml";

    // Create Person Standard Password
    public static String CREATE_PERSON_STANDARD_PASSWORD = "Password@123";
}