package br.com.marph.selenium.resolucao;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class VerificarResolucaoCorreta {
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
	public void pesquisarBaseLegal() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuResolucaoTemplate.prepararAcessoResolucao(driver);

		PesquisarResolucao.pesquisar(driver);
		
		String numero = driver.findElement(By.xpath("//td[@class='sorting_1']")).getText();
		
		//seleciona
		driver.findElement(By.xpath("//td[@class='sorting_1']")).click();
		
		EditarResolucao.pegaAba(driver);
		
		String numeroCompara = driver.findElement(By.id("baseLegal")).getAttribute("value");

		if (!numero.equals(numeroCompara)) {
			throw new TesteAutomatizadoException(EnumMensagens.RESOLUCAO_ERRADA, this.getClass());
		} else {
			WebElement voltar = driver.findElement(By.id("btnIrParaListagem"));
			voltar.click();
			
			WebElement sim = driver.findElement(By.xpath("/html/body/div[5]/div[2]/div/div/div/div/div[4]/button[1]"));
			sim.click();
			
		}

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

	
}
