package com.leaftaps.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
		
		writeAllResults_Excel(mapWholeDetails_Molecule_MoleculeAlternativesText);
		
		reportStep("Successfully written all datas to Excel by reading Map ","pass");
		
		return this;
	}

	
	public void writeAllResults_Excel(Map<String, Map<String, List<String>>> map) {

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
        row.createCell(2).setCellValue("Other details - MRP");
        row.createCell(3).setCellValue("Bacterial Infections");
        row.createCell(4).setCellValue("RxRequired");
        row.createCell(5).setCellValue("MKT");
        row.createCell(6).setCellValue("Country Of Origin");
        row.createCell(7).setCellValue("Generic Name");
        row.createCell(8).setCellValue("Synopsis");
        row.createCell(9).setCellValue("Image Links");
        
        
		
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
}
