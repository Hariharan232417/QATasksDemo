package com.leaftaps.testcases;

import java.util.HashMap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.testng.api.base.ProjectHooks;
import com.leaftaps.pages.MainPage_Netmeds;
import com.leaftaps.pages.OneMG_MainPage;

public class OneMg_PIPERACILLIN4GM_TAZOBACTUM500MG extends ProjectHooks{
	@BeforeTest
	public void setValues() {
		testcaseName = "PIPERACILLIN 4GM + TAZOBACTUM 500MG";
		testDescription ="Extract Data from One MG App and store it in Excel for Molecule - OneMg_PIPERACILLIN 4GM + TAZOBACTUM 500MG";
		authors="Hari";
		category ="Extract Datas";
		excelFileName="SourceData";
	}
	
	@Test
	public void runTests() {
		
		new OneMG_MainPage()
		 .closeBanner()
		 .enterMoleculeName_PressEnter("PIPERACILLIN 4GM + TAZOBACTUM 500MG")
		 .getAll_Alternatives_Of_Molecule_StoreInMap("PIPERACILLIN 4GM + TAZOBACTUM 500MG")
		 .get_AlternativesDetails_StoreInMap("PIPERACILLIN 4GM + TAZOBACTUM 500MG");
		 
		
	}

}
