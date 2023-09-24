package com.leaftaps.testcases;

import java.util.HashMap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.testng.api.base.ProjectHooks;
import com.leaftaps.pages.MainPage_Netmeds;
import com.leaftaps.pages.OneMG_MainPage;

public class OneMg_CEFOPERAZONE1GM_SULBACTUM_500MG extends ProjectHooks{
	@BeforeTest
	public void setValues() {
		testcaseName = "OneMg_CEFOPERAZONE 1GM + SULBACTUM 500MG";
		testDescription ="Extract Data from One MG App and store it in Excel for Molecule - OneMg_CEFOPERAZONE 1GM + SULBACTUM 500MG";
		authors="Hari";
		category ="Extract Datas";
		excelFileName="SourceData";
	}
	
	@Test
	public void runTests() {
		
		new OneMG_MainPage()
		 .closeBanner()
		 .enterMoleculeName_PressEnter("CEFOPERAZONE 1GM + SULBACTUM 500MG")
		 .getAll_Alternatives_Of_Molecule_StoreInMap("CEFOPERAZONE 1GM + SULBACTUM 500MG")
		 .get_AlternativesDetails_StoreInMap("CEFOPERAZONE 1GM + SULBACTUM 500MG");
		 
		
	}

}
