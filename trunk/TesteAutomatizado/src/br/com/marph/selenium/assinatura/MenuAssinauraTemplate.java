package br.com.marph.selenium.assinatura;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MenuAssinauraTemplate {
	
	public static void prepararAcessoBaseLegal(WebDriver driver) {
		
	
	driver.findElement(By.id("closeModalHome")).click();
	
	driver.findElement(By.id("btnEntradaSistemaID")).click();
	
	driver.findElement(By.id("btnAcessar")).click();
	
	driver.findElement(By.id("confirmarDados")).click();
	
	driver.findElement(By.id("acessarSistema")).click();			
	
	driver.findElement(By.xpath("//td[@onmouseup='cmItemMouseUp (this,0)']")).click(); 
	
	driver.findElement(By.xpath("//*[@id='']")).click();
		
	}
}
