package br.com.marph.geicom.enums;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.geicom.util.Conexao;

public class AcessoAdministrador {

	private WebDriver driver;

	@Before
		public void startUp(){
		driver = new FirefoxDriver();
	}
	
	/*@After
	public void driverClose(){
		driver.quit();
	}*/
	
	@Test
	public void browser(){
		Conexao.acessarUrl(driver);
		 Conexao.acessarSistema(driver);
		/*WebElement botaoEnviar = driver.findElement(By.id("btnAcessar"));
		    botaoEnviar.click();*/
	}
		

}
