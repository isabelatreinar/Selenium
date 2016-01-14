package br.com.marph.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AcessoUtils {
	public static void acessaId(WebDriver driver, String... ids){
		for (String idElemento : ids) {
			driver.findElement(By.id(idElemento)).click();
		}
	}
	
	public static void acessaXpath(WebDriver driver, String... xpath){
		for (String xpathElemento : xpath) {
			driver.findElement(By.xpath(xpathElemento)).click();
		}
	}
}
