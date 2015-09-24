package br.com.marph.selenium.resolucao;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MenuResolucaoTemplate {
	private MenuResolucaoTemplate(){}
	
	public static void prepararAcessoBaseLegal(WebDriver driver) {
		WebElement fecharbtn = driver.findElement(By.id("closeModalHome"));
		fecharbtn.click();
		
		WebElement btnEntrar = driver.findElement(By.id("btnEntradaSistemaID"));
		btnEntrar.click();
		
		WebElement btnAcessar = driver.findElement(By.id("btnAcessar"));
		btnAcessar.click();
		
		WebElement btnConfirmar = driver.findElement(By.id("confirmarDados"));
		btnConfirmar.click();
		
		WebElement btnAcessarSist = driver.findElement(By.id("acessarSistema"));
		btnAcessarSist.click();			
		
		WebElement menuCadastrar = driver.findElement(By.xpath("//td[@onmouseup='cmItemMouseUp (this,2)']"));
		menuCadastrar.click(); 
		
		WebElement menuUsuario = driver.findElement(By.xpath("//*[@id='resolucaoMenu']"));
		menuUsuario.click();
	}
}
