package com.leaftaps.testcases;

import java.util.HashMap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.testng.api.base.ProjectHooks;
import com.leaftaps.pages.MainPage_Netmeds;

public class ReadAndWrite extends ProjectHooks{
	@BeforeTest
	public void setValues() {
		testcaseName = "ExtractDatas";
		testDescription ="Extract Data from App and store it in Excel";
		authors="Hari";
		category ="Extract Datas";
		excelFileName="SourceData";
	}
	
	@Test
	public void runTests() {
		
		 new MainPage_Netmeds() 
		 .enterMoleculeName_PressEnter("CEFTRIAXONE 1GM")
		 .scrollTillLastMoleculeResults()
		 .getAll_Alternatives_Of_Molecule_StoreInMap("CEFTRIAXONE 1GM")
		 .get_AlternativesDetails_StoreInMap();
		 
		
	}

}
