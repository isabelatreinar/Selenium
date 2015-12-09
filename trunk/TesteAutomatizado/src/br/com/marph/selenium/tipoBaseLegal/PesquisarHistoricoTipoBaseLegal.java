package br.com.marph.selenium.tipoBaseLegal;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.utils.LogUtils;

public class PesquisarHistoricoTipoBaseLegal {
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
	public void visualizarHistoricoTipoBaseLegal() {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessar menu Cadastro > Tipo Base Legal
		MenuTipoBaseLegalTemplate.prepararAcessoTipoBaseLegal(driver);

		// Pesquisar Tipo de Base Legal
		PesquisarTipoBaseLegal.pesquisar(driver);

		// Visualizar Tipo de Base Legal
		VisualizarTipoBaseLegal.visualizar(driver);

		// Visualizar tela de Histórico de base legal
		VisualizarHistoricoTipoBaseLegal.visualizar(driver);

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

	public static void pesquisar(WebDriver driver) {
		WebElement pesquisa = driver.findElement(By.id("btnExpandirPesquisaAvancada"));
		pesquisa.click();

		WebElement dataInicial = driver.findElement(By.id("dataInicialHistorico"));
		dataInicial.sendKeys("-20102015");

		WebElement dataFinal = driver.findElement(By.id("dataInicialHistorico"));
		dataFinal.sendKeys("-25102015");

		WebElement campoAlterado = driver.findElement(By.xpath("//div[@id='camposTipoBaseLegal_chosen']/ul/li/input"));
		campoAlterado.click();
		campoAlterado.sendKeys("nome");
		campoAlterado.sendKeys(Keys.ENTER);

		driver.findElement(By.id("usuariosAlteracao_chosen")).click();
		WebElement modificadoPor = driver.findElement(By.xpath("//div[@id='usuariosAlteracao_chosen']/div/div/input"));
		modificadoPor.click();
		modificadoPor.sendKeys("Usuário Marph");
		modificadoPor.sendKeys(Keys.ENTER);

		WebElement btnPesquisar = driver.findElement(By.id("btnPesquisar"));
		btnPesquisar.click();

	}
}
