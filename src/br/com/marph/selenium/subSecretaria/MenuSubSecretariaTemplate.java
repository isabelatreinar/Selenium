package br.com.marph.selenium.subSecretaria;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.utils.AcessoUtils;

public class MenuSubSecretariaTemplate {
	private MenuSubSecretariaTemplate() {
	}
	//"closeModalHome",
	public static void prepararAcessoSubSecretaria(WebDriver driver) {
		
		AcessoUtils.xpathClick(driver, "//td[@onmouseup='cmItemMouseUp (this,3)']");
		
		driver.findElement(By.xpath("//*[@id='subsecretariaMenu']")).click();

	}

}
