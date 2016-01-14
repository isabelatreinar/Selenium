package br.com.marph.selenium.beneficiario;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.enums.EnumAcesso;
import br.com.marph.selenium.utils.AcessoUtils;

public class MenuBeneficiarioTemplate {
	private MenuBeneficiarioTemplate() {
	}

	public static void prepararAcessoBeneficiario(WebDriver driver) {

		AcessoUtils.acessaId(driver, "closeModalHome", "btnEntradaSistemaID", "btnAcessar", "confirmarDados",
				EnumAcesso.ADMINISTRADOR.getId(), "acessarSistema");

		AcessoUtils.acessaXpath(driver, "//td[@onmouseup='cmItemMouseUp (this,2)']");

		beneficiario(driver);

	}

	private static void beneficiario(WebDriver driver) {
		driver.findElement(By.xpath("//*[@id='beneficiarioMenu']")).click();
	}
}
