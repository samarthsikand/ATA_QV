package com.main.ATA_QV;

import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
import java.util.ArrayList;
import org.openqa.selenium.By; 
import org.openqa.selenium.JavascriptExecutor; 
import org.openqa.selenium.WebDriver; 
import org.openqa.selenium.WebElement; 
import org.openqa.selenium.firefox.FirefoxDriver;
import com.main.ATA_QV.Tree.Node;
import com.google.common.collect.Lists;

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
		 labels = Lists.newArrayList("Mrs","Mr");
		 selectRadiobutton(labels);
	}
	 public static void navigateToWebsite() {
		 driver.manage().window().maximize();
		 driver.navigate().to("http://www.utexas.edu/learn/forms/radio.html");
	 }

	 public static void selectRadiobutton(List<String> labels) {
		 WebElement inputEle = null;
		 ele = qvObj.returnWebElement(labels);
		 if(ele != null) {
			 inputEle = ele.findElement(By.xpath("./following::input[@type='radio' and @value='"+labels.get(0)+"']"));
			 if(inputEle != null) {
				 inputEle.click();
			 } else {
				 System.out.println("Element not found!!");
			 }
		 } else {
			 inputEle = driver.findElement(By.xpath("//input[@type='radio' and @value='"+labels.get(0)+"']"));
			 if(inputEle != null) {
				 inputEle.click();
			 } else {
				 System.out.println("Element not found!!");
			 }
		 }
	}

}