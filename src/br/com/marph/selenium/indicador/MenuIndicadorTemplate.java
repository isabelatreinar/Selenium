package br.com.marph.selenium.indicador;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.utils.AcessoUtils;

public class MenuIndicadorTemplate {
	public static void menuIndicador(WebDriver driver) {
		AcessoUtils.xpathClick(driver, "//td[@onmouseup='cmItemMouseUp (this,3)']");
		driver.findElement(By.xpath("//*[@id='indicadoresMenu']")).click();
	}
}
