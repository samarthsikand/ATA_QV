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
		 selectCheckbox(labels);
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

	 public static void selectCheckbox(List<String> labels) {
		 ele = qvObj.returnWebElement(labels);
		 if(ele != null) {
			 WebElement inputEle = ele.findElement(By.xpath("./preceding-sibling::input[@type='checkbox']"));
			 if(inputEle != null) {
				 inputEle.click();
			 } else {
				 System.out.println("Checkbox not found!!");
			 }
		 } else {
			 WebElement inputEle = driver.findElement(By.xpath("//input[@type='checkbox' and @value='"+labels.get(0)+"']"));
			 if(inputEle != null) {
				 inputEle.click();
			 } else {
				 System.out.println("Checkbox is not present!!");			 }
		 }
	}

	 public static void selectDropdown(List<String> labels, String value) {
		 Select se = null;
		 ele = qvObj.returnWebElement(labels);
		 if(ele != null) {
			 WebElement inputEle = ele.findElement(By.xpath("./following::select[1] | ./descendant::select[1] "));
			 if(inputEle != null) {
				 se = new Select(inputEle);
				 se.selectByVisibleText(value);
			 } else {
				 System.out.println("Dropdown not found!!");
			 }
		 } else {
			 System.out.println("Dropdown not found!!");
		 }
	 }

}