package br.com.marph.selenium.usuario;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.utils.AcessoUtils;

public class MenuUsuarioTemplate {
	private MenuUsuarioTemplate() {
	}

	public static void prepararAcessoUsuario(WebDriver driver) {
		
		AcessoUtils.xpathClick(driver, "//td[@onmouseup='cmItemMouseUp (this,3)']");
		
		driver.findElement(By.xpath("//*[@id='usuariosMenu']")).click();
		
}

	}
