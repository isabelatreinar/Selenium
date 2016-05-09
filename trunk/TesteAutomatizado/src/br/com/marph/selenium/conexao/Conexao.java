package br.com.marph.selenium.conexao;

import org.openqa.selenium.WebDriver;

public class Conexao {
	
	public static void 	ip(WebDriver driver){
		driver.get("http://172.16.10.115:8080");
	}
}
