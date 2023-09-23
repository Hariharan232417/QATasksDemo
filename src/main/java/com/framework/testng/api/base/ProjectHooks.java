package com.framework.testng.api.base;

import java.io.IOException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import com.framework.selenium.api.base.SeleniumBase;
import com.framework.utils.ReadExcel;

public class ProjectHooks extends SeleniumBase {

	@DataProvider(name = "fetchData")
	public Object[][] fetchData() throws IOException {
		return ReadExcel.readExcelData(excelFileName);
	}
	
	@BeforeMethod
	@Parameters("url")
	public void preCondition(String url) {
		startApp("chrome", true, url);
		setNode();
	}
	
	@AfterMethod
	public void postCondition() {
		close();

	}

	

	
	  

	
	
}
