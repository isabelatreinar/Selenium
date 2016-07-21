package br.com.marph.geicom.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class LoginSistema {

	WebElement driver;

	public static void acessarUrl(WebDriver driver) {
		driver.get("http://172.16.10.115:8081/public/login");

	}
	
	public static void acessarSistema(WebDriver driver){
				
		driver.findElement(By.id("btnAcessar")).click();
		driver.findElement(By.id("confirmarDados")).click();
		driver.findElement(By.id("")).click();
		
	};

/*	public  void acessarSistema(WebDriver driver, String [] ids) {
				
		String[] botoes = new String[5];
    	botoes[0] = "closeModalHome";
    	botoes[1] = "btnAcessar";
    	botoes[2] = "tnAcessar";
    	botoes[3] = "confirmarDados";
    	botoes[4] = "acessarSistema";
    	
           
	}
	
	public void idClick(WebDriver driver, String[] ids){
	   	    
		 LoginSistema log = new LoginSistema();
		 	this.ids = log.acessarSistema(driver,ids);
	    	for (String idElemento : ids) {
	    		driver.findElement(By.id(idElemento)).click();
	        }
	    }*/

}
