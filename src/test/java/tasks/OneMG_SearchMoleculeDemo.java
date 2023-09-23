package tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OneMG_SearchMoleculeDemo {

	static ChromeDriver driver = null;

	public static void openApplication() {
		System.setProperty("webdriver.chrome.silentOutput", "true");

		ChromeOptions options = new ChromeOptions();

		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-notifications");
		// options.addArguments("--headless");

		driver = new ChromeDriver(options);

		String website = "https://www.1mg.com/";

		driver.get(website);

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
	}

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		openApplication();

		Map<String, List<String>> map_AllResults = getAllCardText();

		System.out.println(map_AllResults);
		
		Map<String,Map<String,List<String>>> wholeResults = getAllDetails(map_AllResults);
		
		System.out.println(wholeResults);

		writeAllResults_Excel(wholeResults);
		
		driver.close();

	}
	
	public static Map<String,Map<String,List<String>>> getAllDetails(Map<String, List<String>> map) throws InterruptedException
	{
		
		Map<String,Map<String,List<String>>> mapWholeDetails = new LinkedHashMap();
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<String> values = entry.getValue();

			Map<String,List<String>> mapAllDetails = new LinkedHashMap();

			// Write list values to subsequent columns
			
			for (int i = 0; i < values.size(); i++) {
				List<String> lst = new ArrayList();
				String originalWindowHandle = driver.getWindowHandle();
				try {
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//input[@id='srchBarShwInfo']"))));
					
					String eachAlternatives = values.get(i);
				
					driver.findElement(By.xpath("//input[@id='srchBarShwInfo']")).sendKeys(eachAlternatives, Keys.ENTER);
					
					String xpath_ProductTitle = "//div[contains(@class,'row style__grid-container')]//a//span[contains(@class,'style__pro-title')]";
					
					wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpath_ProductTitle))));
								
					//Mrp:
					String xpath_PriceTag = "//div[contains(@class,'style__price-tag___')]";
					String MRPFetchedText = getElementText(xpath_PriceTag,"MRP");
					lst.add(MRPFetchedText);
					
					
					String productName = values.get(i).trim();
					String xpathExpression = "//div[contains(@class,'row style__grid-container')]//a//span[contains(@class,'style__pro-title') and contains(normalize-space(),'"+productName+"')]";
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpathExpression))));
					wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(xpathExpression))));
					driver.findElement(By.xpath(xpathExpression)).click();
				
					Set<String> windowHandles = driver.getWindowHandles();
					
					List<String> lstWindowHandles = new ArrayList(windowHandles);
					
					driver.switchTo().window(lstWindowHandles.get(1));
					
					String xpath_DrugHeader = "//div[@id='drug_header']";
					Thread.sleep(500);
					wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpath_DrugHeader))));
					
					//Prescription required:
					String xpath_presecriptionRequired = "//div[@id='drug_header']//div[contains(@class,'DrugHeader__prescription-req')]/span";
					String PrescriptionRequiredFetchedText = getElementText(xpath_presecriptionRequired,"Prescription Required");
					lst.add(PrescriptionRequiredFetchedText);
					
					//Manufacturer
					String xpath_Manufacturer = "//div[@id='drug_header']//div[contains(@class,'DrugHeader__meta-value') and not(contains(@class,'saltInfo'))]/a";
					String ManufacturerFetchedText = getElementText(xpath_Manufacturer,"Manufacturer");
					lst.add(ManufacturerFetchedText);
					
					
					//Salt Composition
					String xpath_SaltComposition = "//div[@id='drug_header']//div[contains(@class,'saltInfo DrugHeader__meta-value')]/a";
					String SaltCompositionFetchedText = getElementText(xpath_SaltComposition,"Salt Composition");
					lst.add(SaltCompositionFetchedText);
					
					//Storage:
					String xpath_Storage = "//div[@id='drug_header']//div[text()='Storage']/following::div[contains(@class,'saltInfo DrugHeader__meta-value')]";
					String StorageFetchedText = getElementText(xpath_Storage,"Storage");
					lst.add(StorageFetchedText);
					
					//Images link
					String xpath_ImageLinks = "//div[contains(@class,'DrugHeader__slider-wrapper')]//img";
					String ImageLinksFetchedText = getImagesLink_StoreInList(xpath_ImageLinks);
					lst.add(ImageLinksFetchedText);
					
					//Uses:
					String xpath_UsesAndBenefits = "//div[@id='scroll-bar']//ul/a/span[text()='Uses and benefits']";
					driver.findElement(By.xpath(xpath_UsesAndBenefits)).click();
					String xpathUses = "//div[@id='uses_and_benefits']//h2[contains(text(),'Uses of')]/..";
					wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpathUses))));
					
					String Uses_FetchedText = getElementText(xpathUses,"Uses");
					lst.add(Uses_FetchedText);
					
					//Fact box:
					String xpath_FactBox = "//div[@id='scroll-bar']//ul/a/span[text()='Fact box']";
					driver.findElement(By.xpath(xpath_FactBox)).click();
					Thread.sleep(400);
					String xpath_Left = "//div[@id='fact_box']//div[contains(@class,'DrugFactBox__col-left')]";
					String xpath_Right = "//div[@id='fact_box']//div[contains(@class,'DrugFactBox__col-right')]";
					wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpath_Left))));
					String FactBox_FetchedText = getFactbox_StoreInList(xpath_Left,xpath_Right);
					lst.add(FactBox_FetchedText);
					
					driver.close();
					
					Thread.sleep(1000);
										
					driver.switchTo().window(originalWindowHandle);
				
				}
				catch(NoSuchElementException e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage()+" Element Not found - "+values.get(i));
					lst.add(e.getMessage()+" Element not found when fetching element - "+values.get(i));
					
					if(!driver.getWindowHandle().equals(originalWindowHandle))
					{
						driver.close();
						
						Thread.sleep(1000);
											
						driver.switchTo().window(originalWindowHandle);
					}
					
				}
				catch(ElementClickInterceptedException e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage()+" ElementClickInterceptedException - "+values.get(i));
					lst.add(e.getMessage()+" ElementClickInterceptedException when fetching element - "+values.get(i));
					
					if(!driver.getWindowHandle().equals(originalWindowHandle))
					{
						driver.close();
						
						Thread.sleep(1000);
											
						driver.switchTo().window(originalWindowHandle);
					}
					
					
					
				}
				catch(WebDriverException e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage()+" WebDriverException - "+values.get(i));
					lst.add(" WebDriverException when fetching element - "+values.get(i));
					
					if(!driver.getWindowHandle().equals(originalWindowHandle))
					{
						driver.close();
						
						Thread.sleep(1000);
											
						driver.switchTo().window(originalWindowHandle);
					}
					
				}
				
				mapAllDetails.put(values.get(i), lst);
				
			}
			
			mapWholeDetails.put(key, mapAllDetails);
			
			
		}
		return mapWholeDetails;
	}

	
	//Reads the excel workbook and write all the Map contents to that Excel workbook
	//--------------------------------------------------------------------------------
	public static void writeAllResults_Excel(Map<String, Map<String, List<String>>> map) {

		String fileName = "./Data/AllResults.xlsx";
		FileInputStream in = null;
		FileOutputStream outputStream = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;

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

		XSSFRow row = null;
		XSSFCell cell = null;
		int rowCount = sheet.getLastRowNum() + 1;

		int rowNum = 1;
		row = sheet.createRow(0);
        row.createCell(0).setCellValue("Molecule");
        row.createCell(1).setCellValue("Alternates");
        row.createCell(2).setCellValue("MRP");
        row.createCell(3).setCellValue("Prescription Required");
        row.createCell(4).setCellValue("MANUFACTURER");
        row.createCell(5).setCellValue("SALT COMPOSITION");
        row.createCell(6).setCellValue("STORAGE");
        row.createCell(7).setCellValue("Image Links");
        row.createCell(8).setCellValue("USES");
        row.createCell(9).setCellValue("FACT BOX");
        
        
		
        // Iterate through the outer map
        for (Entry<String, Map<String, List<String>>> outerEntry : map.entrySet()) {
            String outerKey = outerEntry.getKey();
            Map<String, List<String>> innerMap = outerEntry.getValue();
            

            // Iterate through the inner map
            for (Map.Entry<String, List<String>> innerEntry : innerMap.entrySet()) {
                String innerKey = innerEntry.getKey();
                List<String> innerList = innerEntry.getValue();
                row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(outerKey);
                row.createCell(1).setCellValue(innerKey);
                
                int cells = 2;
                
                // Iterate through the list
                for (String value : innerList) {
                   row.createCell(cells++).setCellValue(value);
                }
            }
         
        }
        
        
		try {

			outputStream = new FileOutputStream(fileName);

			workbook.write(outputStream);
			
			workbook.close();
			outputStream.close();
			System.out.println("Excel file created successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * try { outputStream = new FileOutputStream(fileName); // write data in the
		 * excel file //workbook.write(outputStream); } catch (FileNotFoundException e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

	}

	public static Map<String, List<String>> getAllCardText() throws InterruptedException {
		// Get the objects of workbook and sheet:
		// ------------------------------------------

		String fileName = "./Data/SourceData.xlsx";
		FileInputStream in = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;

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
		int rowCount = sheet.getLastRowNum() + 1;

		// System.out.println("Total rows in Excel : " + rowCount);

		// Iterate from Row 1 and get all texts from app and store all cards text in a
		// Map
		// -------------------------------------------------------------------------------
		
		//Close the Banner:
		
		driver.findElement(By.xpath("//*[contains(@class,'style__close-icon')]")).click();

		Map<String, List<String>> mapAllResults_CardsText = new LinkedHashMap();
		
		
		for (int i = 1; i < rowCount; i++) {
			row = sheet.getRow(i);
			String molecule = row.getCell(0).getStringCellValue();

			driver.findElement(By.xpath("//input[@id='srchBarShwInfo']")).sendKeys(molecule, Keys.ENTER);
			
			String xpath_eachCard = "//div[@class='row style__grid-container___3OfcL']//a//span[contains(@class,'style__pro-title')]";
			
			Thread.sleep(1000);
			
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpath_eachCard))));
			
	        //scrollTillLastCard(eachCard);
			
			List<String> lstAllResults_CardsText = storeAllAlternativesText_TillNextButton_IsDisabled(xpath_eachCard);
			
			System.out.println("Total Alternatives: "+lstAllResults_CardsText);

			mapAllResults_CardsText.put(molecule, lstAllResults_CardsText);

		}

		// cell = row.createCell(1);
		// cell.setCellValue(i);

		// Close File input stream and Excel sheet:
		// ---------------------------------------------

		try {
			in.close();
			workbook.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return mapAllResults_CardsText;

	}
	
	
	
	public static List<String> storeAllAlternativesText_TillNextButton_IsDisabled(String xpath) throws InterruptedException
	{
		
		List<String> lstAllAlternativesText=new ArrayList();
		while(true)
		{
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpath))));
						
			List<WebElement> lstAll_Alternatives = driver.findElements(By.xpath(xpath));
			
			for(int i=0;i<lstAll_Alternatives.size();i++)
			{
				lstAllAlternativesText.add(lstAll_Alternatives.get(i).getText());
			}
			
			String xpath_NextButton = "//div[contains(@class,'style__paginate-container')]//span[contains(@class,'style__next')]";
			boolean nextButtonDisabledAttributeFlag = driver.findElement(By.xpath(xpath_NextButton)).getAttribute("class").contains("disabled");
			if(nextButtonDisabledAttributeFlag==false)
				driver.findElement(By.xpath(xpath_NextButton)).click();
			else
				break;
			
		}
		
		return lstAllAlternativesText;
				
		
	}
	
	public static List<String> addInList(List<WebElement> lstElements)
	{
		List<String> allTexts = new ArrayList();
		for(int i=0;i<lstElements.size();i++)
		{
			allTexts.add(lstElements.get(i).getText());
		}
		
		return allTexts;
	}
	
	
	public static String getElementText(String xpath,String ElementName) throws InterruptedException
	{
		String text = "";
		try
		{
			Thread.sleep(200);
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpath))));
			
			text = driver.findElement(By.xpath(xpath)).getText();
		}
		catch(NoSuchElementException e)
		{
			text = "No element Found for "+ElementName;
		}
		
		return text;
		
	}
	
	public static String getFactbox_StoreInList(String xPathColumnLeft, String xPathColumnRight)
	{
		String text = "";
		try
		{
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xPathColumnLeft))));
			List<WebElement> lstAllElementsColumnLeft = driver.findElements(By.xpath(xPathColumnLeft));
			List<WebElement> lstAllElementsColumnRight = driver.findElements(By.xpath(xPathColumnRight));
			int count = lstAllElementsColumnLeft.size();
			
			
			
			for(int i=0;i<count;i++)
			{
				text += lstAllElementsColumnLeft.get(i).getText() + " = " + lstAllElementsColumnRight.get(i).getText() + "\n";
			}
			
			
		}
		catch(NoSuchElementException e)
		{
			text = "No element Found - Synopsis";
		}
		
		return text;
		
	}
	
	public static String getImagesLink_StoreInList(String xPath)
	{
		String text = "";
		try
		{
			//WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
			//wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xPath))));
			List<WebElement> lstAllElements = driver.findElements(By.xpath(xPath));
			
			
			int count = lstAllElements.size();
			
			if(count>0)
			{
				for(int i=0;i<count;i++)
				{
					text = lstAllElements.get(i).getAttribute("src")+" ; "+text;
				}
			}
			else
			{
				text = "No Element found for images link";
			}
			
		}
		catch(NoSuchElementException e)
		{
			text = "No element Found - Image  Links";
		}
		
		return "Image Links ---> "+text;
		
	}
	public static void scrollTillLastCard(List<WebElement> lst) throws InterruptedException
	{
		int initialCardCount = lst.size();

        // Scroll to the bottom of the page
		JavascriptExecutor js = (JavascriptExecutor) driver;

        // Define a maximum timeout (e.g., 30 seconds) for waiting for new cards to load
        long maxWaitTimeInSeconds = 1000;
        long startTime = System.currentTimeMillis();
        
        System.out.println("Started at Time (in Ms) : "+startTime);

        // Periodically check if new cards have loaded
        while (true) {
            // Get the current count of cards/items
        	int previousCardCount = driver.findElements(By.xpath("//div[@class='product-list']//li")).size();
        	//js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        	
        	js.executeScript("window.scrollBy(0, 300);");
        	// Calculate the target scroll position (scrollable height - 10% of window height)
            //long windowHeight = (long) js.executeScript("return window.innerHeight;");
            //long scrollHeight = (long) js.executeScript("return Math.max( document.documentElement.scrollHeight, document.body.scrollHeight );");
            //long targetScrollPosition = (long) (0.90 * scrollHeight);
            
            Thread.sleep(1000);
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

            // Scroll to the target position
            //js.executeScript("window.scrollTo(0, arguments[0]);", targetScrollPosition);
            
            List<WebElement> lstAllProductList = driver.findElements(By.xpath("//div[@class='product-list']//li"));
            
            Thread.sleep(1000);
            js.executeScript("arguments[0].scrollIntoView();", driver.findElements(By.xpath("//div[@class='product-list']//li")).get(lstAllProductList.size()-1));

            if(driver.findElements(By.xpath("//button[@class='ais-InfiniteHits-loadMore ais-InfiniteHits-loadMore--disabled']")).size()>0)
               	break;
            

            int currentCardCount = driver.findElements(By.xpath("//div[@class='product-list']//li")).size();

            // Check if new cards have loaded (compare with initial count)
            if (previousCardCount == currentCardCount)               
                break;
            

            // Check if the maximum wait time has been exceeded
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - startTime;
            System.out.println("Current Time - StartTime (in Ms) : "+timeDiff);
            if ((currentTime - startTime) >= (maxWaitTimeInSeconds * 1000)) 
                break;
            

            // Sleep for a short duration (e.g., 1 second) before checking again
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}

}
