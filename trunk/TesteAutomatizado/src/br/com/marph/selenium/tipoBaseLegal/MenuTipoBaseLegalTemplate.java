package br.com.marph.selenium.tipoBaseLegal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.enums.EnumAcesso;
import br.com.marph.selenium.utils.AcessoUtils;

public class MenuTipoBaseLegalTemplate {
	// private MenuTipoBaseLegalTemplate(){}

	public static void prepararAcessoTipoBaseLegal(WebDriver driver) {
		AcessoUtils.idClick(driver, "closeModalHome", "btnEntradaSistemaID", "btnAcessar", "confirmarDados",
				EnumAcesso.ADMINISTRADOR.getId(), "acessarSistema");

		AcessoUtils.xpathClick(driver, "//td[@onmouseup='cmItemMouseUp (this,2)']");

		tipoBase(driver);

	}

	private static void tipoBase(WebDriver driver) {
		driver.findElement(By.xpath("//*[@id='tipoBaseLegalMenu']")).click();
	}
}
