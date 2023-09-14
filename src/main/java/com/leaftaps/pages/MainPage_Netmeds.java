package com.leaftaps.pages;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.framework.selenium.api.design.Locators;
import com.framework.testng.api.base.ProjectHooks;

import cucumber.api.java.en.And;

public class MainPage_Netmeds extends ProjectHooks{
	
	String searchForMedicinesInputbox = "//input[@id='search']";
	
	String allCards = "//div[@class='product-list']//li";
	
	public MainPage_Netmeds enterMoleculeName_PressEnter(String molecule) {
		clearAndType(locateElement(Locators.XPATH, "searchForMedicinesInputbox"), molecule);
		reportStep(molecule+" entered successfully","pass");
		return this;
	}
	
	
	public MainPage_Netmeds scrollTillLastMoleculeResults(String allCards) {
		
		int initialCardCount = getTotalCardCounts(allCards);
		
		 // Define a maximum timeout (e.g., 30 seconds) for waiting for new cards to load
        long maxWaitTimeInSeconds = 1000;
        long startTime = System.currentTimeMillis();
        
       //To be updated
		return this;
	}
	 
	@And("Click Login button")
	public HomePage clickLogin() {
		click(locateElement(Locators.CLASS_NAME, "decorativeSubmit"));
		reportStep("Login button clicked successfully", "pass");
		return new HomePage();
	}

}
