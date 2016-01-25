package br.com.marph.selenium.subSecretaria;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.enums.EnumAcesso;
import br.com.marph.selenium.utils.AcessoUtils;

public class MenuSubSecretariaTemplate {
	private MenuSubSecretariaTemplate() {
	}

	public static void prepararAcessoSubSecretaria(WebDriver driver) {
		AcessoUtils.idClick(driver, "closeModalHome", "btnEntradaSistemaID", "btnAcessar", "confirmarDados",
				EnumAcesso.ADMINISTRADOR.getId(), "acessarSistema");

		AcessoUtils.xpathClick(driver, "//td[@onmouseup='cmItemMouseUp (this,3)']");

		subSecretaria(driver);
	}

	private static void subSecretaria(WebDriver driver) {
		driver.findElement(By.xpath("//*[@id='subsecretariaMenu']")).click();
	}
}
