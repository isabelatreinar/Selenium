package br.com.marph.selenium.beneficiario;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import br.com.marph.selenium.utils.AcessoUtils;

public class MenuBeneficiarioTemplate {
	public static void menuBeneficiario(WebDriver driver) {
		AcessoUtils.xpathClick(driver, "//td[@onmouseup='cmItemMouseUp (this,3)']");
		driver.findElement(By.xpath("//*[@id='beneficiarioMenu']")).click();
	}
}
