package com.leaftaps.testcases;

import java.util.HashMap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.testng.api.base.ProjectHooks;
import com.leaftaps.pages.MainPage_Netmeds;
import com.leaftaps.pages.OneMG_MainPage;

public class OneMg_TEICOPLANIN400MG extends ProjectHooks{
	@BeforeTest
	public void setValues() {
		testcaseName = "OneMg_TEICOPLANIN400MG";
		testDescription ="Extract Data from One MG App and store it in Excel for Molecule - OneMg_TEICOPLANIN 400MG";
		authors="Hari";
		category ="Extract Datas";
		excelFileName="SourceData";
	}
	
	@Test
	public void runTests() {
		
		new OneMG_MainPage()
		 .closeBanner()
		 .enterMoleculeName_PressEnter("TEICOPLANIN 400MG")
		 .getAll_Alternatives_Of_Molecule_StoreInMap("TEICOPLANIN 400MG")
		 .get_AlternativesDetails_StoreInMap("TEICOPLANIN 400MG");
		 
		
	}

}
