package br.com.marph.selenium.testesExcell;

import java.io.File;
import java.io.IOException;
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

import br.com.marph.selenium.base.MenuBaseLegalTemplate;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;
import br.com.marph.selenium.validacao.ValidaToast;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

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

		WebElement botaoCadastrar = driver.findElement(By.id("btnNovaBaseLegal"));
		botaoCadastrar.click();

		cadastro();
		if (driver.findElement(By.xpath("//ol[@class='breadcrumb small']")).getText()
				.equalsIgnoreCase("Você está em: Base Legal > Nova Base Legal")) {
			validar();
		}

		if (!driver.findElement(By.xpath("//ol[@class='breadcrumb small']")).getText()
				.equalsIgnoreCase("Você está em: Base Legal > Nova Base Legal")) {
			ValidaToast.valida(driver);
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

	protected void validar() throws TesteAutomatizadoException {
		try {
			driver.findElement(By.id("numero")).click();
			if ("Existe tipo de base legal cadastrado com esse número"
					.equalsIgnoreCase(driver.findElement(By.xpath("//*[@id='numero_maindiv']/div")).getText())) {
				throw new TesteAutomatizadoException(EnumMensagens.BASE_LEGAL_JA_CADASTRADA, this.getClass());
			} else {
				throw new TesteAutomatizadoException(EnumMensagens.PDF_MAIOR, this.getClass());
			}
		} catch (NoSuchElementException e) {

		}
	}

	protected void cadastro() throws TesteAutomatizadoException {

		WorkbookSettings workbookSettings = new WorkbookSettings();
		workbookSettings.setEncoding("ISO-8859-1");
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(new File("./data/baseLegal.xls"), workbookSettings);
		} catch (BiffException | IOException e) {

			e.printStackTrace();
		}
		Sheet sheet = wb.getSheet(0);
		String tipo = sheet.getCell(0, 1).getContents();
		String numero = sheet.getCell(1, 1).getContents();
		String data = sheet.getCell(2, 1).getContents();
		String ano = sheet.getCell(3, 1).getContents();
		String pdf = sheet.getCell(4, 1).getContents();

		if (StringUtils.isNotBlank(tipo)) {
			driver.findElement(By.id("tipoBaseLegal_chosen")).click();
			driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(tipo);
			driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(Keys.TAB);
		} else
			throw new TesteAutomatizadoException(EnumMensagens.TIPO_EM_BRANCO, this.getClass());

		if (StringUtils.isNotBlank(numero)) {
			driver.findElement(By.id("numero")).sendKeys(numero);
		} else
			throw new TesteAutomatizadoException(EnumMensagens.NUMERO_EM_BRANCO, this.getClass());

		if (StringUtils.isNotBlank(data)) {
			driver.findElement(By.id("dataPublicacao")).sendKeys(data);
		} else
			throw new TesteAutomatizadoException(EnumMensagens.DATA_EM_BRANCO, this.getClass());

		if (StringUtils.isNotBlank(ano)) {
			driver.findElement(By.id("dataVigencia_chosen")).click();
			driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys(ano);
			driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys(Keys.TAB);
		} else
			throw new TesteAutomatizadoException(EnumMensagens.ANO_EM_BRANCO, this.getClass());

		if (StringUtils.isNotBlank(pdf)) {
			driver.findElement(By.id("textoPublicado")).sendKeys(pdf);
		} else
			throw new TesteAutomatizadoException(EnumMensagens.PDF_EM_BRANCO, this.getClass());

		driver.findElement(By.id("btnSalvar")).click();

	}
}
