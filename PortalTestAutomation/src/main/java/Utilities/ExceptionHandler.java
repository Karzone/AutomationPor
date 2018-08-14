package Utilities;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.openqa.selenium.WebDriver;

/**
 * Created by KKalaiyarasu on 04/09/2016.
 * Copyrights : KCOM
 */
public class ExceptionHandler extends ModuleBase {
    /**
     * Helper Method to handle Exceptions
     * @param e Exception
     * @param driver WebDriver
     * @param row DataSheet Row
     * @throws Exception
     */
    public static void HandleExceptions(Exception e, WebDriver driver, HSSFRow row) throws Exception
    {
        HSSFCell cell = row.createCell ( 2 );
        cell.setCellValue ( Constants.FAIL );
        logger.log ( LogStatus.INFO, "Exception Snapshot : " + logger.addScreenCapture ( CaptureScreenshot ( driver ) ) );
        logger.log ( LogStatus.FAIL, ExceptionUtils.getMessage ( e ) );
    }
}
