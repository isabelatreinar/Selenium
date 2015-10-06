package br.com.marph.selenium.historico;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PesquisarHistorico {
	
	public void pesquisarHistorico(WebDriver driver){
		WebElement exibirPesquisa = driver.findElement(By.xpath("//button[@class='btn btCollapseOpen']"));
		exibirPesquisa.click();
	}
}
