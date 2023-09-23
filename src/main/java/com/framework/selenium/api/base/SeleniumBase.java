package com.framework.selenium.api.base;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.framework.selenium.api.design.Browser;
import com.framework.selenium.api.design.Element;
import com.framework.selenium.api.design.Locators;
import com.framework.utils.ExtentReporter;

public class SeleniumBase extends ExtentReporter implements Browser, Element {
	protected Actions act;

	public static String projectId;
	public static String auctionRef;

	protected String getAttribute(WebElement ele, String attributeValue) {
		String val = "";
		try {
			val = ele.getAttribute(attributeValue);
		} catch (WebDriverException e) {
			reportStep("Attribue value not able to fetch :" + e.getMessage(), "info");
		}
		return val;
	}

	protected void moveToElement(WebElement ele) {
		act = new Actions(getDriver());
		act.moveToElement(ele).perform();
	}

	protected void dragAndDrop(WebElement eleSoutce, WebElement eleTarget) {
		act = new Actions(getDriver());
		act.dragAndDrop(eleSoutce, eleTarget).perform();
	}

	protected void contextClick(WebElement ele) {
		act = new Actions(getDriver());
		act.contextClick(getWait().until(ExpectedConditions.elementToBeClickable(ele))).perform();
	}

	protected void hoverAndClick(WebElement ele) {
		act = new Actions(getDriver());
		act.moveToElement(getWait().until(ExpectedConditions.elementToBeClickable(ele))).pause(5000).click().perform();
	}

	protected void doubleTap(WebElement ele) {
		act = new Actions(getDriver());
		act.click(getWait().until(ExpectedConditions.elementToBeClickable(ele))).click().perform();
		reportStep("Element moved", "info");
	}

	protected void doubleClick(WebElement ele) {
		act = new Actions(getDriver());
		act.doubleClick(getWait().until(ExpectedConditions.elementToBeClickable(ele))).perform();
		reportStep("Element double clicked", "info");
	}

