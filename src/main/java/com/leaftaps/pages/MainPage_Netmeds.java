package com.leaftaps.pages;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;

import com.framework.selenium.api.design.Locators;
import com.framework.testng.api.base.ProjectHooks;

import cucumber.api.java.en.And;

public class MainPage_Netmeds extends ProjectHooks{
	
	String searchForMedicinesInputbox = "//input[@id='search']";
	
	String xpath_AllCards = "//div[@class='product-list']//li";
	
	String xpath_AllAlternateText ="//div[@id='algolia_hits']//a[@class='category_name']";
	
	Map<String,List<String>> map_Molecule_AlternateText = new LinkedHashMap();
	
	Map<String,Map<String,List<String>>> mapWholeDetails_Molecule_MoleculeAlternativesText = new LinkedHashMap();
	
	
	public MainPage_Netmeds enterMoleculeName_PressEnter(String molecule) {
		
		typeAndEnter(locateElement(Locators.XPATH, searchForMedicinesInputbox), molecule);
		reportStep(molecule+" entered successfully","pass");
		return this;
	}
	
	
	public MainPage_Netmeds scrollTillLastMoleculeResults() {
		
		int totalAlternatives = periodicallyCheck_For_NewCardLoads(xpath_AllCards);
		
		reportStep("Successfully scrolled till last element. Total Alternatives - "+totalAlternatives,"pass");
		return this;
	}
	
	
	public MainPage_Netmeds getAll_Alternatives_Of_Molecule_StoreInMap(String molecule)
	{
		List<String> lstAllAlternativesText = getAllAlternativesText(xpath_AllAlternateText);
		
		map_Molecule_AlternateText.put(molecule, lstAllAlternativesText);
		
		reportStep("Successfully stored all Alternatives for Molecule "+molecule+"in a Map ","pass");
		
		System.out.println(map_Molecule_AlternateText);
		
		return this;
	}
	 
	public MainPage_Netmeds get_AlternativesDetails_StoreInMap()
	{
		mapWholeDetails_Molecule_MoleculeAlternativesText = getEachAlternativesText_StoreInMap(map_Molecule_AlternateText);
		
		System.out.println(mapWholeDetails_Molecule_MoleculeAlternativesText);
		
		reportStep("Successfully stored all details in a Map ","pass");
		
		return this;
	}

}
