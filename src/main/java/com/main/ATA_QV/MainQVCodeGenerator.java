package com.main.ATA_QV;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class MainQVCodeGenerator {

	public void readFile(String featureFilePath) {
		String strLine;
		Map<String,String> functions = new HashMap<String,String>();
		List<String> labelsList = new ArrayList<String>();
		String codeLine = "";
		
		try {
			File file = new File("src/main/java/com/main/ATA_QV/ImplementQV.java");
			if(!file.exists()){
    			file.createNewFile();
    		} else {
    			file.delete();
    		}
			FileWriter fileWritter = new FileWriter("src/main/java/com/main/ATA_QV/"+file.getName(),true);
			fileWritter.write(getCode("Package Name"));
			fileWritter.write(getCode("Header Files"));
			fileWritter.write(getCode("Class Name"));
			fileWritter.write("\t"+getCode("Firefox driver"));
			fileWritter.write("\t"+getCode("URL initialization"));
			fileWritter.write("\t"+getCode("webelement initialization"));
			fileWritter.write("\t"+getCode("MainQV object"));
			fileWritter.write("\t"+getCode("Main function"));
			fileWritter.write("\t\t"+getCode("Element list Html tag"));
			/*fileWritter.write("\t"+getCode("webelement initialization"));*/
			fileWritter.write("\t\t"+getCode("Root Node"));
			
			fileWritter.write("\t\t"+getCode("Labels list"));
			FileInputStream fstream = new FileInputStream(featureFilePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			while((strLine = br.readLine()) != null) {
				codeLine = "";
				String listLabels = "";
				strLine = strLine.trim();
				if(strLine.length() > 0) {
					System.out.println(strLine);
					strLine = strLine.substring(strLine.indexOf(" ")).trim();
					if(strLine.startsWith("I am on")) {
						System.out.println(strLine.substring(strLine.indexOf('"')+1, strLine.lastIndexOf('"')));
						codeLine = "\t\t navigateToWebsite();\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void navigateToWebsite() {\n"+
									"\t\t driver.manage().window().maximize();\n"+
									"\t\t driver.navigate().to(\"http://"+strLine.substring(strLine.indexOf('"')+1, strLine.lastIndexOf('"'))+"\");\n"+
									"\t }\n";
						if(!functions.containsKey("navigation")) {
							functions.put("navigation", codeLine);
						}
					} else if(strLine.contains("click") && (strLine.contains("button") || strLine.contains("link"))) {
						labelsList = getWebElementAnchors(strLine);
						for(String str : labelsList) {
							listLabels = listLabels+"\""+str+"\",";
						}
						listLabels = listLabels.substring(0, listLabels.length()-1);
						codeLine = "\t\t labels = Lists.newArrayList("+listLabels+");\n"+
									"\t\t clickButtonOrLink(labels);\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void clickButtonOrLink(List<String> labels) {\n"+
									"\t\t ele = returnFinalElement(labels,\"clickable\");\n"+
									"\t\t if(ele != null) {\n"+
									"\t\t\t ele.click();\n"+
									"\t\t } else {\n"+
									"\t\t\t System.out.println(\"Element could not be found!!\");\n"+
									"\t\t }\n"+
									"\t}\n";
						if(!functions.containsKey("clickbutton")) {
							functions.put("clickbutton",codeLine);
						}
					} else if(strLine.contains("set") && strLine.contains("field")) {
						labelsList = getWebElementAnchors(strLine);
						for(int i = 0;i<labelsList.size()-1;i++) {
							listLabels = listLabels+"\""+labelsList.get(i)+"\",";
						}
						
						listLabels = listLabels.substring(0, listLabels.length()-1);
						codeLine = "\t\t labels = Lists.newArrayList("+listLabels+");\n"+
									"\t\t insertIntoFieldValues(labels,\""+labelsList.get(labelsList.size()-1)+"\");\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void insertIntoFieldValues(List<String> labels,String value) {\n"+
									"\t\t ele = returnFinalElement(labels,\"setfield\");\n" +
									"\t\t if(ele != null) {\n"+
									"\t\t\t //WebElement inputEle = ele.findElement(By.xpath(\"./descendant::input[1] | ./following::input[1]\"));\n"+
									"\t\t\t ele.sendKeys(value);\n"+
									"\t\t } else {\n"+
									"\t\t\t System.out.println(\"Element could not be found!!\");\n"+
									"\t\t }\n"+
									"\t}\n";
						if(!functions.containsKey("setfield")) {
							functions.put("setfield",codeLine);
						}
					} else if(strLine.contains("click") && strLine.contains("field")) {
						labelsList = getWebElementAnchors(strLine);
						for(String str : labelsList) {
							listLabels = listLabels+"\""+str+"\",";
						}
						listLabels = listLabels.substring(0, listLabels.length()-1);
						codeLine = "\t\t labels = Lists.newArrayList("+listLabels+");\n"+
									"\t\t clickField(labels);\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void clickField(List<String> labels) {\n"+
									"\t\t ele = returnFinalElement(labels,\"clickable\");\n"+
									"\t\t if(ele != null) {\n"+
									"\t\t\t //WebElement inputEle = ele.findElement(By.xpath(\"./descendant::input[1] | ./following::input[1]\"));\n"+
									"\t\t\t inputEle.click();\n"+
									"\t\t } else {\n"+
									"\t\t\t System.out.println(\"Element could not be found!!\");\n"+
									"\t\t }\n"+
									"\t}\n";
						if(!functions.containsKey("clickfield")) {
							functions.put("clickfield",codeLine);
						}
					} else if(strLine.contains("select") && strLine.contains("date")) {
						labelsList = getWebElementAnchors(strLine);
						for(String str : labelsList) {
							listLabels = listLabels+"\""+str+"\",";
						}
						listLabels = listLabels.substring(0, listLabels.length()-1);
						codeLine = "\t\t labels = Lists.newArrayList("+listLabels+");\n"+
									"\t\t selectDateOnCalendar(labels);\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void selectDateOnCalendar(List<String> labels) {\n"+
									"\t\t ele = qvObj.returnCalendarElement(labels);\n"+
									"\t\t if(ele != null) {\n"+
									"\t\t\t ele.click();\n"+
									"\t\t } else {\n"+
									"\t\t\t System.out.println(\"Date could not be found!!\");\n"+
									"\t\t }\n"+
									"\t}\n";
						if(!functions.containsKey("setdate")) {
							functions.put("setdate", codeLine);
						}
					} else if(strLine.contains("select") && strLine.contains("radiobutton")) {
						labelsList = getWebElementAnchors(strLine);
						for(String str : labelsList) {
							listLabels = listLabels + "\"" + str + "\",";
						}
						listLabels = listLabels.substring(0, listLabels.length()-1);
						codeLine = "\t\t labels = Lists.newArrayList("+listLabels+");\n"+
									"\t\t selectRadiobutton(labels);\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void selectRadiobutton(List<String> labels) {\n" +
									"\t\t ele = returnFinalElement(labels,\"selectradiobutton\");\n" +
									"\t\t if(ele != null) {\n" +
									"\t\t\t ele.click();\n"+
									"\t\t } else {\n" +
									"\t\t\t System.out.println(\"The radiobutton was not found!!\");\n"+
									"\t\t }\n"+
									"\t}\n";
						if(!functions.containsKey("radiobutton")) {
							functions.put("radiobutton", codeLine);
						}
					} else if(strLine.contains("select") && strLine.contains("radiobutton") && strLine.contains("group")) {
						labelsList = getWebElementAnchors(strLine);
						for(String str : labelsList) {
							listLabels = listLabels + "\"" +str + "\",";
						}
						String value = labelsList.get(0);
						listLabels = listLabels.substring(listLabels.indexOf(",")+1,listLabels.length()-1);
						codeLine = "\t\t labels = Lists.newArrayList("+listLabels+");\n" +
									"\t\t selectRadiobuttonGroup(labels,\""+value+"\");\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void selectRadiobuttonGroup(List<String> labels, String value) {\n" +
									"\t\t WebElement inputEle = null;\n" +
									"\t\t ele = qvObj.returnWebElement(labels);\n" + 
									"\t\t if(ele != null) {\n" +
									"\t\t\t inputEle = ele.findElement(By.xpath(\"./following::input[@type='radio' and @value='\"+value+\"']\"));\n"+
									"\t\t\t inputEle.click();\n" + 
									"\t\t } else {\n" +
									"\t\t\t System.out.println(\"Element not found!!\");\n"+
									"\t\t }\n"+
									"\t}\n";
						if(!functions.containsKey("radiobuttongroup")) {
							functions.put("radiobuttongroup", codeLine);
						}
					} else if(strLine.contains("select") && strLine.contains("checkbox")) {
						labelsList = getWebElementAnchors(strLine);
						for(String str : labelsList) {
							listLabels = listLabels + "\"" + str + "\",";
						}
						listLabels = listLabels.substring(0, listLabels.length()-1);
						codeLine = "\t\t labels = Lists.newArrayList("+listLabels+");\n"+
									"\t\t selectCheckbox(labels);\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void selectCheckbox(List<String> labels) {\n"+
									"\t\t ele = returnFinalElement(labels,\"selectcheckbox\");\n"+
									"\t\t if(ele != null) {\n"+
									"\t\t\t ele.click();\n"+
									"\t\t } else {\n"+
									"\t\t\t System.out.println(\"Checkbox could not be found!!\");\n"+
									"\t\t }\n"+
									"\t}\n";
						if(!functions.containsKey("checkbox")) {
							functions.put("checkbox", codeLine);
						}
					} else if(strLine.contains("select") && strLine.contains("dropdown")) {
						labelsList = getWebElementAnchors(strLine);
						for(String str : labelsList) {
							listLabels = listLabels + "\"" +str + "\",";
						}
						String value = labelsList.get(0);
						
						listLabels = listLabels.substring(listLabels.indexOf(",")+1,listLabels.length()-1);
						codeLine = "\t\t labels = Lists.newArrayList("+listLabels+");\n"+
									"\t\t selectDropdown(labels,\""+value+"\");\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void selectDropdown(List<String> labels, String value) {\n" +
									"\t\t Select se = null;\n" +
									"\t\t ele = returnFinalElement(labels,\"dropdown\");\n" +
									"\t\t if(ele != null) {\n" +
									"\t\t\t se = new Select(ele);\n" +
									"\t\t\t se.selectByVisibleText(value);\n" +
									"\t\t } else {\n" +
									"\t\t\t System.out.println(\"Dropdown not found!!\");\n"+
									"\t\t }\n"+
									"\t }\n";
						if(!functions.containsKey("dropdown")) {
							functions.put("dropdown", codeLine);
						}
					} else if(strLine.contains("select") && strLine.contains("multiselect")) {
						labelsList = getWebElementAnchors(strLine);
						for(String str : labelsList) {
							listLabels = listLabels + "\"" + str + "\",";
						}
						String value = labelsList.get(0);
						listLabels = listLabels.substring(listLabels.indexOf(",")+1,listLabels.length()-1);
						codeLine = "\t\t labels = Lists.newArrayList("+listLabels+");\n" +
									"\t\t selectMultiselect(labels,\""+value+"\");\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void selectMultiselect(List<String> labels,String value) {\n" +
									"\t\t Select se = null;\n" +
									"\t\t ele = returnFinalElement(labels,\"multiselect\");\n" + 
									"\t\t if(ele != null) {\n" +
									"\t\t\t String[] listOfValues = value.split(\";\");\n" +
									"\t\t\t if(listOfValues != null && listOfValues.size() != 0) {\n"+
									"\t\t\t\t se = new Select(inputEle);\n"+
									"\t\t\t\t for(String str : listOfValues) {\n"+
									"\t\t\t\t\t se.selectByVisibleText(str);\n" +
									"\t\t\t\t }\n" +
									"\t\t\t }\n"+
									"\t\t } else {\n"+
									"\t\t\t System.out.println(\"Multiselect not found!!\");\n"+
									"\t\t }\n"+
									"\t }";
						if(!functions.containsKey("multiselect")) {
							functions.put("multiselect", codeLine);
						}
					}
					
				}
			}
			fileWritter.write("\t}\n");
			
			for(String str : functions.keySet()) {
				fileWritter.write(functions.get(str));
				fileWritter.write("\n");
			}
			
			codeLine = "\t public static WebElement returnFinalElement(List<String> labels, String elementName) {\n"+
						"\t\t WebElement finalEle = null;\n"+
						"\t\t WebElement inputEle = null;\n"+
						"\t\t if(elementName.equals(\"clickable\") || elementName.equals(\"setfield\")) {\n"+
						"\t\t\t finalEle = qvObj.returnWebElement(labels);\n"+
						"\t\t } else if(elementName.equals(\"selectdate\")) {\n"+
						"\t\t\t finalEle = qvObj.returnCalendarElement(labels);\n"+
						"\t\t } else if(elementName.equals(\"selectradiobutton\")) {\n"+
						"\t\t\t inputEle = null;\n"+
						"\t\t\t inputEle = qvObj.returnWebElement(labels);\n"+
						"\t\t\t if(inputEle != null) {\n" +
						"\t\t\t\t finalEle = inputEle.findElement(By.xpath(\"./following::input[@type='radio' and @value='\"+labels.get(0)+\"']\"));\n" +
						"\t\t\t\t if(finalEle == null) {\n" +
						"\t\t\t\t\t finalEle = driver.findElement(By.xpath(\"//input[@type='radio' and @value='\"+labels.get(0)+\"']\"));\n" +
						"\t\t\t\t\t if(finalEle == null) {\n" + 
						"\t\t\t\t\t\t System.out.println(\"Radiobutton not found!!\");\n" + 
						"\t\t\t\t\t }\n" +
						"\t\t\t\t }\n"+
						"\t\t\t }\n"+
						"\t\t } else if(elementName.equals(\"selectcheckbox\")) {\n"+
						"\t\t\t inputEle = null;\n"+
						"\t\t\t inputEle = qvObj.returnWebElement(labels);\n"+
						"\t\t\t if(inputEle != null) {\n"+
						"\t\t\t\t finalEle = inputEle.findElement(By.xpath(\"./preceding-sibling::input[@type='checkbox']\"));\n"+
						"\t\t\t } else {\n"+
						"\t\t\t\t finalEle = driver.findElement(By.xpath(\"//input[@type='checkbox' and @value='\"+labels.get(0)+\"']\"));\n"+
						"\t\t\t }\n"+
						"\t\t } else if(elementName.equals(\"dropdown\")) {\n"+
						"\t\t\t inputEle = null;\n"+
						"\t\t\t inputEle = qvObj.returnWebElement(labels);\n"+
						"\t\t\t if(inputEle != null) {\n"+
						"\t\t\t\t finalEle = inputEle.findElement(By.xpath(\"./following::select[1] | ./descendant::select[1]\"));\n"+
						"\t\t\t }\n "+
						"\t\t } else if(elementName.equals(\"multiselect\")) {\n"+
						"\t\t\t inputEle = null;\n"+
						"\t\t\t inputEle = qvObj.returnWebElement(labels);\n"+
						"\t\t\t if(inputEle != null) {\n"+
						"\t\t\t\t finalEle = inputEle.findElement(By.xpath(\"./following::select[@multiple]\"));\n"+
						"\t\t\t }\n"+
						"\t\t }\n"+
						"\t\t return finalEle;\n"+
						"\t }\n";
			fileWritter.write(codeLine);
			fileWritter.write("}");
			fileWritter.close();
			br.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getWebElementAnchors(String line) {
		Pattern p = Pattern.compile("\"([^\"]*)\"");
		Matcher m = p.matcher(line);
		List<String> anchors = new ArrayList<String>();
		while(m.find()) {
			anchors.add(m.group(1));
		}
		return anchors;
	}
	
	public String getCode(String codeLine) {
		String headerFiles = "import java.util.HashMap; \n" + 
					  		 "import java.util.List; \n"+
					  		 "import java.util.Map; \n"+
					  		 "import java.util.ArrayList;\n"+
					  		 "import org.openqa.selenium.By; \n"+
					  		 "import org.openqa.selenium.support.ui.Select; \n"+
					  		 "import org.openqa.selenium.WebDriver; \n"+
					  		 "import org.openqa.selenium.WebElement; \n"+
					  		 "import org.openqa.selenium.firefox.FirefoxDriver;\n"+
					  		 "import com.main.ATA_QV.Tree.Node;\n"+
					  		 "import com.google.common.collect.Lists;\n\n";
		String packageName = "package com.main.ATA_QV;\n\n";
		String className = "public class ImplementQV {\n\n";
		String webDriverName = "private static WebDriver driver = new FirefoxDriver();\n";
		String privateStringURL = "private String URL;\n";
		String mainFunction = "public static void main(String args[]) {\n";
		String webElement = "private static WebElement ele = null;\n\n";
		String mainQv = "private static MainQV qvObj = new MainQV(driver);\n\n";
		String listOfElement = "List<WebElement> listOfElement = driver.findElements(By.xpath(\"//html\"));\n";
		String rootNode = "Node<WebElement> rootNode = null;\n";
		String labelsList = "List<String> labels = new ArrayList<String>();\n\n";
		
		if(codeLine.equalsIgnoreCase("Header files")) {
			return headerFiles;
		} else if(codeLine.equalsIgnoreCase("Package Name")) {
			return packageName;
		} else if(codeLine.equalsIgnoreCase("Class name")) {
			return className;
		} else if(codeLine.equalsIgnoreCase("Firefox Driver")) {
			return webDriverName;
		} else if(codeLine.equalsIgnoreCase("URL initialization")) {
			return privateStringURL;
		} else if(codeLine.equalsIgnoreCase("Main function")) {
			return mainFunction;
		} else if(codeLine.equalsIgnoreCase("webelement initialization")) {
			return webElement;
		} else if(codeLine.equalsIgnoreCase("MainQV object")){
			return mainQv;
		} else if(codeLine.equalsIgnoreCase("Element list Html tag")) {
			return listOfElement;
		} else if(codeLine.equalsIgnoreCase("Root Node")) {
			return rootNode;
		} else if(codeLine.equals("Labels list")){
			return labelsList;
		} else {
			return null;
		}
	}
}
