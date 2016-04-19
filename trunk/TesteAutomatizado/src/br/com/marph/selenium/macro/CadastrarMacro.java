package br.com.marph.selenium.macro;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;

public class CadastrarMacro {
	
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	
	@Before
	public void startBrowser() {
		System.setProperty("webdriver.ie.driver", "C:\\IEDriverServer.exe");
		driver = new InternetExplorerDriver();
		Conexao.ip(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		AcessoSistema.acesso(driver);
	}
	
	@Test
	public void cadastrar() throws TesteAutomatizadoException {
		driver.findElement(By.xpath("//*[@id='MacroSubMenu']")).click();
	}

}
