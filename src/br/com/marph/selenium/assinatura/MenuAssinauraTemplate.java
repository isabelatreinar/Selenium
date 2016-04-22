package br.com.marph.selenium.assinatura;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MenuAssinauraTemplate {
	
	public static void menuAssinaturaTermoAditivo(WebDriver driver) {		
	
	driver.findElement(By.xpath("//td[@onmouseup='cmItemMouseUp (this,0)']")).click(); 
	
	driver.findElement(By.xpath("//*[@id='']")).click();
		
	}
}
