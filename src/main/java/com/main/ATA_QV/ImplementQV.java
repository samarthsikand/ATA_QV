package com.main.ATA_QV;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.google.common.collect.Lists;
import com.main.ATA_QV.Tree.Node;

public class ImplementQV {

	private static WebDriver driver = new FirefoxDriver();
	private String URL;
	private static WebElement ele = null;

		private static MainQV qvObj = new MainQV(driver);

	public static void main(String args[]) {
	List<WebElement> listOfElement = driver.findElements(By.xpath("//html"));

	Node<WebElement> rootNode = null;
	List<String> labels = new ArrayList<String>();
		 navigateToWebsite();
		 labels = Lists.newArrayList("Search Buses","From");
		 clickButton(labels);
	}
	 public static void navigateToWebsite() {
		 driver.manage().window().maximize();
		 driver.navigate().to("http://http://redbus.in");
	 }

	 public static void clickButton(List<String> labels) {
		 ele = qvObj.returnWebElement(labels);
		 ele.click();
	}

}