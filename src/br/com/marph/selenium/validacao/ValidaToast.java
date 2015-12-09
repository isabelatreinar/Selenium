package br.com.marph.selenium.validacao;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class ValidaToast {
	public static void valida(WebDriver driver) throws TesteAutomatizadoException {
		boolean validar = driver.findElement(By.id("toast-container")).isDisplayed();

		if (validar == true) {
			LogUtils.log(EnumMensagens.BASE_LEGAL_VALIDADO, ValidaToast.class);
		} else {
			throw new TesteAutomatizadoException(EnumMensagens.BASE_LEGAL_NAO_VALIDADO, ValidaToast.class);
		}
	}
}
