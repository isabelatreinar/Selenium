package br.com.marph.selenium.base;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.utils.LogUtils;

public class PesquisarBaseLegal {

	private final String LOG_NAME = "RAFAEL";
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
	public void realizaCadastro() {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuBaseLegalTemplate.prepararAcessoBaseLegal(driver);

		pesquisar();

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

	private void pesquisar() {
		WebElement tipo = driver.findElement(By.id("tipoBaseLegal_chosen"));
		tipo.click();

		WebElement procuraTipoBase = driver.findElement(By.xpath("//li[@data-option-array-index='1']"));
		procuraTipoBase.click();

		WebElement numero = driver.findElement(By.id("numero"));
		numero.sendKeys("654456");

		WebElement data = driver.findElement(By.id("dataPublicacao"));
		data.click();
		WebElement dataSeleciona = driver.findElement(By.xpath("//td[@class='day']"));
		dataSeleciona.click();

		WebElement anoVigencia = driver.findElement(By.id("dataVigencia_chosen"));
		anoVigencia.click();

		WebElement anoVigenciaSeleciona = driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/ul/li[1]"));
		anoVigenciaSeleciona.click();

		WebElement btnPesquisa = driver.findElement(By.id("btnPesquisar"));
		btnPesquisa.click();
	}
}
