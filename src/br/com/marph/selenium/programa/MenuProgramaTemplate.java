package br.com.marph.selenium.programa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MenuProgramaTemplate {
	private MenuProgramaTemplate(){}
	
	public static void prepararAcessoPrograma(WebDriver driver) {
		driver.findElement(By.id("closeModalHome")).click();
		
		driver.findElement(By.id("btnEntradaSistemaID")).click();
		
		driver.findElement(By.id("btnAcessar")).click();
		
		driver.findElement(By.id("confirmarDados")).click();
		
		driver.findElement(By.id("acessarSistema")).click();			
		
		driver.findElement(By.xpath("//td[@onmouseup='cmItemMouseUp (this,2)']")).click(); 
		
		driver.findElement(By.xpath("//*[@id='programaMenu']")).click();
	}
}
