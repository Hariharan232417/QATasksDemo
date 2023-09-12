package com.leaftaps.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.framework.selenium.api.design.Locators;
import com.framework.testng.api.base.ProjectHooks;

public class HomePage extends ProjectHooks{
	
	public HomePage verifyHomePage() {
		verifyDisplayed(locateElement(Locators.LINK_TEXT, "CRM/SFA"));
		reportStep("Homepage is loaded", "pass");
		return this;
	}
	
	public HomePage getCRMSFATextAndWriteToExcel(String username) {
		verifyDisplayed(locateElement(Locators.LINK_TEXT, "CRM/SFA"));
		String text = getElementText(locateElement(Locators.LINK_TEXT, "CRM/SFA"));
		reportStep("Fetched Text from Application : "+text, "pass");
		writeToExcel(username, text);
		return this;
	}
	
	public HomePage writeToExcel(String username, String text) {
		String fileName = "./Data/SourceData.xlsx";
		FileInputStream in = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;

		// Get the objects of workbook and sheet:
		// ------------------------------------------

		if (new File(fileName).exists()) {

			try {
				in = new FileInputStream(fileName);
				workbook = new XSSFWorkbook(in);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			sheet = workbook.getSheet("Sheet1");
		} else {
			workbook = new XSSFWorkbook();
			sheet = workbook.createSheet("Sheet1");
		}

		XSSFCellStyle style = workbook.createCellStyle();

		XSSFRow row = null;
		XSSFCell cell = null;
		int rowCount = sheet.getLastRowNum()+1;

		System.out.println("Total rows in Excel : " + rowCount);

		// Iterate from Row 1 and get all texts from Google and Writes data to Excel sheet
		// -------------------------------------------------------------------------------

		for (int i = 1; i < rowCount; i++) {
			row = sheet.getRow(i);
			String firtsColumncell = row.getCell(0).getStringCellValue();
			
			if(firtsColumncell.equalsIgnoreCase(username))
			{
				cell = row.createCell(2);
				cell.setCellValue(text);
				break;
			}
			
			
		}
		
		
		//Close File output stream and Excel sheet:
		//---------------------------------------------

		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(fileName);
			// write data in the excel file
			workbook.write(outputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	
	public MyHomePage clickCrmsfaLink() {
		click(locateElement(Locators.LINK_TEXT, "CRM/SFA"));
		reportStep("CRM/SFA link is clicked", "pass");
		return new MyHomePage();
	}
	
}
