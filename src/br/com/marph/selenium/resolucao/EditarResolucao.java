package br.com.marph.selenium.resolucao;

import java.util.List;
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
import br.com.marph.selenium.utils.LogUtils;

public class EditarResolucao {
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
	public void realizaEdicao() {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuResolucaoTemplate.prepararAcessoResolucao(driver);

		PesquisarResolucao.pesquisar(driver);

		VisualizarResolucao.visualiza(driver);

		pegaAba(driver);

		editarResolucao(driver);

		editarBeneficiario(driver);

		editarIndicadores(driver);

		editarPeriodo(driver);

		WebElement voltar = driver.findElement(By.id("liIrParaListagem"));
		voltar.click();

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

	public static void editarPeriodo(WebDriver driver) {
		/**
		 * Edita Aba de periodo de monitoramento
		 */
		WebElement editar = driver.findElement(By.xpath("//*[@id='heading222']/ul/li[3]/a"));
		editar.click();

		WebElement data1 = driver
				.findElement(By.xpath("//*[@id='collapse222']/div/div/div[2]/div[3]/div/div/div/input"));
		data1.clear();
		data1.sendKeys("-25102015");

		WebElement data2 = driver
				.findElement(By.xpath("//*[@id='collapse222']/div/div/div[2]/div[3]/div/div/div/input"));
		data2.clear();
		data2.sendKeys("-31102015");

		WebElement salvar = driver.findElement(By.xpath("//*[@id='heading222']/ul/li[1]/a"));
		salvar.click();
	}

	public static void editarIndicadores(WebDriver driver) {
		/**
		 * Edita aba de Indicadores
		 */
		driver.findElement(By.xpath("//*[@class='panel-heading']/button")).click();
		
		WebElement editar = driver.findElement(By.xpath("//*[@class='panel-heading']/ul/li[3]/a"));
		editar.click();

		WebElement ponto = driver.findElement(By.xpath("//*[@class='tabelaIndicadores]/div[2]/div[3]/input"));
		ponto.clear();
		ponto.sendKeys("6000");

		WebElement peso = driver.findElement(By.xpath("//*[@class='tabelaIndicadores]/div[2]/div[4]/input"));
		peso.clear();
		peso.sendKeys("10000");

		WebElement preRequisito = driver.findElement(By.xpath("//*[@class='tabelaIndicadores']/div[2]/div[5]/a"));
		preRequisito.click();

		WebElement salvar = driver.findElement(By.xpath("//*[@class='panel-heading']/ul/li[1]/a"));
		salvar.click();

		WebElement proximo = driver.findElement(By.id("btnProximo"));
		proximo.click(); 
 
	} 

	public static void editarBeneficiario(WebDriver driver) {
		/**
		 * Importa planilha na aba de beneficiario fazendo edição
		 */
		WebElement upload = driver.findElement(By.id("uploadBeneficiariosContemplados"));
		upload.sendKeys("C:\\Users\\rafael.sad\\Documents\\Export.xlsx");

		WebElement importar = driver.findElement(By.id("buttonImportar"));
		importar.click();

		WebElement proximo = driver.findElement(By.id("btnProximo"));
		proximo.click();

	}

	public static void editarResolucao(WebDriver driver) {
		/**
		 * Edita aba de resolução
		 */
		WebElement selecionarBase = driver.findElement(By.id("termosBaseLegal_chosen"));
		selecionarBase.click();
		WebElement base = driver.findElement(By.xpath("//*[@id='termosBaseLegal_chosen']/div/ul/li[5]"));
		base.click();

		WebElement tempo = driver.findElement(By.id("tempoVigencia"));
		tempo.clear();
		tempo.sendKeys("10");

		WebElement descricao = driver.findElement(By.id("descricao"));
		descricao.clear();
		descricao.sendKeys("Teste ");

		WebElement salvar = driver.findElement(By.id("btnSalvar1"));
		salvar.click();

		WebElement proximo = driver.findElement(By.id("btnProximo"));
		proximo.click();

	}

	public static void pegaAba(WebDriver driver) {
		/**
		 * Pega em qual aba esta e volta para a primeira que é a aba de
		 * Resolução
		 */
		int valor = 0;
		List<WebElement> wizard = driver.findElements(By.xpath("//*[@id='wizard']/ul/li"));

		for (int i = 0; i < wizard.size(); i++) {
			if ("current".equals(wizard.get(i).getAttribute("class"))) {
				valor = i;
			}
		}

		if (valor == 1) {
			driver.findElement(By.id("btnAnterior")).click();
		} else if (valor == 2) {
			driver.findElement(By.id("btnAnterior")).click();
			driver.findElement(By.id("btnAnterior")).click();
		} else if (valor == 3) {
			driver.findElement(By.id("btnAnterior")).click();
			driver.findElement(By.id("btnAnterior")).click();
			driver.findElement(By.id("btnAnterior")).click();
		} else if (valor == 4) {
			driver.findElement(By.id("btnAnterior")).click();
			driver.findElement(By.id("btnAnterior")).click();
			driver.findElement(By.id("btnAnterior")).click();
			driver.findElement(By.id("btnAnterior")).click();
		}
	}
}
