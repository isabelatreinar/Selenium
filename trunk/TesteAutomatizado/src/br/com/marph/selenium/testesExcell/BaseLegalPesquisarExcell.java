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

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.base.MenuBaseLegalTemplate;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class BaseLegalPesquisarExcell {
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
	public void teste() throws Exception {
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		MenuBaseLegalTemplate.prepararAcessoBaseLegal(driver);

		pesquisaEdicao();

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

	protected void pesquisaEdicao() {
		try {
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setEncoding("ISO-8859-1");
			Workbook wb = Workbook.getWorkbook(new File("./data/baseLegalPesquisa.xls"), workbookSettings);
			Sheet sheet = wb.getSheet(0);
			String tipo = sheet.getCell(0, 2).getContents();
			String numero = sheet.getCell(1, 2).getContents();
			String data = sheet.getCell(2, 2).getContents();
			String ano = sheet.getCell(3, 2).getContents();
			String tipoEditar = sheet.getCell(5, 2).getContents();
			String numeroEditar = sheet.getCell(6, 2).getContents();
			String dataEditar = sheet.getCell(7, 2).getContents();
			String anoEditar = sheet.getCell(8, 2).getContents();
			String pdf = sheet.getCell(9, 2).getContents();

			if (StringUtils.isNotBlank(tipo)) {
				WebElement tipo1 = driver.findElement(By.id("tipoBaseLegal_chosen"));
				tipo1.click();
				WebElement procuraTipoBase = driver
						.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input"));
				procuraTipoBase.sendKeys(tipo);
				procuraTipoBase.sendKeys(Keys.TAB);
			}

			if (StringUtils.isNotBlank(numero)) {
				WebElement numero1 = driver.findElement(By.id("numero"));
				numero1.sendKeys(numero);
			}
			
			if (StringUtils.isNotBlank(data)) {
				WebElement data1 = driver.findElement(By.id("dataPublicacao"));
				data1.sendKeys(data);
			}
			
			if (StringUtils.isNotBlank(ano)) {
			WebElement anoVigencia = driver.findElement(By.id("dataVigencia_chosen"));
			anoVigencia.click();
			WebElement anoVigenciaSeleciona = driver
					.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input"));
			anoVigenciaSeleciona.sendKeys(ano);
			anoVigenciaSeleciona.sendKeys(Keys.TAB);
			}
			
			WebElement pesquisar = driver.findElement(By.id("btnPesquisar"));
			pesquisar.click();

			WebElement usuario = driver.findElement(By.xpath("//td[@class='sorting_1']"));
			usuario.click();

			WebElement botaoEditar = driver.findElement(By.id("btnEditar1"));
			botaoEditar.click();
			
			if (StringUtils.isNotBlank(tipoEditar)) {
			WebElement tipo2 = driver.findElement(By.id("tipoBaseLegal_chosen"));
			tipo2.click();
			WebElement procuraTipoBase1 = driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input"));
			procuraTipoBase1.sendKeys(tipoEditar);
			}
			
			if (StringUtils.isNotBlank(numeroEditar)) {
			WebElement numero2 = driver.findElement(By.id("numero"));
			numero2.clear();
			numero2.sendKeys(numeroEditar);
			}
			
			if (StringUtils.isNotBlank(dataEditar)) {
			WebElement data2 = driver.findElement(By.id("dataPublicacao"));
			data2.clear();
			data2.sendKeys(dataEditar);
			}
			
			if (StringUtils.isNotBlank(pdf)) {
				driver.findElement(By.id("textoPublicado")).sendKeys(pdf);
				}else LogUtils.log(EnumMensagens.PDF_EM_BRANCO, this.getClass());
			
			if (StringUtils.isNotBlank(anoEditar)) {
			WebElement anoVigencia1 = driver.findElement(By.id("dataVigencia_chosen"));
			anoVigencia1.click();
			WebElement anoVigenciaSeleciona1 = driver
					.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input"));
			anoVigenciaSeleciona1.sendKeys(anoEditar);
			anoVigenciaSeleciona1.sendKeys(Keys.TAB);
			}		
			
			if ("Existe uma Deliberação cadastrada com este número."
					.equals(driver.findElement(By.xpath("//*[@id='numero_label']/label/span")).getText())) {
				LogUtils.log(EnumMensagens.DELIBERACAO_CADASTRADO, this.getClass());
			}
			
			if ("Tamanho de arquivo não suportado. Selecione um arquivo com até 5 MB."
					.equals(driver.findElement(By.xpath("//*[@id='textoPublicado_label']/label/span")).getText())) {
				LogUtils.log(EnumMensagens.PDF_MAIOR, this.getClass());
			}
			
			WebElement salvar = driver.findElement(By.id("btnSalvar"));
			salvar.click();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
