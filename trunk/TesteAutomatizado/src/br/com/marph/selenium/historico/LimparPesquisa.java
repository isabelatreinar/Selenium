package br.com.marph.selenium.historico;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LimparPesquisa {
	
	public static void limparPesquisa(WebDriver driver){
		WebElement btnLimpar = driver.findElement(By.id(""));
		btnLimpar.click();
	}
}
