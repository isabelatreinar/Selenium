package br.com.marph.selenium.testeExclusao;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import br.com.marph.selenium.blocoDeFinanciamento.MenuBlocoTemplate;
import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagensLog;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class VisualizarBloco {
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	
	@Before
	public void startBrowser() {
		driver = new FirefoxDriver();
		Conexao.ip(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Test
	public void realizaBusca() throws InterruptedException, TesteAutomatizadoException {

		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		
		AcessoSistema.perfilAdministrador(driver);

		MenuBlocoTemplate.menuBlocoFinanciamento(driver);
		
		PesquisarBloco.pesquisar(driver);
		
		visualizar(driver);
		
		float tempoGasto = (System.currentTimeMillis() - timestart);
		float tempoSegundos = tempoGasto / 1000;

		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos - FINALIZADO COM SUCESSO\n");

		if (tempoSegundos > 5000) {
			log.warn(sb.toString() + "\n");
		} else {
			log.info(sb.toString() + "\n");
		}
	}
	
	public static void visualizar (WebDriver driver) throws TesteAutomatizadoException {
		try {
			
			WebElement linha = driver.findElement(By.xpath("//*[@id='rowId7']/td[1]"));
			Actions action = new Actions(driver);
			action.moveToElement(linha).click().perform();
		//	driver.findElement(By.id("btnAcaoLinha-0")).click();
		//	driver.findElement(By.id("btnAcaoItem-0-0")).click();
		} catch (NoSuchElementException e) {
			throw new TesteAutomatizadoException(EnumMensagensLog.BLOCO_NAO_ENCONTRADO,VisualizarBloco.class);
		}
	}
}
