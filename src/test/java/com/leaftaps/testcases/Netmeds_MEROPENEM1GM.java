package com.leaftaps.testcases;

import java.util.HashMap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.testng.api.base.ProjectHooks;
import com.leaftaps.pages.MainPage_Netmeds;

public class Netmeds_MEROPENEM1GM extends ProjectHooks{
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
		 .enterMoleculeName_PressEnter("TEICOPLANIN 400MG")
		 .scrollTillLastMoleculeResults()
		 .getAll_Alternatives_Of_Molecule_StoreInMap("TEICOPLANIN 400MG")
		 .get_AlternativesDetails_StoreInMap();
		 
		
	}

}
