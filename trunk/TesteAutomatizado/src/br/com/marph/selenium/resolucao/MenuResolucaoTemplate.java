package br.com.marph.selenium.resolucao;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.enums.EnumAcesso;
import br.com.marph.selenium.utils.AcessoUtils;

public class MenuResolucaoTemplate {
	private MenuResolucaoTemplate(){}
	
	public static void prepararAcessoResolucao(WebDriver driver) {
		AcessoUtils.idClick(driver, "closeModalHome", "btnEntradaSistemaID","btnAcessar","confirmarDados",EnumAcesso.ADMINISTRADOR.getId(),"acessarSistema");
		
		AcessoUtils.xpathClick(driver, "//td[@onmouseup='cmItemMouseUp (this,3)']");
		
		resolucao(driver);
	}

	private static void resolucao(WebDriver driver) {
		driver.findElement(By.xpath("//*[@id='resolucaoMenu']")).click();
	}
}
