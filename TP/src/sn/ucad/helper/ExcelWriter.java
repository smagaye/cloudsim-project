package sn.ucad.helper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ExcelWriter {

	private static final String OUTPUT_FOLDER ="../Resultats/";
    private static final String OUTPUT_FILE = "tp_cloudsim.xlsx";
	private static final String TEMPLATE_XLS_FILE_PATH = "./templates/table_and_graphe.xlsx";
	private static String[] columns = {"Nombre de VMs", "Charge Fixe", "Charge Aleatoire"};
	static {
		Path path = Paths.get(OUTPUT_FOLDER);

		if (Files.exists(path)) {
		  System.out.println("Exist");
		}else {
			System.out.println("Not Exist"); 
			try {
				Files.createDirectory(path);
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
    public static void generateExcelFile(double fixe[],double aleatoire[]) throws IOException, InvalidFormatException {
		DecimalFormat dft = new DecimalFormat("###.##");
    	List<TableRow> tablesRows =  new ArrayList<>();
    	for(int i=0; i<10;i++) {
			System.out.println(i+1+"\t"+dft.format(fixe[i])+"\t"+dft.format(aleatoire[i]));
    		tablesRows.add(new TableRow(i+1, fixe[i],aleatoire[i]));
    	}
    	// Create a Workbook
        Workbook workbook = new HSSFWorkbook();     // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances for various things like DataFormat,
           Hyperlink, RichTextString etc in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Temps d'éxéutuion");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(false);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Creating cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        // Create Other rows and cells with tablesRows data
        int rowNum = 1;
        for(TableRow tableRow: tablesRows) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(tableRow.getVms());

            row.createCell(1)
                    .setCellValue(tableRow.getFixe());

            row.createCell(2)
                    .setCellValue(tableRow.getAleatoire());
        }

        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(OUTPUT_FOLDER+OUTPUT_FILE);
        workbook.write(fileOut);
        fileOut.close();

        workbook.close();
        System.out.println("Fichier excel "+OUTPUT_FOLDER+OUTPUT_FILE+" généré avec succès...");
    }

    public static void generateExcelFileUsingTemplate(double fixe[],double aleatoire[]) throws IOException, InvalidFormatException {
		DecimalFormat dft = new DecimalFormat("###.##");
    	List<TableRow> tablesRows =  new ArrayList<>();
    	for(int i=0; i<10;i++) {
			System.out.println(i+1+"\t"+dft.format(fixe[i])+"\t"+dft.format(aleatoire[i]));
    		tablesRows.add(new TableRow(i+1, fixe[i],aleatoire[i]));
    	}
    	// Create a Workbook
    	Workbook workbook = new XSSFWorkbook(new FileInputStream(new File(TEMPLATE_XLS_FILE_PATH)));

        // Create a Sheet
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        System.out.println("Retrieving Sheets using Iterator");
        Sheet sheet = null;
        while (sheetIterator.hasNext()) {
            Sheet sheetTmp = sheetIterator.next();
            if("Temps d'éxéutuion".equals(sheetTmp.getSheetName())) {
            	sheet = sheetTmp;
            	break;
            }
        }

        // Create Other rows and cells with tablesRows data
        int rowNum = 1;
        for(TableRow tableRow: tablesRows) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(tableRow.getVms());

            row.createCell(1)
                    .setCellValue(tableRow.getFixe());

            row.createCell(2)
                    .setCellValue(tableRow.getAleatoire());
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(OUTPUT_FOLDER+OUTPUT_FILE);
        workbook.write(fileOut);
        fileOut.close();

        workbook.close();
        System.out.println("Fichier excel "+OUTPUT_FOLDER+OUTPUT_FILE+" généré avec succès...");
    }
}


class TableRow {
    private int vms;

    private double fixe;

    private double aleatoire;


    public TableRow(int vms, double fixe, double aleatoire) {
        this.vms = vms;
        this.fixe = fixe;
        this.aleatoire = aleatoire;
    }


	public int getVms() {
		return vms;
	}


	public void setVms(int vms) {
		this.vms = vms;
	}


	public double getFixe() {
		return fixe;
	}


	public void setFixe(double fixe) {
		this.fixe = fixe;
	}


	public double getAleatoire() {
		return aleatoire;
	}


	public void setAleatoire(double aleatoire) {
		this.aleatoire = aleatoire;
	}

}