	public void waitForApperance(WebElement element) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			reportStep("Element did not appear after 20 seconds", "fail", false);

		}

	}

	@Override
	public void click(WebElement ele) {
		try {
			ele.isDisplayed(); // @FindBy return the proxy even if it does not exist !!
		} catch (NoSuchElementException e) {
			reportStep("The Element " + ele + " is not found", "fail");
		}

		String text = "";
		try {
			try {
				Thread.sleep(500);
				getWait().until(ExpectedConditions.elementToBeClickable(ele));
				text = ele.getText();
				if (ele.isEnabled()) {
					ele.click();
				} else {
					getDriver().executeScript("arguments[0].click()", ele);
				}
			} catch (Exception e) {
				boolean bFound = false;
				int totalTime = 0;
				while (!bFound && totalTime < 10000) {
					try {
						Thread.sleep(500);
						ele.click();
						bFound = true;

					} catch (Exception e1) {
						bFound = false;
					}
					totalTime = totalTime + 500;
				}
				if (!bFound)
					ele.click();
			}
		} catch (StaleElementReferenceException e) {
			System.err.println(e);
			reportStep("The Element " + text + " could not be clicked due to:" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			System.err.println(e);
			reportStep("The Element " + ele + " could not be clicked due to: " + e.getMessage(), "fail");
		} catch (Exception e) {
			System.err.println(e);
			reportStep("The Element " + ele + " could not be clicked due to: " + e.getMessage(), "fail");
		}
	}

	public void clickUsingJs(WebElement ele) {
		try {
			ele.isDisplayed(); // @FindBy return the proxy even if it does not exist !!
		} catch (NoSuchElementException e) {
			reportStep("The Element " + ele + " is not found", "fail");
		}

		String text = "";
		try {
			try {
				getDriver().executeScript("arguments[0].click()", ele);
			} catch (Exception e) {
				boolean bFound = false;
				int totalTime = 0;
				while (!bFound && totalTime < 10000) {
					try {
						Thread.sleep(500);
						getDriver().executeScript("arguments[0].click()", ele);
						bFound = true;

					} catch (Exception e1) {
						bFound = false;
					}
					totalTime = totalTime + 500;
				}
				if (!bFound)
					getDriver().executeScript("arguments[0].click()", ele);
			}
		} catch (StaleElementReferenceException e) {
			reportStep("The Element " + text + " could not be clicked due to:" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			reportStep("The Element " + ele + " could not be clicked due to: " + e.getMessage(), "fail");
		} catch (Exception e) {
			reportStep("The Element " + ele + " could not be clicked due to: " + e.getMessage(), "fail");
		}
	}

	public void click(Locators locatorType, String value) {
		String text = "";
		WebElement ele = locateElement(locatorType, value);
		try {
			try {
				Thread.sleep(500);
				getWait().until(ExpectedConditions.elementToBeClickable(ele));
				text = ele.getText();
				if (ele.isEnabled()) {
					ele.click();
				} else {
					getDriver().executeScript("arguments[0].click()", ele);
				}
			} catch (Exception e) {
				boolean bFound = false;
				int totalTime = 0;
				while (!bFound && totalTime < 10000) {
					try {
						Thread.sleep(500);
						ele = locateElement(locatorType, value);
						ele.click();
						bFound = true;

					} catch (Exception e1) {
						bFound = false;
					}
					totalTime = totalTime + 500;
				}
				if (!bFound)
					ele.click();
			}
		} catch (StaleElementReferenceException e) {
			reportStep("The Element " + text + " could not be clicked " + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			reportStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail");
		} catch (Exception e) {
			reportStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail");
		}
	}

	public void clickWithNoSnap(WebElement ele) {
		try {
			getWait().until(ExpectedConditions.elementToBeClickable(ele));
			ele.click();
		} catch (StaleElementReferenceException e) {
			reportStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail", false);
		} catch (WebDriverException e) {
			reportStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail", false);
		} catch (Exception e) {
			reportStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail", false);
		}
	}

	@Override
	public void append(WebElement ele, String data) {
		try {
			String attribute = ele.getAttribute("value");
			if (attribute.length() > 1) {
				ele.sendKeys(data);
			} else {
				ele.sendKeys(data);
			}
		} catch (WebDriverException e) {
			reportStep("The Element " + ele + " could not be appended \n" + e.getMessage(), "fail");
		}
	}

	@Override
	public void clear(WebElement ele) {
		try {
			ele.clear();
		} catch (ElementNotInteractableException e) {
			reportStep("The field is not Interactable \n" + e.getMessage(), "fail");
		}
	}

	/**
	 * Overloaded method used to clear the existing value and type the data with
	 * keys for tab or enter kind of
	 * 
	 * @param ele  - WebElement from the DOM
	 * @param data - Use to type and pass Keys as many needed
	 */
	public void clearAndType(WebElement ele, CharSequence... data) {
		try {
			getWait().until(ExpectedConditions.visibilityOf(ele));
			ele.clear();
			ele.sendKeys(data);
		} catch (ElementNotInteractableException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) { // retry - 1
			pause(500);
			try {
				ele.sendKeys(data);
			} catch (Exception e1) {
				reportStep("The Element " + ele + " did not allow to clear / type \n" + e.getMessage(), "fail");
			}
		}

	}

	@Override
	public void clearAndType(WebElement ele, String data) {
		try {
			getWait().until(ExpectedConditions.visibilityOf(ele));
			ele.clear();
			ele.sendKeys("", "", data);
		} catch (ElementNotInteractableException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) { // retry - 1
			pause(500);
			try {
				ele.sendKeys(data);
			} catch (Exception e1) {
				reportStep("The Element " + ele + " did not allow to clear / type \n" + e.getMessage(), "fail");
			}
		}

	}

	public void typeAndTab(WebElement ele, String data) {
		try {
			getWait().until(ExpectedConditions.visibilityOf(ele));
			ele.clear();
			ele.sendKeys("", "", data, Keys.TAB);
		} catch (ElementNotInteractableException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		}

	}

	public void type(WebElement ele, String data) {
		try {
			getWait().until(ExpectedConditions.visibilityOf(ele));
			ele.clear();
			ele.sendKeys("", "", data);
		} catch (ElementNotInteractableException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		}

	}

	public void typeAndEnter(WebElement ele, String data) {
		try {
			getWait().until(ExpectedConditions.visibilityOf(ele));
			ele.clear();
			ele.sendKeys("", "", data, Keys.ENTER);
		} catch (ElementNotInteractableException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		} 
		catch (StaleElementReferenceException e) {
			reportStep("The Element " + ele + " is stale \n" + e.getMessage(), "fail");
		}
		catch (WebDriverException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		}

	}

	@Override
	public String getElementText(WebElement ele) {
		try {
			String text = ele.getText();
			reportStep("Text has been retrieved " + text, "info");
			return text;
		} catch (WebDriverException e) {
			reportStep("Sorry! text is not available \n" + e.getMessage(), "fail");
		} catch (Exception e) {
			reportStep("Sorry! text is not available \n" + e.getMessage(), "fail");
		}
		return null;
	}
	
	public String getElementText(String xpath,String ElementName)
	{
		String text = "";
		try
		{
			WebDriverWait wait = new WebDriverWait(getDriver(),Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath(xpath))));
			
			text = getDriver().findElement(By.xpath(xpath)).getText();
		}
		catch(NoSuchElementException e)
		{
			text = "No element Found for "+ElementName;
		}
		
		return ElementName +"---> "+text;
		
	}
	
	public String getSynopsis_StoreInList(String xPath)
	{
		String text = "";
		try
		{
			Thread.sleep(500);
			//waitForApperance(locateElement(Locators.XPATH,xPath));
			List<WebElement> lstAllElements = locateElements(Locators.XPATH,xPath);
			int count = lstAllElements.size();
			
			
			
			for(int i=0;i<count;i++)
			{
				text = lstAllElements.get(i).getText()+" ; "+text;
			}
			
			
		}
		catch(NoSuchElementException e)
		{
			text = "No element Found - Synopsis";
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Synopsis ---> "+text;
		
	}
	
	
	public String getFactbox_StoreInList(String xPathColumnLeft, String xPathColumnRight)
	{
		String text = "";
		try
		{
			WebDriverWait wait = new WebDriverWait(getDriver(),Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath(xPathColumnLeft))));
			List<WebElement> lstAllElementsColumnLeft = getDriver().findElements(By.xpath(xPathColumnLeft));
			List<WebElement> lstAllElementsColumnRight = getDriver().findElements(By.xpath(xPathColumnRight));
			int count = lstAllElementsColumnLeft.size();
			
			
			
			for(int i=0;i<count;i++)
			{
				text += lstAllElementsColumnLeft.get(i).getText() + " = " + lstAllElementsColumnRight.get(i).getText() + "\n";
			}
			
			
		}
		catch(NoSuchElementException e)
		{
			text = "No element Found - Factbox";
		}
		
		return text;
		
	}
	
	public String getImagesLink_StoreInList(String xPath)
	{
		String text = "";
		try
		{
			Thread.sleep(500);
			
			List<WebElement> lstAllElements = getDriver().findElements(By.xpath(xPath));
		
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
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Image Links ---> "+text;
		
	}

	@Override
	public String getBackgroundColor(WebElement ele) {
		String cssValue = null;
		try {
			cssValue = ele.getCssValue("color");
			reportStep("The background color is " + cssValue, "info");
		} catch (WebDriverException e) {
			reportStep("Not able to get the background color \n" + e.getMessage(), "fail");
		} catch (Exception e) {
			reportStep("Not able to get the background color \n" + e.getMessage(), "fail");
		}
		return cssValue;
	}

	@Override
	public String getTypedText(WebElement ele) {
		String attributeValue = null;
		try {
			attributeValue = ele.getAttribute("value");
			reportStep("The attribute value is " + attributeValue, "info");
		} catch (WebDriverException e) {
			reportStep("Not able to find attribute value \n" + e.getMessage(), "fail");
		}
		return attributeValue;
	}

	@Override
	public void selectDropDownUsingText(WebElement ele, String value) {
		try {
			Select sel = new Select(ele);
			sel.selectByVisibleText(value);
		} catch (WebDriverException e) {
			reportStep("Not able to select the drop down with text \n" + value, "fail");
		}
	}

	@Override
	public void selectDropDownUsingIndex(WebElement ele, int index) {
		try {
			Select sel = new Select(ele);
			sel.selectByIndex(index);
		} catch (WebDriverException e) {
			reportStep("Not able to select the drop down with index " + index + " \n" + e.getMessage(), "fail");
		}
	}

	@Override
	public void selectDropDownUsingValue(WebElement ele, String value) {
		try {
			Select sel = new Select(ele);
			sel.selectByValue(value);
		} catch (WebDriverException e) {
			reportStep("Not able to select the drop down with value " + value + " \n" + e.getMessage(), "fail");
		}
	}

	@Override
	public boolean verifyExactText(WebElement ele, String expectedText) {
		try {
			String text = ele.getText();
			if (text.contains(expectedText)) {
				return true;
			} else {
				reportStep("The expected text " + text + "doesn't equals to the  " + expectedText, "warning");
			}
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while verifying the Text \n" + e.getMessage(), "fail");
		}

		return false;
	}

	@Override
	public boolean verifyPartialText(WebElement ele, String expectedText) {
		try {
			if (ele.getText().contains(expectedText)) {
				return true;
			} else {
				reportStep("The expected text doesn't contain the actual " + expectedText, "warning");
			}
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while verifying the Text \n" + e.getMessage(), "fail");
		}
		return false;
	}

	@Override
	public boolean verifyExactAttribute(WebElement ele, String attribute, String value) {
		try {
			if (ele.getAttribute(attribute).equals(value)) {
				return true;
			} else {
				reportStep("The expected attribute :" + attribute + " value does not contains the actual " + value,
						"warning");
			}
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while verifying the Attribute Text \n" + e.getMessage(), "fail");
		}
		return false;
	}

	@Override
	public void verifyPartialAttribute(WebElement ele, String attribute, String value) {
		try {
			if (ele.getAttribute(attribute).contains(value)) {
				reportStep("The expected attribute :" + attribute + " value contains the actual " + value, "pass");
			} else {
				reportStep("The expected attribute :" + attribute + " value does not contains the actual " + value,
						"warning");
			}
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while verifying the Attribute Text \n" + e.getMessage(), "fail");
		}

	}

	@Override
	public boolean verifyDisplayed(WebElement ele) {
		try {
			if (ele.isDisplayed()) {
				return true;
			} else {
				reportStep("The element " + ele + " is not visible", "warnings");
			}
		} catch (WebDriverException e) {
			reportStep("WebDriverException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	@Override
	public boolean verifyDisappeared(WebElement ele) {
		try {
			Boolean until = getWait().until(ExpectedConditions.invisibilityOf(ele));
			reportStep("Waited for an element to disappear", "info");
			return until;
		} catch (org.openqa.selenium.TimeoutException e) {
			reportStep("Element not disappeared \n" + e.getMessage(), "fail");
		} catch (Exception e) {
			reportStep("Element not disappeared \n" + e.getMessage(), "fail");
		}
		return false;

	}

	@Override
	public boolean verifyEnabled(WebElement ele) {
		try {
			if (ele.isEnabled()) {
				return true;
			} else {
				reportStep("The element " + ele + " is not Enabled", "warning");
			}
		} catch (WebDriverException e) {
			reportStep("WebDriverException : \n" + e.getMessage(), "fail");
		}
		return false;
	}

	@Override
	public boolean verifySelected(WebElement ele) {
		try {
			if (ele.isSelected()) {
				return true;
			} else {
				reportStep("The element " + ele + " is not selected", "warning");
			}
		} catch (WebDriverException e) {
			reportStep("WebDriverException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	@Override
	public void startApp(String url, boolean headless) {
		try {
			setDriver("chrome", headless);
			setWait();
			act = new Actions(getDriver());
			getDriver().manage().window().maximize();
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(90));
			getDriver().get(url);
			reportStep("The Browser Launched in chrome browser with URL " + url, "pass");
		} catch (Exception e) {
			reportStep("Something went wrong \n" + e.getMessage(), "fail");
		}

	}

	@Override
	public void startApp(String browser, boolean headless, String url) {
		try {
			if (browser.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.silentOutput", "true");
				setDriver("chrome", headless);
			} else if (browser.equalsIgnoreCase("firefox")) {
				setDriver("firefox", headless);
			} else if (browser.equalsIgnoreCase("msedge")) {
				setDriver("msedge", headless);
			}
			setWait();
			getDriver().manage().window().maximize();
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			//getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
			getDriver().get(url);
		} catch (WebDriverException e) {
			e.printStackTrace();
			//reportStep("The Browser Could not be Launched. Hence Failed \n" + e.getMessage(), "fail");
		} catch (Exception e) {
			e.printStackTrace();
			//reportStep("The Browser Could not be Launched. Hence Failed \n" + e.getMessage(), "fail");
		}
	}

	@Override
	public WebElement locateElement(Locators locatorType, String value) {
		try {
			switch (locatorType) {
			case CLASS_NAME:
				return getDriver().findElement(By.className(value));
			case CSS:
				return getDriver().findElement(By.cssSelector(value));
			case ID:
				return getDriver().findElement(By.id(value));
			case LINK_TEXT:
				return getDriver().findElement(By.linkText(value));
			case NAME:
				return getDriver().findElement(By.name(value));
			case PARTIAL_LINKTEXT:
				return getDriver().findElement(By.partialLinkText(value));
			case TAGNAME:
				return getDriver().findElement(By.tagName(value));
			case XPATH:
				return getDriver().findElement(By.xpath(value));
			default:
				System.err.println("Locator is not Valid");
				break;
			}
		} catch (NoSuchElementException e) {
			reportStep("The Element with locator:" + locatorType + " Not Found with value: " + value + "\n"
					+ e.getMessage(), "fail");
		} catch (Exception e) {
			reportStep("The Element with locator:" + locatorType + " Not Found with value: " + value + "\n"
					+ e.getMessage(), "fail");
		}
		return null;
	}

	@Override
	public WebElement locateElement(String value) {
		try {
			WebElement findElementById = getDriver().findElement(By.id(value));
			return findElementById;
		} catch (NoSuchElementException e) {
			reportStep("The Element with locator id Not Found with value: " + value + "\n" + e.getMessage(), "fail");
		} catch (Exception e) {
			reportStep("The Element with locator id Not Found with value: " + value + "\n" + e.getMessage(), "fail");
		}
		return null;
	}

	@Override
	public List<WebElement> locateElements(Locators type, String value) {
		try {
			switch (type) {
			case CLASS_NAME:
				return getDriver().findElements(By.className(value));
			case CSS:
				return getDriver().findElements(By.cssSelector(value));
			case ID:
				return getDriver().findElements(By.id(value));
			case LINK_TEXT:
				return getDriver().findElements(By.linkText(value));
			case NAME:
				return getDriver().findElements(By.name(value));
			case PARTIAL_LINKTEXT:
				return getDriver().findElements(By.partialLinkText(value));
			case TAGNAME:
				return getDriver().findElements(By.tagName(value));
			case XPATH:
				return getDriver().findElements(By.xpath(value));
			default:
				System.err.println("Locator is not Valid");
				break;
			}
		} catch (NoSuchElementException e) {
			reportStep("The Element with locator:" + type + " Not Found with value: " + value + "\n" + e.getMessage(),
					"fail");
		}
		return null;
	}

	@Override
	public void switchToAlert() {
		try {
			getDriver().switchTo().alert();
			reportStep("Focus has been switched to Alert", "info", false);
		} catch (NoAlertPresentException e) {
			reportStep("There is no alert present.", "fail", false);
		} catch (WebDriverException e) {
			reportStep("WebDriverException : " + e.getMessage(), "fail", false);
		}
	}

	@Override
	public void acceptAlert() {
		String text = "";
		try {
			getWait().until(ExpectedConditions.alertIsPresent());
			Alert alert = getDriver().switchTo().alert();
			text = alert.getText();
			alert.accept();
			reportStep("The alert " + text + " is accepted.", "pass", false);
		} catch (NoAlertPresentException e) {
			reportStep("There is no alert present.", "fail", false);
		} catch (WebDriverException e) {
			reportStep("WebDriverException : " + e.getMessage(), "fail", false);
		}

	}

	@Override
	public void dismissAlert() {
		String text = "";
		try {
			Alert alert = getDriver().switchTo().alert();
			text = alert.getText();
			alert.dismiss();
			reportStep("The alert " + text + " is accepted.", "pass", false);
		} catch (NoAlertPresentException e) {
			reportStep("There is no alert present.", "pass", false);
		} catch (WebDriverException e) {
			reportStep("WebDriverException : " + e.getMessage(), "fail", false);
		}

	}

	@Override
	public String getAlertText() {
		String text = "";
		try {
			Alert alert = getDriver().switchTo().alert();
			text = alert.getText();
			reportStep("The alert text is " + text, "pass", false);
		} catch (NoAlertPresentException e) {
			reportStep("There is no alert present.", "fail", false);
		} catch (WebDriverException e) {
			reportStep("WebDriverException : \n" + e.getMessage(), "fail", false);
		}
		return text;
	}

	@Override
	public void typeAlert(String data) {
		try {
			getDriver().switchTo().alert().sendKeys(data);
		} catch (NoAlertPresentException e) {
			reportStep("There is no alert present.", "fail", false);
		} catch (WebDriverException e) {
			reportStep("WebDriverException : \n" + e.getMessage(), "fail", false);
		}
	}

	@Override
	public void switchToWindow(int index) {
		try {
			Set<String> allWindows = getDriver().getWindowHandles();
			List<String> allhandles = new ArrayList<String>(allWindows);
			getDriver().switchTo().window(allhandles.get(index));
			reportStep("The Window With index: " + index + " switched successfully", "info", false);
			reportStep(getDriver().getTitle(), "info");
		} catch (NoSuchWindowException e) {
			reportStep("The Window With index: " + index + " not found\n" + e.getMessage(), "fail", false);
		} catch (Exception e) {
			reportStep("The Window With index: " + index + " not found\n" + e.getMessage(), "fail", false);
		}
	}

	@Override
	public boolean switchToWindow(String title) {
		try {
			Set<String> allWindows = getDriver().getWindowHandles();
			for (String eachWindow : allWindows) {
				getDriver().switchTo().window(eachWindow);
				if (getDriver().getTitle().equals(title)) {
					break;
				}
			}
			reportStep("The Window With Title: " + title + "is switched ", "info");
			return true;
		} catch (NoSuchWindowException e) {
			reportStep("The Window With Title: " + title + " not found", "fail", false);
		}
		return false;
	}

	@Override
	public void switchToFrame(int index) {
		try {
			Thread.sleep(100);
			getDriver().switchTo().frame(index);
		} catch (NoSuchFrameException e) {
			reportStep("No such frame " + e.getMessage(), "warning", false);
		} catch (Exception e) {
			reportStep("No such frame " + e.getMessage(), "fail", false);
		}

	}

	@Override
	public void switchToFrame(WebElement ele) {
		try {
			getDriver().switchTo().frame(ele);
		} catch (NoSuchFrameException e) {
			reportStep("No such frame " + e.getMessage(), "fail", false);
		} catch (Exception e) {
			reportStep("No such frame " + e.getMessage(), "fail", false);
		}

	}

	public void switchToFrameUsingXPath(String xpath) {
		try {
			getDriver().switchTo().frame(locateElement(Locators.XPATH, xpath));
		} catch (NoSuchFrameException e) {
			// reportStep("No such frame " + e.getMessage(), "warning", false);
		} catch (Exception e) {
			// reportStep("No such frame " + e.getMessage(), "fail", false);
		}

	}

	@Override
	public void switchToFrame(String idOrName) {
		try {
			getDriver().switchTo().frame(idOrName);
		} catch (NoSuchFrameException e) {
			reportStep("No such frame " + e.getMessage(), "fail", false);
		} catch (Exception e) {
			reportStep("No such frame " + e.getMessage(), "fail", false);
		}
	}

	@Override
	public void defaultContent() {
		try {
			getDriver().switchTo().defaultContent();
		} catch (Exception e) {
			reportStep("No such window " + e.getMessage(), "fail", false);
		}
	}

	@Override
	public boolean verifyUrl(String url) {
		if (getDriver().getCurrentUrl().equals(url)) {
			reportStep("The url: " + url + " matched successfully", "info");
			return true;
		} else {
			reportStep("The url: " + url + " not matched", "fail");
		}
		return false;
	}

	@Override
	public boolean verifyTitle(String title) {
		if (getDriver().getTitle().equals(title)) {
			reportStep("Page title: " + title + " matched successfully", "info");
			return true;
		} else {
			reportStep("Page url: " + title + " not matched", "fail");

		}
		return false;
	}

	@Override
	public long takeSnap() {
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L;
		try {
			FileUtils.copyFile(getDriver().getScreenshotAs(OutputType.FILE),
					new File("./" + ExtentReporter.folderName + "/images/" + number + ".jpg"));
		} catch (WebDriverException e) {
			reportStep("The browser has been closed." + e.getMessage(), "fail");
		} catch (IOException e) {
			reportStep("The snapshot could not be taken " + e.getMessage(), "warning");
		}
		return number;
	}

	@Override
	public void close() {
		try {
			getDriver().close();
			reportStep("Browser is closed", "info", false);
		} catch (Exception e) {
			reportStep("Browser cannot be closed " + e.getMessage(), "fail", false);
		}
	}

	@Override
	public void quit() {
		try {
			getDriver().quit();
			reportStep("Browser is closed", "info", false);
		} catch (Exception e) {
			reportStep("Browser cannot be closed " + e.getMessage(), "fail", false);
		}
	}

	public void waitForDisapperance(WebElement element) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
			wait.until(ExpectedConditions.invisibilityOf(element));
		} catch (Exception e) {
			reportStep("Element did not appear after 10 seconds", "fail", false);

		}

	}

	public void pause(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void chooseDate(WebElement ele, String data) {
		try {
			getDriver().executeScript("arguments[0].setAttribute('value', '" + data + "')", ele);
			reportStep("The Data :" + data + " entered Successfully", "pass");
		} catch (ElementNotInteractableException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		}

	}

	public void fileUpload(WebElement ele, String data) {
		try {
			hoverAndClick(ele);
			pause(2000);

			// Store the copied text in the clipboard
			StringSelection stringSelection = new StringSelection(data);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

			// Paste it using Robot class
			Robot robot = new Robot();

			// Enter to confirm it is uploaded
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);

			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);

			Thread.sleep(5000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			reportStep("The file is selected Successfully", "pass");
		} catch (Exception e) {
			reportStep("The file is not selected Successfully", "fail");
		}

	}

	@Override
	public void executeTheScript(String js, WebElement ele) {
		getDriver().executeScript(js, ele);
	}

	public static void generateProjectAndAuctionIds() {
		int ranNum1 = (int) (Math.random() * 10000);
		int ranNum2 = ranNum1 + 9999;
		projectId = Integer.toString(ranNum1);
		auctionRef = Integer.toString(ranNum2);
	}

	public void fileUploadWithJs(WebElement ele, String data) {
		try {

			clickUsingJs(ele);
			;
			pause(2000);

			// Store the copied text in the clipboard
			StringSelection stringSelection = new StringSelection(data);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

			// Paste it using Robot class
			Robot robot = new Robot();

			// Enter to confirm it is uploaded
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);

			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);

			Thread.sleep(5000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			reportStep("The file is selected Successfully", "pass");
		} catch (Exception e) {
			reportStep("The file is not selected Successfully", "fail");
		}

	}

	// QATasks Functions:
	// --------------------
	public int getTotalCardCounts(String xpath) {

		int CardCount = -1;
		try {

			CardCount = locateElements(Locators.XPATH, xpath).size();

		} catch (Exception e) {
			reportStep("Exception when finding the Total Molecule counts", "fail");
		}

		return CardCount;

	}

	public void scrollByPixel(int pixel) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getDriver().executeScript("window.scrollBy(0, 300);");
	}

	public void scrollToBottomOfPage() {
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		getDriver().executeScript("window.scrollTo(0, document.body.scrollHeight);");
	}
	
	public void scrollToLastElement(String xpath)
	{
		//List<WebElement> lstAllProductList = locateElements(Locators.XPATH, xpath);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<WebElement> lstAllProductList = locateElements(Locators.XPATH, xpath);
		getDriver().executeScript("arguments[0].scrollIntoView();", locateElements(Locators.XPATH, xpath).get(lstAllProductList.size()-1));
		

	}
	
	public void scrollToSpecifiedElement(String xpath)
	{
		String text = "";
		try {
			Thread.sleep(500);
			getDriver().executeScript("arguments[0].scrollIntoView();", locateElement(Locators.XPATH, xpath));
			
			text = locateElements(Locators.XPATH, xpath).get(0).getText();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (StaleElementReferenceException e) {
			reportStep("The Element with " + xpath + " could not be scrolled into due to Stale element -> " + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			reportStep("The Element with " + xpath + " could not be scrolled into due to Webdriver exception -> " + e.getMessage(), "fail");
		} catch (Exception e) {
			reportStep("The Element with " + xpath + " could not be scroll into due to unknown Exception ->  " + e.getMessage(), "fail");
		}
	}

	public int periodicallyCheck_For_NewCardLoads(String allCards_Xpath) {
		
		int currentCardCount=-1;
		long maxWaitTimeInSeconds = 1000;
		long startTime = getCurrentTimeInMS();
		System.out.println("Started at Time (in Ms):  " + startTime);
		
		try {
			

			while (true) {
				//> get previous card count
				int previousCardCount = locateElements(Locators.XPATH,allCards_Xpath).size();
	
				//> scroll by 300 pixel
				scrollByPixel(200);
				
		
				//> scroll to Bottom of page
				scrollToBottomOfPage();
				
				//> scroll to (as of now) last product list
				//scrollToLastElement(allCards_Xpath);
				
				List<WebElement> lstAllProductList = locateElements(Locators.XPATH,allCards_Xpath);
	            
	            Thread.sleep(1000);
	            getDriver().executeScript("arguments[0].scrollIntoView();", locateElements(Locators.XPATH,allCards_Xpath).get(lstAllProductList.size()-1));
				
	            
	          //1> condition checks - if it reaches at end-> break loop
				if(locateElements(Locators.XPATH,"//button[@class='ais-InfiniteHits-loadMore ais-InfiniteHits-loadMore--disabled']").size()>0)
				{
					currentCardCount = locateElements(Locators.XPATH,allCards_Xpath).size();
					System.out.println("Loop stopped as it comes to the last element ais-InfiniteHits-loadMore ais-InfiniteHits-loadMore--disabled ");
					break;
					
				}
				
				//> get current card count
				currentCardCount = locateElements(Locators.XPATH,allCards_Xpath).size();
				
				
				//1> condition check - if prev ==current card -> break loop
				if (previousCardCount == currentCardCount)
				{
					System.out.println("Loop stopped as prev count = current count");
					break;
				}
		            
				
				//2> condition check - currentTime - startTime >= max wait time. ie - > maximum wait time has been exceeded
				long currentTime = getCurrentTimeInMS();
	            long timeDiff = currentTime - startTime;
	            System.out.println("Current Time - StartTime (in Ms) : "+timeDiff);
	            if ((currentTime - startTime) >= (maxWaitTimeInSeconds * 1000)) 
	            {
					System.out.println("Loop stopped as current time - start time exceeded");
					break;
				}
	               
	            
	            
	          
	            
	            Thread.sleep(1000);
				
			
			}
		
		}
		catch(InterruptedException e)
		{
			e.getStackTrace();
		}
		
		return currentCardCount;
	}
	
	public long getCurrentTimeInMS() {
		return System.currentTimeMillis();

	}
	
	
	public List<String> getAllAlternativesText(String xPath)
	{
		List<String> lstAllText = new ArrayList();
		for (int i = 0; i < locateElements(Locators.XPATH,xPath).size(); i++) {
			String text = locateElements(Locators.XPATH,xPath).get(i).getAttribute("title");
			lstAllText.add(text);

		}
		
		return lstAllText;
	}
	
	
	
	public Map<String,Map<String,List<String>>> getEachAlternativesText_StoreInMap(Map<String,List<String>> map)
	{
		
		Map<String,Map<String,List<String>>> mapWholeDetails = new LinkedHashMap();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<String> values = entry.getValue();

			Map<String,List<String>> mapAllDetails = new LinkedHashMap();

			
			for (int i = 0; i < values.size(); i++) {
				List<String> lst = new ArrayList();
				try {
					getDriver().findElement(By.xpath("//input[@id='search']")).sendKeys(values.get(i), Keys.ENTER);
					
					Thread.sleep(1000);
					getWait().until(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath("//div[@id='algolia_hits']//img[@class='product-image-photo']"))));
					String productName = values.get(i).trim().replace("'s", "").replace("'S", "");
					
					String xpathExpression = "//div[@id='algolia_hits']//img[@class='product-image-photo' and contains(@alt,'"+productName+"')]";
					
					getDriver().findElement(By.xpath(xpathExpression)).click();
					
					getWait().until(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath("//div[@class='essentials']//span[@class='final-price']"))));
					
					String xpath_Mrp = "//div[@class='essentials']//span[@class='final-price']";
					String MRPFetchedText = getElementText(xpath_Mrp,"MRP");
					lst.add(MRPFetchedText);
					
					String xpath_BacterialInfections= "//div[@class='product-detail']//span[@class='gen_drug ellipsis']";
					String BacterialInfectionsFetchedText = getElementText(xpath_BacterialInfections,"Bacterial Infections");
					lst.add(BacterialInfectionsFetchedText);
					
					String xpath_RxRequired = "//div[@class='product-detail']//span[@class='req_Rx']";
					String RxRequiredFetchedText = getElementText(xpath_RxRequired,"RxRequired");
					lst.add(RxRequiredFetchedText);
					
					String xpath_MKT = "//div[@class='essentials']//span[@class='drug-manu']/a";
					String MKTFetchedText = getElementText(xpath_MKT,"MKT");
					lst.add(MKTFetchedText);
					
					String xpath_CoutryOfOrigin = "//div[@class='essentials']//span[@class='drug-manu ellipsis origin_text']";
					String CountryOfOriginText = getElementText(xpath_CoutryOfOrigin,"Country Of origin");
					lst.add(CountryOfOriginText);
					
					String xpath_GenericName = "//div[@id='choose-generic-substitutes-block']//div[@class='drug-conf']";
					String GenericNameText = getElementText(xpath_GenericName,"Generic Name");
					lst.add(GenericNameText);
					
					String xpath_Synopsis = "//h2[text()='SYNOPSIS']/..//tr";
					//scrollToSpecifiedElement(xpath_Synopsis);
					String synopsis = getSynopsis_StoreInList(xpath_Synopsis);
					lst.add(synopsis);
					
					
					String xpath_ImageLink = "//div[@class='main-container']//figure/img";
					//scrollToSpecifiedElement(xpath_ImageLink);
					String imageLinks = getImagesLink_StoreInList(xpath_ImageLink);
					lst.add(imageLinks);
				
				}
				catch(NoSuchElementException e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage()+" Error in fetching element - "+values.get(i));
					lst.add(" Error in fetching element - "+values.get(i));
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mapAllDetails.put(values.get(i), lst);
				
			}
			
			mapWholeDetails.put(key, mapAllDetails);
			
			
		}
		
		return mapWholeDetails;
	}
	
	
	
	//One MG:
	
	
	public List<String> OneMG_storeAllAlternativesText_TillNextButton_IsDisabled(String xpath)
	{
		
		List<String> lstAllAlternativesText=new ArrayList();
		String eachAlternativesText =null;
		try
		{
			while(true)
			{
				Thread.sleep(1000);
				WebDriverWait wait = new WebDriverWait(getDriver(),Duration.ofSeconds(20));
				wait.until(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath(xpath))));
							
				List<WebElement> lstAll_Alternatives = getDriver().findElements(By.xpath(xpath));
				
				for(int i=0;i<lstAll_Alternatives.size();i++)
				{
					 eachAlternativesText = lstAll_Alternatives.get(i).getText();
					lstAllAlternativesText.add(eachAlternativesText);
				}
				
				String xpath_NextButton = "//div[contains(@class,'style__paginate-container')]//span[contains(@class,'style__next')]";
				boolean nextButtonDisabledAttributeFlag = getDriver().findElement(By.xpath(xpath_NextButton)).getAttribute("class").contains("disabled");
				if(nextButtonDisabledAttributeFlag==false)
					getDriver().findElement(By.xpath(xpath_NextButton)).click();
				else
					break;
				
			}
		}
		catch (NoSuchElementException e) {
			reportStep("The Element with locator: " + xpath + " Not Found" , "fail");
		} catch (Exception e) {
			reportStep("The Element with locator: " + xpath + " Not Found as there is Generic Exception" , "fail");
				
		}
		
		
		return lstAllAlternativesText;
				
		
	}
	
	
	public Map<String,Map<String,List<String>>> OneMG_GetEachAlternativesText_StoreInMap(Map<String,List<String>> map)
	{
		
		Map<String,Map<String,List<String>>> mapWholeDetails = new LinkedHashMap();
		
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
		
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<String> values = entry.getValue();

			Map<String,List<String>> mapAllDetails = new LinkedHashMap();

			// Write list values to subsequent columns
			
			for (int i = 0; i < values.size(); i++) {
				List<String> lst = new ArrayList();
				String originalWindowHandle = getDriver().getWindowHandle();
				try {
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath("//input[@id='srchBarShwInfo']"))));
					
					String eachAlternatives = values.get(i);
				
					getDriver().findElement(By.xpath("//input[@id='srchBarShwInfo']")).sendKeys(eachAlternatives, Keys.ENTER);
					
					String xpath_ProductTitle = "//div[contains(@class,'row style__grid-container')]//a//span[contains(@class,'style__pro-title')]";
					
					wait.until(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath(xpath_ProductTitle))));
								
					//Mrp:
					String xpath_PriceTag = "//div[contains(@class,'style__price-tag___')]";
					String MRPFetchedText = getElementText(xpath_PriceTag,"MRP");
					lst.add(MRPFetchedText);
					
					
					String productName = values.get(i).trim();
					String xpathExpression = "//div[contains(@class,'row style__grid-container')]//a//span[contains(@class,'style__pro-title') and contains(normalize-space(),'"+productName+"')]";
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath(xpathExpression))));
					wait.until(ExpectedConditions.elementToBeClickable(getDriver().findElement(By.xpath(xpathExpression))));
					getDriver().findElement(By.xpath(xpathExpression)).click();
				
					Set<String> windowHandles = getDriver().getWindowHandles();
					
					List<String> lstWindowHandles = new ArrayList(windowHandles);
					
					getDriver().switchTo().window(lstWindowHandles.get(1));
					
					String xpath_DrugHeader = "//div[@id='drug_header']";
					Thread.sleep(500);
					wait.until(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath(xpath_DrugHeader))));
					
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
					getDriver().findElement(By.xpath(xpath_UsesAndBenefits)).click();
					String xpathUses = "//div[@id='uses_and_benefits']//h2[contains(text(),'Uses of')]/..";
					wait.until(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath(xpathUses))));
					
					String Uses_FetchedText = getElementText(xpathUses,"Uses");
					lst.add(Uses_FetchedText);
					
					//Fact box:
					String xpath_FactBox = "//div[@id='scroll-bar']//ul/a/span[text()='Fact box']";
					getDriver().findElement(By.xpath(xpath_FactBox)).click();
					Thread.sleep(400);
					String xpath_Left = "//div[@id='fact_box']//div[contains(@class,'DrugFactBox__col-left')]";
					String xpath_Right = "//div[@id='fact_box']//div[contains(@class,'DrugFactBox__col-right')]";
					wait.until(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath(xpath_Left))));
					String FactBox_FetchedText = getFactbox_StoreInList(xpath_Left,xpath_Right);
					lst.add(FactBox_FetchedText);
					
					getDriver().close();
					
					Thread.sleep(1000);
										
					getDriver().switchTo().window(originalWindowHandle);
				
				}
				catch(NoSuchElementException e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage()+" Element Not found - "+values.get(i));
					lst.add(e.getMessage()+" Element not found when fetching element - "+values.get(i));
					
					reportStep(e.getMessage()+" Element not found when fetching element - "+values.get(i),"fail");
					
					if(!getDriver().getWindowHandle().equals(originalWindowHandle))
					{
						getDriver().close();
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
											
						getDriver().switchTo().window(originalWindowHandle);
					}
					
				}
				catch(ElementClickInterceptedException e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage()+" ElementClickInterceptedException - "+values.get(i));
					lst.add(e.getMessage()+" ElementClickInterceptedException when fetching element - "+values.get(i));
					
					reportStep(e.getMessage()+" ElementClickInterceptedException when fetching element - "+values.get(i),"fail");
					
					if(!getDriver().getWindowHandle().equals(originalWindowHandle))
					{
						getDriver().close();
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}					
						getDriver().switchTo().window(originalWindowHandle);
					}
					
					
					
				}
				catch(WebDriverException e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage()+" WebDriverException - "+values.get(i));
					lst.add(" WebDriverException when fetching element - "+values.get(i));
					
					reportStep(e.getMessage()+" WebDriverException when fetching element - "+values.get(i),"fail");
					
					if(!getDriver().getWindowHandle().equals(originalWindowHandle))
					{
						getDriver().close();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}					
						getDriver().switchTo().window(originalWindowHandle);
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mapAllDetails.put(values.get(i), lst);
				
			}
			
			mapWholeDetails.put(key, mapAllDetails);
			
			
		}
		return mapWholeDetails;
	}
	
	
}
