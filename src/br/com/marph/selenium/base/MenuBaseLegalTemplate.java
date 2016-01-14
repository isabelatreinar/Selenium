package br.com.marph.selenium.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.enums.EnumAcesso;
import br.com.marph.selenium.utils.AcessoUtils;

public class MenuBaseLegalTemplate {
	private MenuBaseLegalTemplate() {
	}

	public static void prepararAcessoBaseLegal(WebDriver driver) {
		
		AcessoUtils.acessaId(driver, "closeModalHome", "btnEntradaSistemaID","btnAcessar","confirmarDados",EnumAcesso.ADMINISTRADOR.getId(),"acessarSistema");
		
		AcessoUtils.acessaXpath(driver, "//td[@onmouseup='cmItemMouseUp (this,2)']");
		
		baseLegal(driver);
	}

	private static void baseLegal(WebDriver driver) {
		driver.findElement(By.xpath("//*[@id='baseLegalMenu']")).click();
	}

}
