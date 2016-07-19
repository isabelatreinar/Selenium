package br.com.marph.geicom.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Conexao {

	WebElement driver;

	public static void acessarUrl(WebDriver driver) {
		driver.get("http://172.16.10.115:8081/public/login");

	}

	public static void acessarSistema(WebDriver driver) {
		driver.findElement(By.id("btnAcessar")).click();
		driver.findElement(By.id("confirmaDados")).click();
	}

}
