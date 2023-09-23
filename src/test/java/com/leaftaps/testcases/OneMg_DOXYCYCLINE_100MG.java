package com.leaftaps.testcases;

import java.util.HashMap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.testng.api.base.ProjectHooks;
import com.leaftaps.pages.MainPage_Netmeds;
import com.leaftaps.pages.OneMG_MainPage;

public class OneMg_DOXYCYCLINE_100MG extends ProjectHooks{
	@BeforeTest
	public void setValues() {
		testcaseName = "OneMg_DOXYCYCLINE_100MG";
		testDescription ="Extract Data from One MG App and store it in Excel for Molecule - OneMg_DOXYCYCLINE_100MG";
		authors="Hari";
		category ="Extract Datas";
		excelFileName="SourceData";
	}
	
	@Test
	public void runTests() {
		
		new OneMG_MainPage()
		 .closeBanner()
		 .enterMoleculeName_PressEnter("DOXYCYCLINE 100MG")
		 .getAll_Alternatives_Of_Molecule_StoreInMap("DOXYCYCLINE 100MG")
		 .get_AlternativesDetails_StoreInMap("DOXYCYCLINE 100MG");
		 
		
	}

}
