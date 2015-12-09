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
import org.openqa.selenium.WebDriver;
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
		
		if (! driver.findElement(By.xpath("//ol[@class='breadcrumb small']")).getText()
				.equalsIgnoreCase("Você está em: Base Legal > Visualizar Base Legal > Editar Base Legal")) {
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

	protected void pesquisaEdicao() throws TesteAutomatizadoException {
		
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setEncoding("ISO-8859-1");
			Workbook wb = null;
			try {
				wb = Workbook.getWorkbook(new File("./data/baseLegalPesquisa.xls"), workbookSettings);
			} catch (BiffException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
				driver.findElement(By.id("tipoBaseLegal_chosen")).click();
				driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(tipo);
				driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(Keys.TAB);
			}

			if (StringUtils.isNotBlank(numero)) {
				driver.findElement(By.id("numero")).sendKeys(numero);
			}

			if (StringUtils.isNotBlank(data)) {
				driver.findElement(By.id("dataPublicacao")).sendKeys(data);
			}

			if (StringUtils.isNotBlank(ano)) {
				driver.findElement(By.id("dataVigencia_chosen")).click();
				driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys(ano);
				driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys(Keys.TAB);
			}

			driver.findElement(By.id("btnPesquisar")).click();
			
			if(driver.findElement(By.xpath("//*[@id='baseLegalDataTable']/tbody/tr/td")).getText().equalsIgnoreCase("Resultado não encontrado.")){
				throw new TesteAutomatizadoException(EnumMensagens.RESULTADO_NAO_ENCONTRADO, this.getClass());
			}else{
				driver.findElement(By.xpath("//td[@class='sorting_1']")).click();
			}			
			
			//editar
			driver.findElement(By.id("btnEditar1")).click();

			if (StringUtils.isNotBlank(tipoEditar)) {
				driver.findElement(By.id("tipoBaseLegal_chosen")).click();
				driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(tipoEditar);
			}

			if (StringUtils.isNotBlank(numeroEditar)) {
				driver.findElement(By.id("numero")).clear();
				driver.findElement(By.id("numero")).sendKeys(numeroEditar);
			}

			if (StringUtils.isNotBlank(dataEditar)) {
				driver.findElement(By.id("dataPublicacao")).clear();
				driver.findElement(By.id("dataPublicacao")).sendKeys(dataEditar);
			}

			if (StringUtils.isNotBlank(pdf)) {
				driver.findElement(By.id("textoPublicado")).sendKeys(pdf);
			} else
				LogUtils.log(EnumMensagens.PDF_EM_BRANCO, this.getClass());

			if (StringUtils.isNotBlank(anoEditar)) {
				driver.findElement(By.id("dataVigencia_chosen")).click();
				driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys(anoEditar);
				driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys(Keys.TAB);
			}

			driver.findElement(By.id("btnSalvar")).click();
		
	}
}
