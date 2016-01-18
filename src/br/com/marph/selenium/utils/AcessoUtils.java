package br.com.marph.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class AcessoUtils {
	public static void idClick(WebDriver driver, String... ids){
		for (String idElemento : ids) {
			driver.findElement(By.id(idElemento)).click();
		}
	}
	
	public static void idSend(WebDriver driver,String valor, String... ids){
		for (String idElemento : ids) {
			driver.findElement(By.id(idElemento)).sendKeys(valor);
		}
	}
	
	public static void xpathClick(WebDriver driver, String... xpath){
		for (String xpathElemento : xpath) {
			driver.findElement(By.xpath(xpathElemento)).click();
		}
	}
	
	public static void xpathSend(WebDriver driver,String valor, String... xpath){
		for (String xpathElemento : xpath) {
			driver.findElement(By.xpath(xpathElemento)).sendKeys(valor);
		}
	}
	
	public static void xpathChoosenSend(WebDriver driver,String xpath, Object... valores){
		for (Object valor : valores) {
			if(valor instanceof Keys){
				driver.findElement(By.xpath(xpath)).sendKeys((Keys)valor);
			}else{
				driver.findElement(By.xpath(xpath)).sendKeys((String)valor);
			}
			
		}
	}
	
	
}
