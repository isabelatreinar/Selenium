package br.com.marph.selenium.programa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.enums.EnumAcesso;
import br.com.marph.selenium.utils.AcessoUtils;

public class MenuProgramaTemplate {
	private MenuProgramaTemplate(){}
	
	public static void prepararAcessoPrograma(WebDriver driver) {
		AcessoUtils.acessaId(driver, "closeModalHome", "btnEntradaSistemaID","btnAcessar","confirmarDados",EnumAcesso.ADMINISTRADOR.getId(),"acessarSistema");
		
		AcessoUtils.acessaXpath(driver, "//td[@onmouseup='cmItemMouseUp (this,2)']");
		
		programa(driver);
	}

	private static void programa(WebDriver driver) {
		driver.findElement(By.xpath("//*[@id='programaMenu']")).click();
	}
}
