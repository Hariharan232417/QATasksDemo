package tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Dictionary {

	// Instantiate a ChromeDriver class.
	static RemoteWebDriver driver = null;
	public void readExcel(String filePath, String fileName, String sheetName) throws IOException, InterruptedException {
		System.setProperty("webdriver.chrome.silentOutput", "true");
	
		
		ChromeOptions options = new ChromeOptions();

			options.addArguments("--remote-allow-origins=*");
			options.addArguments("--no-sandbox"); 
			options.addArguments("--disable-dev-shm-usage"); 
			options.addArguments("--disable-notifications");  
			//options.addArguments("--headless");
			
				driver = new ChromeDriver(options);
		//System.setProperty("webdriver.chrome.driver","./driver/chromedriver.exe");
		//driver = new ChromeDriver();
		//driver.manage().window().setPosition(new Point(0, -1000));
		// Create an object of File class to open xlsx file
		File file = new File(filePath + "\\" + fileName);
		// Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		Workbook guru99Workbook = null;
		// Find the file extension by splitting file name in substring and getting only
		// extension name
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		if (fileExtensionName.equals(".xlsx")) {
			guru99Workbook = new XSSFWorkbook(inputStream);
		} else if (fileExtensionName.equals(".xls")) {
			guru99Workbook = new HSSFWorkbook(inputStream);
		}
		// String input
		String name = "";
		int count = 0;
		while (!name.equalsIgnoreCase("endend")) {
			// Read sheet inside the workbook by its name
			Sheet guru99Sheet = guru99Workbook.getSheet(sheetName);
			// Find number of rows in excel file
			int rowCount = guru99Sheet.getLastRowNum() - guru99Sheet.getFirstRowNum();
			// Create a loop over all the rows of excel file to read it
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter a value");
			name = sc.nextLine();
			count = 0;
			for (int i = 0; i < rowCount + 1; i++) {
				Row row = guru99Sheet.getRow(i);
				// Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum() - 1; j++) {
					if (name.equalsIgnoreCase(row.getCell(j).getStringCellValue())) {
						// Print Excel data in console
						System.out.print(
								row.getCell(j).getStringCellValue() + " == " + row.getCell(j + 1).getStringCellValue());
						System.out.println();
						count++;
					}
				}
			}
			if (count == 0) {

				// Storing the Application Url in the String variable
				String url = ("https://www.google.com/search?q=english+to+tamil+translator&rlz=1C1CHBD_enIN923IN923&oq=english+to+tamil+translator&aqs=chrome..69i57.14088j0j4&sourceid=chrome&ie=UTF-8");
				// Launch the ToolsQA WebSite
				driver.get(url);
				driver.findElement(By.xpath("//*[@id='tw-source-text-ta']")).sendKeys(name);
				Thread.sleep(800);
				String text = driver.findElement(By.xpath("//*[@id='tw-target-text']")).getText();
				System.err.println(name + " == " + text);

				// Create a new row and append it at last of sheet
				Row newRow = guru99Sheet.createRow(rowCount + 1);
				// Fill data in row

				Cell cell0 = newRow.createCell(0);
				Cell cell1 = newRow.createCell(1);

				cell0.setCellValue(name);
				cell1.setCellValue(text);
				// Create an object of FileOutputStream class to create write data in excel file
				FileOutputStream outputStream = new FileOutputStream(file);
				// write data in the excel file
				guru99Workbook.write(outputStream);

			}
		}

	}

	// Main function is calling readExcel function to read data from excel file

	public static void main(String... strings) throws IOException, InterruptedException {

		Dictionary objExcelFile1 = new Dictionary();

		// Prepare the path of excel file

		String filePath = System.getProperty("user.dir") + "/";

		// Call read file method of the class to read data

		objExcelFile1.readExcel("./", "dict-eng-to-tamil.xlsx", "Sheet1");
		driver.close();
	}

}
