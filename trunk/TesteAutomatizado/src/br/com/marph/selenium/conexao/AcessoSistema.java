package br.com.marph.selenium.conexao;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.enums.EnumAcesso;
import br.com.marph.selenium.utils.AcessoUtils;

public class AcessoSistema {
	public static void acesso(WebDriver driver) {

		AcessoUtils.idClick(driver, "btnEntradaSistemaID", "btnAcessar", "confirmarDados",
				EnumAcesso.ADMINISTRADOR.getId(), "acessarSistema");
		
		

		AcessoUtils.xpathClick(driver, "//td[@onmouseup='cmItemMouseUp (this,3)']");

	}
}
