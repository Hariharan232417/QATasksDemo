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

public class OneMG_MainPage extends ProjectHooks{
	
	String closeBanner = "//*[contains(@class,'style__close-icon')]";
	
	String searchForMedicinesInputbox = "//input[@id='srchBarShwInfo']";

	String xpath_AllCards = "//div[contains(@class,'row style__grid-container')]//a//span[contains(@class,'style__pro-title')]";
	
	
	Map<String,List<String>> map_Molecule_AlternateText = new LinkedHashMap();
	
	Map<String,Map<String,List<String>>> mapWholeDetails_Molecule_MoleculeAlternativesText = new LinkedHashMap();
	
	public OneMG_MainPage closeBanner() {
		
		click(locateElement(Locators.XPATH, closeBanner));
		reportStep("Banner closed successfully","pass");
		return this;
	}
	
	
	public OneMG_MainPage enterMoleculeName_PressEnter(String molecule) {
		
		typeAndEnter(locateElement(Locators.XPATH, searchForMedicinesInputbox), molecule);
		reportStep(molecule+" entered successfully","pass");
		return this;
	}
	
	
	public OneMG_MainPage getAll_Alternatives_Of_Molecule_StoreInMap(String molecule) {
		
		List<String> lst_AllAlternativesText = OneMG_storeAllAlternativesText_TillNextButton_IsDisabled(xpath_AllCards);
		
		int totalAlternatives = lst_AllAlternativesText.size();
		
		reportStep("Successfully scrolled till last Page. Total Alternatives - "+totalAlternatives,"pass");
		
		map_Molecule_AlternateText.put(molecule, lst_AllAlternativesText);
		
		reportStep("Successfully stored all Alternatives for Molecule "+molecule+"in a Map ","pass");
		
		System.out.println(map_Molecule_AlternateText);
		return this;
	}
	
	 
	public OneMG_MainPage get_AlternativesDetails_StoreInMap(String molecule)
	{
		mapWholeDetails_Molecule_MoleculeAlternativesText = OneMG_GetEachAlternativesText_StoreInMap(map_Molecule_AlternateText);
		
		System.out.println(mapWholeDetails_Molecule_MoleculeAlternativesText);
		
		reportStep("Successfully stored all details in a Map ","pass");
		
		writeAllResults_Excel(molecule, mapWholeDetails_Molecule_MoleculeAlternativesText);
		
		reportStep("Successfully written all datas to Excel by reading Map ","pass");
		
		return this;
	}

	
	public void writeAllResults_Excel(String molecule, Map<String, Map<String, List<String>>> map) {
		
		String moleculeFileName_RemoveSlash=molecule;
		if(molecule.contains("/"))
		{
			 moleculeFileName_RemoveSlash = molecule.replace("/", "_");
		}
		

		String fileName = "./Data/AllResults_"+moleculeFileName_RemoveSlash+".xlsx";
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
}
