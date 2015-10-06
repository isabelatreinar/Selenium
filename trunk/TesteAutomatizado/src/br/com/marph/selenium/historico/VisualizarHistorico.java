package br.com.marph.selenium.historico;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VisualizarHistorico {
	
	public void visualizarHistorico(WebDriver driver){
		WebElement btnHistorico = driver.findElement(By.id("btnHistorico1"));
		btnHistorico.click();
	}

}
