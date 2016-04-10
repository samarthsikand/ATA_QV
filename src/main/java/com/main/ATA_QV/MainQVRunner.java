package com.main.ATA_QV;

public class MainQVRunner {

	private static String featureFilePath = "src/main/java/com/main/ATA_QV/new.feature";
	
	public static void main(String args[]) {
		MainQVCodeGenerator objQV = new MainQVCodeGenerator();
		objQV.readFile(featureFilePath);
	}
}
