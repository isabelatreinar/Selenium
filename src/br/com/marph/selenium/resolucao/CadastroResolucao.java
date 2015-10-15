package br.com.marph.selenium.resolucao;

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

public class CadastroResolucao {
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
	public void realizaBusca() {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuResolucaoTemplate.prepararAcessoResolucao(driver);

		cadastrarResolucao();

		WebElement avancar = driver.findElement(By.id("btnProximo"));
		avancar.click();

		beneficiarios();

		WebElement avancar1 = driver.findElement(By.id("btnProximo"));
		avancar1.click();

		indicadores();

		float tempoGasto = (System.currentTimeMillis() - timestart);
		float tempoSegundos = tempoGasto / 1000;

		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos");

		if (tempoSegundos > 5000) {
			log.warn(sb.toString() + "\n");
		} else {
			log.info(sb.toString() + "\n");
		}
	}

	protected void indicadores() {

		WebElement criar = driver.findElement(By.id("criar"));
		criar.click();

		WebElement nome = driver.findElement(By.id("nome"));
		nome.sendKeys("Teste");

		WebElement btnIndicador = driver.findElement(By.xpath("//*[@class='_cadastroIndicador']"));
		btnIndicador.click();

		WebElement indicador = driver.findElement(By.xpath("//*[@data-label-field='descricao']"));
		indicador.sendKeys("Taxa");
		WebElement indicador1 = driver.findElement(By.id("ui-id-2"));
		indicador1.click();

		WebElement ponto = driver.findElement(By.xpath("//*[@id='tabelaIndicadoresNovo']/div[2]/div[3]/input"));
		ponto.sendKeys("5000");

		WebElement peso = driver.findElement(By.xpath("//*[@id='tabelaIndicadoresNovo']/div[2]/div[4]/input"));
		peso.sendKeys("10000");

		WebElement preRequisito = driver.findElement(By.xpath("//*[@id='tabelaIndicadoresNovo']/div[2]/div[5]/a"));
		preRequisito.click();

		WebElement salvar = driver.findElement(By.xpath("//*[@id='headingNovo']/ul/li[1]/a"));
		salvar.click();

	}

	protected void beneficiarios() {
		WebElement upload = driver.findElement(By.id("uploadBeneficiariosContemplados"));
		upload.sendKeys("C:\\Users\\rafael.sad\\Documents\\beneficiarioExport.xls");

		WebElement importar = driver.findElement(By.id("buttonImportar"));
		importar.click();
	}

	private void cadastrarResolucao() {
		WebElement cadastrarResolucao = driver.findElement(By.id("btnNovaResolucao"));
		cadastrarResolucao.click();
		// Selecionar programa
		WebElement programaSelecionar = driver.findElement(By.id("programa_chosen"));
		programaSelecionar.click();
		WebElement programa = driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input"));
		programa.sendKeys("farmácia de minas");
		programa.sendKeys(Keys.TAB);
		// fim

		// numero resolucao
		WebElement numero = driver.findElement(By.id("baseLegal"));
		numero.sendKeys("123");
		WebElement numeroSeleciona = driver.findElement(By.xpath("//li[@id='ui-id-2']"));
		numeroSeleciona.click();
		// fim
		// base legal
		WebElement selecionarBase = driver.findElement(By.id("termosBaseLegal_chosen"));
		selecionarBase.click();
		WebElement base = driver.findElement(By.xpath("//*[@id='termosBaseLegal_chosen']/div/ul/li[2]"));
		base.click();
		// fim
		WebElement tempo = driver.findElement(By.id("tempoVigencia"));
		tempo.sendKeys("25");

		WebElement descricao = driver.findElement(By.id("descricao"));
		descricao.sendKeys("Teste TESTE");

		WebElement salvar = driver.findElement(By.id("btnSalvar1"));
		salvar.click();
	}
}
