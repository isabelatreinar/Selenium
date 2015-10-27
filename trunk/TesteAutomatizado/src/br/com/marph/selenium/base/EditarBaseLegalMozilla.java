package br.com.marph.selenium.base;

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
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class EditarBaseLegalMozilla {
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
	public void realizaCadastro() throws Exception {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuBaseLegalTemplate.prepararAcessoBaseLegal(driver);
		
		PesquisarBaseLegal.pesquisar(driver);
		
		VisualizarBaseLegal.visualizar(driver);

		edicaoCampos();

		validacao();

		boolean validar = driver.findElement(By.id("toast-container")).isDisplayed();

		if (validar == true) {
			LogUtils.log(EnumMensagens.BASE_LEGAL_VALIDADO, this.getClass());
		} else {
			throw new TesteAutomatizadoException(EnumMensagens.BASE_LEGAL_NAO_VALIDADO, this.getClass());
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

	private void validacao() throws TesteAutomatizadoException {
		if ("Obrigatório!"
				.equals(driver.findElement(By.xpath("//*[@id='tipoBaseLegal_label']/label/span")).getText())) {
			throw new TesteAutomatizadoException(EnumMensagens.TIPO_EM_BRANCO, this.getClass());
		}

		if ("Obrigatório!".equalsIgnoreCase(driver.findElement(By.xpath("//*[@id='numero_label']/label/span")).getText())) {
			throw new TesteAutomatizadoException(EnumMensagens.NUMERO_EM_BRANCO, this.getClass());
		}

		if ("Existe tipo de base legal cadastrado com esse número"
				.equalsIgnoreCase(driver.findElement(By.xpath("//*[@id='numero_label']/label/span")).getText())) {
			throw new TesteAutomatizadoException(EnumMensagens.DELIBERACAO_CADASTRADO, this.getClass());
		}

		if ("Obrigatório!"
				.equalsIgnoreCase(driver.findElement(By.xpath("//*[@id='dataPublicacao_label']/label/span")).getText())) {
			throw new TesteAutomatizadoException(EnumMensagens.DATA_EM_BRANCO, this.getClass());
		}

		if ("Obrigatório!".equalsIgnoreCase(driver.findElement(By.xpath("//*[@id='dataVigencia_label']/label/span")).getText())) {
			throw new TesteAutomatizadoException(EnumMensagens.DATA_EM_BRANCO, this.getClass());
		}

		if ("Obrigatório!"
				.equalsIgnoreCase(driver.findElement(By.xpath("//*[@id='textoPublicado_label']/label/span")).getText())) {
			throw new TesteAutomatizadoException(EnumMensagens.PDF_EM_BRANCO, this.getClass());
		}

		if ("Tamanho de arquivo não suportado. Selecione um arquivo com até 5 MB."
				.equalsIgnoreCase(driver.findElement(By.xpath("//*[@id='textoPublicado_label']/label/span")).getText())) {
			throw new TesteAutomatizadoException(EnumMensagens.PDF_MAIOR, this.getClass());
		}
	}

	private void edicaoCampos() {

		WebElement btnEditar = driver.findElement(By.id("btnEditar1"));
		btnEditar.click();

		WebElement TipoBase = driver.findElement(By.id("tipoBaseLegal_chosen"));
		TipoBase.click();		
		WebElement procuraTipoBase = driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input"));
		procuraTipoBase.sendKeys("Resolução");// numero 2
		procuraTipoBase.sendKeys(Keys.TAB);

		WebElement numero = driver.findElement(By.id("numero"));
		numero.clear();
		numero.sendKeys("500");

		WebElement data = driver.findElement(By.id("dataPublicacao"));
		data.clear();
		data.sendKeys("-22092015");
		data.sendKeys(Keys.TAB);

		driver.findElement(By.id("textoPublicado")).sendKeys("C:\\Users\\rafael.sad\\Documents\\TESTEEE.pdf");

		WebElement anoVigencia = driver.findElement(By.id("dataVigencia_chosen"));
		anoVigencia.click();
		WebElement anoVigenciaSeleciona = driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input"));
		anoVigenciaSeleciona.sendKeys("2019");
		anoVigenciaSeleciona.sendKeys(Keys.TAB);
		WebElement salvar = driver.findElement(By.id("btnSalvar"));
		salvar.click();
	}
}
