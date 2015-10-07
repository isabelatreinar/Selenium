package br.com.marph.selenium.historico;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PesquisarHistorico {
	
	public static void pesquisarHistorico(WebDriver driver){
		
		// Exibe os campos de pesquisa de histórico
		WebElement exibirPesquisa = driver.findElement(By.xpath("//button[@class='btn btCollapseOpen']"));
		exibirPesquisa.click();
		
		/*WebElement dataInicial = driver.findElement(By.id("dataInicialHistorico"));
		dataInicial.click();
		WebElement dataInicialSeleciona = driver.findElement(By.xpath("//td[@class='day']"));
		dataInicialSeleciona.click();
		
		WebElement dataFinal = driver.findElement(By.id("dataFinalHistorico"));
		dataFinal.click();
		WebElement dataFinalSeleciona = driver.findElement(By.xpath("//td[@class='day']"));
		dataFinalSeleciona.click();*/
		
		WebElement campoAlterado = driver.findElement(By.id("camposBeneficiario_chosen"));
		campoAlterado.click();
		campoAlterado.sendKeys("Responsável nome");
		campoAlterado.sendKeys(Keys.TAB);
		//WebElement campoAlteradoSeleciona = driver.findElement(By.xpath("//*[@id='camposBeneficiario_chosen']/div/ul/li[9]]"));
		//campoAlteradoSeleciona.click();
		
		WebElement modificadoPor = driver.findElement(By.id("usuariosAlteracao_chosen"));
		modificadoPor.click();
		
		WebElement modificadoPorSeleciona = driver.findElement(By.xpath("//*[@id='usuariosAlteracao_chosen']/div/ul/li"));
		modificadoPorSeleciona.click();
		
		WebElement btnPesquisar = driver.findElement(By.id("btnPesquisar"));
		btnPesquisar.click();
	}
	
}
