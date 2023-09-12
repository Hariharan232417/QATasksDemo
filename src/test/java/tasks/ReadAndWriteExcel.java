package tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ReadAndWriteExcel {

	static RemoteWebDriver driver = null;

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.silentOutput", "true");

		ChromeOptions options = new ChromeOptions();

		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-notifications");
		// options.addArguments("--headless");

		driver = new ChromeDriver(options);
		
		driver.get("https://www.google.com");
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

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
			driver.findElement(By.xpath("//textarea[@id='APjFqb']")).sendKeys(firtsColumncell, Keys.ENTER);
			
			//String text = driver.findElement(By.xpath("(//div[@id=\"res\"]//span[@class=\"VuuXrf\"])[1]")).getText();
			
			cell = row.createCell(1);
			cell.setCellValue(i);
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
		
	}

}
