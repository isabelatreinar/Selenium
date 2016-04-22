package br.com.marph.selenium.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import br.com.marph.selenium.utils.AcessoUtils;

public class MenuBaseLegalTemplate {

	public static void menuBaseLegal(WebDriver driver) {
		AcessoUtils.xpathClick(driver, "//td[@onmouseup='cmItemMouseUp (this,3)']");
		driver.findElement(By.xpath("//*[@id='baseLegalMenu']")).click();
	}

}
