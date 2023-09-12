package com.leaftaps.testcases;

import java.util.HashMap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.testng.api.base.ProjectHooks;
import com.leaftaps.pages.LoginPage;

public class ReadAndWrite extends ProjectHooks{
	@BeforeTest
	public void setValues() {
		testcaseName = "ExtractDatas";
		testDescription ="Extract Data from App and store it in Excel";
		authors="Hari";
		category ="Extract Datas";
		excelFileName="SourceData";
	}
	
	@Test(dataProvider = "fetchData")
	public void runTests(String username,String password) {
		new LoginPage()
		.enterUsername(username)
		.enterPassword(password)
		.clickLogin()
		.getCRMSFATextAndWriteToExcel(username);
		
			
		
	}

}
