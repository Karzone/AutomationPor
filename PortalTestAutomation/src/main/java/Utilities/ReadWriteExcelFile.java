package Utilities;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ReadWriteExcelFile {

    public static void readXLSFile(String filePath) throws IOException
    {
        try {
            InputStream ExcelFileToRead = new FileInputStream ( filePath );
            HSSFWorkbook wb = new HSSFWorkbook ( ExcelFileToRead );

            HSSFSheet sheet = wb.getSheetAt ( 0 );
            HSSFRow row;
            HSSFCell cell;

            Iterator rows = sheet.rowIterator ();

            while (rows.hasNext ()) {
                row = (HSSFRow) rows.next ();
                Iterator cells = row.cellIterator ();

                while (cells.hasNext ()) {
                    cell = (HSSFCell) cells.next ();

                    if (cell.getCellType () == HSSFCell.CELL_TYPE_STRING) {
                        System.out.print ( cell.getStringCellValue () + " " );
                    } else if (cell.getCellType () == HSSFCell.CELL_TYPE_NUMERIC) {
                        System.out.print ( cell.getNumericCellValue () + " " );
                    } else {
                        //U Can Handle Boolean, Formula, Errors
                    }
                }
                System.out.println ();
            }
        }catch(Exception exception){
            throw exception;
        }

    }

    public static void writeXLSFile() throws IOException {

        try {

            String sheetName = "CreatePerson"; //name of sheet

            HSSFWorkbook wb;
            InputStream ExcelFileToRead = new FileInputStream ( Utilities.Constants.PATH_DATA_SHEET);
            wb = new HSSFWorkbook(ExcelFileToRead);

            HSSFSheet sheet = wb.getSheet ("CreatePerson");
            HSSFRow row;
            String userId;

            Iterator rows = sheet.rowIterator ();
            rows.next ();

            while (rows.hasNext ()) {
                row = (HSSFRow) rows.next ();
                userId = row.getCell ( 3 ).getStringCellValue ();
                row.getCell ( 3 ).setCellValue ( "Test" );
            }

            FileOutputStream fileOut = new FileOutputStream ( Constants.PATH_DATA_SHEET );

            //write this workbook to an Outputstream.
            wb.write ( fileOut );
            fileOut.flush ();
            fileOut.close ();
        }
        catch(Exception exception){
            throw exception;
        }
    }

}