package com.leaftaps.testcases;

import java.util.HashMap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.testng.api.base.ProjectHooks;
import com.leaftaps.pages.MainPage_Netmeds;
import com.leaftaps.pages.OneMG_MainPage;

public class OneMg_LINEZOLID_600MG300ML extends ProjectHooks{
	@BeforeTest
	public void setValues() {
		testcaseName = "OneMg_LINEZOLID 600MG/300ML";
		testDescription ="Extract Data from One MG App and store it in Excel for Molecule - OneMg_LINEZOLID 600MG/300ML";
		authors="Hari";
		category ="Extract Datas";
		excelFileName="SourceData";
	}
	
	@Test
	public void runTests() {
		
		new OneMG_MainPage()
		 .closeBanner()
		 .enterMoleculeName_PressEnter("LINEZOLID 600MG/300ML")
		 .getAll_Alternatives_Of_Molecule_StoreInMap("LINEZOLID 600MG/300ML")
		 .get_AlternativesDetails_StoreInMap("LINEZOLID 600MG/300ML");
		 
		
	}

}
