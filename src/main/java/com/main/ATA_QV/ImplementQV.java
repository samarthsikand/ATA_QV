package com.main.ATA_QV;

import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
import java.util.ArrayList;
import org.openqa.selenium.By; 
import org.openqa.selenium.support.ui.Select; 
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
		 labels = Lists.newArrayList("Mr","Mrs");
		 selectRadiobutton(labels);
		 labels = Lists.newArrayList("abc","xyz","klmn");
		 selectRadiobutton(labels);
		 labels = Lists.newArrayList("xyz","abc");
		 selectCheckbox(labels);
		 labels = Lists.newArrayList("abc","xyz");
		 selectDropdown(labels,"sads");
	}
	 public static void navigateToWebsite() {
		 driver.manage().window().maximize();
		 driver.navigate().to("http://www.utexas.edu/learn/forms/radio.html");
	 }

	 public static void selectRadiobutton(List<String> labels) {
		 ele = returnFinalElement(labels,"selectradiobutton");
		 if(ele != null) {
			 ele.click();
		 } else {
			 System.out.println("The radiobutton was not found!!");
		 }
	}

	 public static void selectCheckbox(List<String> labels) {
		 ele = returnFinalElement(labels,"selectcheckbox");
		 if(ele != null) {
			 ele.click();
		 } else {
			 System.out.println("Checkbox could not be found!!");
		 }
	}

	 public static void selectDropdown(List<String> labels, String value) {
		 Select se = null;
		 ele = returnFinalElement(labels,"dropdown");
		 if(ele != null) {
			 se = new Select(ele);
			 se.selectByVisibleText(value);
		 } else {
			 System.out.println("Dropdown not found!!");
		 }
	 }

	 public static WebElement returnFinalElement(List<String> labels, String elementName) {
		 WebElement finalEle = null;
		 WebElement inputEle = null;
		 if(elementName.equals("clickable") || elementName.equals("setfield")) {
			 finalEle = qvObj.returnWebElement(labels);
		 } else if(elementName.equals("selectdate")) {
			 finalEle = qvObj.returnCalendarElement(labels);
		 } else if(elementName.equals("selectradiobutton")) {
			 inputEle = null;
			 inputEle = qvObj.returnWebElement(labels);
			 if(inputEle != null) {
				 finalEle = inputEle.findElement(By.xpath("./following::input[@type='radio' and @value='"+labels.get(0)+"']"));
				 if(finalEle == null) {
					 finalEle = driver.findElement(By.xpath("//input[@type='radio' and @value='"+labels.get(0)+"']"));
					 if(finalEle == null) {
						 System.out.println("Radiobutton not found!!");
					 }
				 }
			 }
		 } else if(elementName.equals("selectcheckbox")) {
			 inputEle = null;
			 inputEle = qvObj.returnWebElement(labels);
			 if(inputEle != null) {
				 finalEle = inputEle.findElement(By.xpath("./preceding-sibling::input[@type='checkbox']"));
			 } else {
				 finalEle = driver.findElement(By.xpath("//input[@type='checkbox' and @value='"+labels.get(0)+"']"));
			 }
		 } else if(elementName.equals("dropdown")) {
			 inputEle = null;
			 inputEle = qvObj.returnWebElement(labels);
			 if(inputEle != null) {
				 finalEle = inputEle.findElement(By.xpath("./following::select[1] | ./descendant::select[1]"));
			 }
 		 } else if(elementName.equals("multiselect")) {
			 inputEle = null;
			 inputEle = qvObj.returnWebElement(labels);
			 if(inputEle != null) {
				 finalEle = inputEle.findElement(By.xpath("./following::select[@multiple]"));
			 }
		 }
		 return finalEle;
	 }
}