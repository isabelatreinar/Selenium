package br.com.marph.selenium.beneficiario;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PesquisarBeneficiarioMozilla {

	public static void pesquisar(WebDriver driver) {
		WebElement nome = driver.findElement(By.id("buscaNome"));
		nome.sendKeys("FUNDO MUNICIPAL DE SAÚDE DE CAMPO BELO");
		
		//Selecionar unidade regional
		WebElement unidadeRegional = driver.findElement(By.id("unidadeRegional_chosen"));
		unidadeRegional.click();
		WebElement procuraTipoRegional = driver.findElement(By.xpath("//li[@data-option-array-index='6']"));
		procuraTipoRegional.click(); 
		//fim
		
		WebElement btnPesquisar = driver.findElement(By.id("btnPesquisar"));
		btnPesquisar.click();
	}
}
