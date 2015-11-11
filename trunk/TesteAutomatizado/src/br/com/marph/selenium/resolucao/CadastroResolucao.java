package br.com.marph.selenium.resolucao;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
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
	public void realizaBusca() throws InterruptedException, TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuResolucaoTemplate.prepararAcessoResolucao(driver);

		cadastrarResolucao();

		if (driver.findElement(By.xpath("//*[@id='RESOLUCAO']")).getAttribute("class").equalsIgnoreCase("current")) {
			validarResolucao();
		}

		beneficiarios();

		if (driver.findElement(By.xpath("//*[@id='BENEFICIARIOS_CONTEMPLADOS']")).getAttribute("class")
				.equalsIgnoreCase("current")) {
			validarBeneficiarios();
		}

		indicadores();

		WebElement avancar2 = driver.findElement(By.id("btnProximo"));
		avancar2.click();

		periodo();

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

	private void cadastrarResolucao() throws TesteAutomatizadoException {
		WebElement cadastrarResolucao = driver.findElement(By.id("btnNovaResolucao"));
		cadastrarResolucao.click();
		// Selecionar programa
		WebElement programaSelecionar = driver.findElement(By.id("programa_chosen"));
		programaSelecionar.click();
		WebElement programa = driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input"));
		programa.sendKeys("farmácia de minas");
		programa.sendKeys(Keys.TAB);
		// fim

		// valida programa

		if (driver.findElement(By.id("programa_chosen")).isDisplayed() && driver
				.findElement(By.xpath("//*[@id='programa_chosen']/a/span")).getText().equalsIgnoreCase("Programa")) {
			throw new TesteAutomatizadoException(EnumMensagens.PROGRAMA_EM_BRANCO, this.getClass());
		}

		// fim
		// numero resolucao
		WebElement numero = driver.findElement(By.id("baseLegal"));
		numero.sendKeys("403");
		WebElement numeroSeleciona = driver.findElement(By.xpath("//li[@id='ui-id-6']"));// ALTERAR
		numeroSeleciona.click(); // NUMERO
									// PARA
									// PEGAR
									// OUTRA
									// RESOLUÇÃO
		// fim
		if (StringUtils.isBlank(driver.findElement(By.id("baseLegal-label")).getAttribute("value"))) {
			throw new TesteAutomatizadoException(EnumMensagens.NUMERO_EM_BRANCO, this.getClass());
		}

		if (StringUtils.isNotBlank(driver.findElement(By.id("baseLegal-label")).getAttribute("value"))) {
			boolean present1 = true;
			try {
				WebElement data1 = driver.findElement(By.id("baseLegal"));
				data1.click();
				driver.findElement(By.xpath("//*[@id='baseLegal_maindiv']/div")).isDisplayed();
				present1 = true;
			} catch (NoSuchElementException e) {
				present1 = false;
			}

			if (present1 == true) {
				throw new TesteAutomatizadoException(EnumMensagens.RESOLUCAO_JA_CADASTRADA, this.getClass());
			}

		}

		WebElement selecionarBase = driver.findElement(By.id("termosBaseLegal_chosen"));
		selecionarBase.click();
		WebElement base = driver.findElement(By.xpath("//*[@id='termosBaseLegal_chosen']/div/ul/li[2]"));
		base.click();

		WebElement tempo = driver.findElement(By.id("tempoVigencia"));
		tempo.sendKeys("25");

		WebElement descricao = driver.findElement(By.id("descricao"));
		descricao.sendKeys("Teste TESTE");
		/*
		 * JavascriptExecutor js = (JavascriptExecutor) driver;
		 * js.executeScript("$('#descricao').val()").toString();
		 */

		// fazer validação de descrição aqui ou na validação.
		WebElement salvar = driver.findElement(By.id("btnSalvar1"));
		salvar.click();

		WebElement avancar = driver.findElement(By.id("btnProximo"));
		avancar.click();
	}

	/**
	 * Validar resolução
	 * 
	 * @throws TesteAutomatizadoException
	 */
	protected void validarResolucao() throws TesteAutomatizadoException {

		try {
			driver.findElement(By.xpath("//*[@class='search-choice']")).isDisplayed();
		} catch (NoSuchElementException e) {
			throw new TesteAutomatizadoException(EnumMensagens.BASE_LEGAL_EM_BRANCO, this.getClass());
		}

		if (StringUtils.isBlank(driver.findElement(By.id("tempoVigencia")).getAttribute("value"))) {
			throw new TesteAutomatizadoException(EnumMensagens.TEMPO_EM_BRANCO, this.getClass());
		}
	}

	protected void beneficiarios() throws InterruptedException, TesteAutomatizadoException {
		
		  WebElement upload =
		  driver.findElement(By.id("uploadBeneficiariosContemplados"));
		  upload.sendKeys("C:\\Users\\rafael.sad\\Documents\\Export.xlsx");
		 

		WebElement importar = driver.findElement(By.id("buttonImportar"));
		importar.click();

		 Thread.sleep(1000);

		WebElement avancar1 = driver.findElement(By.id("btnProximo"));
		avancar1.click();
	}

	protected void validarBeneficiarios() throws TesteAutomatizadoException {

		driver.findElement(By.id("uploadBeneficiariosContemplados-txt")).click();

		if (driver.findElement(By.xpath("//*[@class='col-md-12 uploadFile']/div")).getText()
				.equalsIgnoreCase("Tamanho de arquivo não suportado. Selecione um arquivo com até 5 MB.")) {
			throw new TesteAutomatizadoException(EnumMensagens.PDF_MAIOR, this.getClass());
		}

		try {
			if (driver.findElement(By.id("downloadTxt")).isDisplayed()) {
				throw new TesteAutomatizadoException(EnumMensagens.PDF_ERRO_DE_LOG, this.getClass());
			}

		} catch (NoSuchElementException e) {

		}

	}

	protected void indicadores() {

		WebElement criar = driver.findElement(By.id("criar"));
		criar.click();

		WebElement nome = driver.findElement(By.id("nome"));
		nome.sendKeys("Teste");

		WebElement btnIndicador = driver.findElement(By.xpath("//*[@id='collapseNovo']/div/ul/li[1]/a"));
		btnIndicador.click();

		WebElement indicador = driver.findElement(By.xpath("//*[@data-label-field='nomeIndicador']"));
		indicador.sendKeys("uni");
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

	protected void validarToolTipIndicadores() {
		
	}
	
	public void periodo() {
		WebElement editar = driver.findElement(By.xpath("//*[@class='panel-heading']/ul/li[3]/a"));
		editar.click();

		WebElement adicionar = driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/ul/li/a"));
		adicionar.click();

		WebElement data = driver.findElement(
				By.xpath("//*[@class='panel-collapse collapse in']/div/div/div[3]/div[2]/div/div/div/input"));
		data.sendKeys("-19102015");

		WebElement dataFim = driver.findElement(
				By.xpath("//*[@class='panel-collapse collapse in']/div/div/div[3]/div[3]/div/div/div/input"));
		dataFim.sendKeys("-22102015");

		WebElement salvar = driver.findElement(By.xpath("//*[@class='panel-heading']/ul/li[1]/a"));
		salvar.click();
	}
}
