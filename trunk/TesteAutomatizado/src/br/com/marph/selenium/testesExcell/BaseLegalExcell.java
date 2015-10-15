package br.com.marph.selenium.testesExcell;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.base.MenuBaseLegalTemplate;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class BaseLegalExcell {
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
	public void teste() throws Exception {
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		MenuBaseLegalTemplate.prepararAcessoBaseLegal(driver);

		WebElement botaoCadastrar = driver.findElement(By.id("btnNovoUsuario"));
		botaoCadastrar.click();

		cadastro();
		
		validar();

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

	protected void validar() throws TesteAutomatizadoException {
		boolean validar = driver.findElement(By.id("toast-container")).isDisplayed();

		if (validar == true) {
			LogUtils.log(EnumMensagens.BASE_LEGAL_VALIDADO, this.getClass());
		} else {
			throw new TesteAutomatizadoException(EnumMensagens.BASE_LEGAL_NAO_VALIDADO, this.getClass());
		}
	}

	protected void cadastro() {
		try {
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setEncoding("ISO-8859-1");
			Workbook wb = Workbook.getWorkbook(new File("./data/baseLegal.xls"), workbookSettings);
			Sheet sheet = wb.getSheet(0);
			String tipo = sheet.getCell(0, 1).getContents();
			String numero = sheet.getCell(1, 1).getContents();
			String data = sheet.getCell(2, 1).getContents();
			String ano = sheet.getCell(3, 1).getContents();
			String pdf = sheet.getCell(4, 1).getContents();
			
			if (StringUtils.isNotBlank(tipo)) {
			WebElement tipoBase = driver.findElement(By.id("tipoBaseLegal_chosen"));
			tipoBase.click();
			WebElement procuraTipoBase = driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input"));
			procuraTipoBase.sendKeys(tipo);
			procuraTipoBase.sendKeys(Keys.TAB);
			}else LogUtils.log(EnumMensagens.TIPO_EM_BRANCO, this.getClass());
			
			if (StringUtils.isNotBlank(numero)) {
			WebElement numero1 = driver.findElement(By.id("numero"));
			numero1.sendKeys(numero);
			}else LogUtils.log(EnumMensagens.NUMERO_EM_BRANCO, this.getClass());
			
			if (StringUtils.isNotBlank(data)) {
			WebElement data1 = driver.findElement(By.id("dataPublicacao"));
			data1.sendKeys(data);
			}else LogUtils.log(EnumMensagens.DATA_EM_BRANCO, this.getClass());
			
			if (StringUtils.isNotBlank(ano)) {
			WebElement anoVigencia = driver.findElement(By.id("dataVigencia_chosen"));
			anoVigencia.click();
			WebElement anoVigenciaSeleciona = driver
					.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input"));
			anoVigenciaSeleciona.sendKeys(ano);
			anoVigenciaSeleciona.sendKeys(Keys.TAB);
			}else LogUtils.log(EnumMensagens.ANO_EM_BRANCO, this.getClass());
			
			if (StringUtils.isNotBlank(pdf)) {
			driver.findElement(By.id("textoPublicado")).sendKeys(pdf);
			}else LogUtils.log(EnumMensagens.PDF_EM_BRANCO, this.getClass());
			
			
			WebElement salvar = driver.findElement(By.id("btnSalvar"));
			salvar.click();
			
			if ("Existe uma Deliberação cadastrada com este número."
					.equals(driver.findElement(By.xpath("//*[@id='numero_label']/label/span")).getText())) {
				LogUtils.log(EnumMensagens.DELIBERACAO_CADASTRADO, this.getClass());
			}
			
			if ("Tamanho de arquivo não suportado. Selecione um arquivo com até 5 MB."
					.equals(driver.findElement(By.xpath("//*[@id='textoPublicado_label']/label/span")).getText())) {
				LogUtils.log(EnumMensagens.PDF_MAIOR, this.getClass());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
