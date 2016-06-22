package br.com.marph.selenium.tipoBaseLegal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.utils.AcessoUtils;

public class MenuTipoBaseLegalTemplate {
	// private MenuTipoBaseLegalTemplate(){}

	public static void menuTipoBaseLegal(WebDriver driver) {
		
		AcessoUtils.xpathClick(driver, "//td[@onmouseup='cmItemMouseUp (this,3)']");

		driver.findElement(By.xpath("//*[@id='tipoBaseLegalMenu']")).click();

	}
}
