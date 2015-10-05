package br.com.maph.selenium.tipoBaseLegal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MenuTipoBaseLegalTemplate {
	//private MenuTipoBaseLegalTemplate(){}
	
	public static void prepararAcessoTipoBaseLegal(WebDriver driver){
		WebElement btnFecharModal = driver.findElement(By.id("closeModalHome"));
		btnFecharModal.click();
		
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
		
		WebElement menuTipoBaseLegal = driver.findElement(By.xpath("//*[@id='tipoBaseLegalMenu']"));
		menuTipoBaseLegal.click();							
		
	}
}
