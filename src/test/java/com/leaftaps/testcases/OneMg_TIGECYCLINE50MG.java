package com.leaftaps.testcases;

import java.util.HashMap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.testng.api.base.ProjectHooks;
import com.leaftaps.pages.MainPage_Netmeds;
import com.leaftaps.pages.OneMG_MainPage;

public class OneMg_TIGECYCLINE50MG extends ProjectHooks{
	@BeforeTest
	public void setValues() {
		testcaseName = "OneMg_TIGECYCLINE50MG";
		testDescription ="Extract Data from One MG App and store it in Excel for Molecule - OneMg_TIGECYCLINE50MG";
		authors="Hari";
		category ="Extract Datas";
		excelFileName="SourceData";
	}
	
	@Test
	public void runTests() {
		
		new OneMG_MainPage()
		 .closeBanner()
		 .enterMoleculeName_PressEnter("TIGECYCLINE 50MG")
		 .getAll_Alternatives_Of_Molecule_StoreInMap("TIGECYCLINE 50MG")
		 .get_AlternativesDetails_StoreInMap("TIGECYCLINE 50MG");
		 
		
	}

}
