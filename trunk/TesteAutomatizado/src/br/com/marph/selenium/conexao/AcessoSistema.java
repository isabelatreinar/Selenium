package br.com.marph.selenium.conexao;

import org.openqa.selenium.WebDriver;
import br.com.marph.selenium.enums.EnumAcesso;
import br.com.marph.selenium.utils.AcessoUtils;

public class AcessoSistema {
	public static void perfilAdministrador(WebDriver driver) {
		AcessoUtils.idClick(driver, "closeModalHome", "btnEntradaSistemaID", "btnAcessar", "confirmarDados",
				EnumAcesso.ADMINISTRADOR.getId(), "acessarSistema");
	}
}
