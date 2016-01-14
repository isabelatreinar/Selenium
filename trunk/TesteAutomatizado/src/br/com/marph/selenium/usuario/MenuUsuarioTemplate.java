package br.com.marph.selenium.usuario;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.enums.EnumAcesso;
import br.com.marph.selenium.utils.AcessoUtils;

public class MenuUsuarioTemplate {
	private MenuUsuarioTemplate() {
	}

	public static void prepararAcessoUsuario(WebDriver driver) {
		AcessoUtils.acessaId(driver, "closeModalHome", "btnEntradaSistemaID", "btnAcessar", "confirmarDados",
				EnumAcesso.ADMINISTRADOR.getId(), "acessarSistema");

		AcessoUtils.acessaXpath(driver, "//td[@onmouseup='cmItemMouseUp (this,2)']");
		
		usuario(driver);
	}

	private static void usuario(WebDriver driver) {
		driver.findElement(By.xpath("//*[@id='usuariosMenu']")).click();
	}
}
