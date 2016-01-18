package br.com.marph.selenium.indicador;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.enums.EnumAcesso;
import br.com.marph.selenium.utils.AcessoUtils;

public class MenuIndicadorTemplate {
	public static void prepararAcessoIndicador(WebDriver driver) {
		AcessoUtils.idClick(driver, "closeModalHome", "btnEntradaSistemaID","btnAcessar","confirmarDados",EnumAcesso.ADMINISTRADOR.getId(),"acessarSistema");
		
		AcessoUtils.xpathClick(driver, "//td[@onmouseup='cmItemMouseUp (this,2)']");
		
		indicador(driver);
	}

	private static void indicador(WebDriver driver) {
		driver.findElement(By.xpath("//*[@id='indicadoresMenu']")).click();
	}
}
