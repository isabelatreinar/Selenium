package br.com.marph.selenium.blocoDeFinanciamento;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import br.com.marph.selenium.utils.AcessoUtils;

public class MenuBlocoTemplate {
	public static void menuBlocoFinanciamento(WebDriver driver) {
		AcessoUtils.xpathClick(driver, "//td[@onmouseup='cmItemMouseUp (this,3)']");
		driver.findElement(By.xpath("//*[@id='blocoFinanciamentoMenu']")).click();
	}
}
