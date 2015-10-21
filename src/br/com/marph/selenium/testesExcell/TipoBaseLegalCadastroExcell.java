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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.tipoBaseLegal.MenuTipoBaseLegalTemplate;
import br.com.marph.selenium.utils.LogUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class TipoBaseLegalCadastroExcell {
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

		MenuTipoBaseLegalTemplate.prepararAcessoTipoBaseLegal(driver);
		WebElement botaoCadastrar = driver.findElement(By.id("btnNovoTipoBaseLegal"));
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
			LogUtils.log(EnumMensagens.TIPO_BASE_LEGAL_VALIDADO, this.getClass());
		} else {
			throw new TesteAutomatizadoException(EnumMensagens.TIPO_BASE_LEGAL_NAO_VALIDO, this.getClass());
		}
	}

	protected void cadastro() throws TesteAutomatizadoException {
		/**@author rafael.sad
		 * Try catch para tratar a leitura dos dados na planilha e para
		 * alimentar os campos a serem testados no brownser.
		 */

		try {
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setEncoding("ISO-8859-1");
			Workbook wb = Workbook.getWorkbook(new File("./data/tipoBaseLegal.xls"), workbookSettings);
			Sheet sheet = wb.getSheet(0);
			String nome = sheet.getCell(0,1).getContents();
			String transferencia = sheet.getCell(1,1).getContents();
			String prestacaoMetas = sheet.getCell(2,1).getContents();
			String prestacaoContas = sheet.getCell(3,1).getContents();
			String descricao = sheet.getCell(4,1).getContents();
			

			if (StringUtils.isNotBlank(nome)) {
				WebElement nomeCampo = driver.findElement(By.id("nomeTipoBaseLegal"));
				nomeCampo.sendKeys(nome);
			} else
				LogUtils.log(EnumMensagens.NOME_EM_BRANCO, this.getClass());

			if (StringUtils.isNotBlank(transferencia)) {
				WebElement transferenciaCampo = driver.findElement(By.id("transferenciaRecursosFinanceiros_chosen"));
				transferenciaCampo.click();
				WebElement selecionarTransferencia = driver
						.findElement(By.xpath("//*[@id='transferenciaRecursosFinanceiros_chosen']/div/div/input"));
				selecionarTransferencia.sendKeys(transferencia);
				selecionarTransferencia.sendKeys(Keys.TAB);
			} else
				throw new TesteAutomatizadoException(EnumMensagens.TRANSFERENCIA_DE_RECURSOS_EM_BRANCO, this.getClass());

			if (StringUtils.isNotBlank(prestacaoMetas)) {
				WebElement prestacaoMetasClica = driver.findElement(By.id("prestacaoMetas_chosen"));
				prestacaoMetasClica.click();
				WebElement prestacaoMetasPreenche = driver.findElement(By.xpath("//*[@id='prestacaoMetas_chosen']/div/div/input"));
				prestacaoMetasPreenche.sendKeys(prestacaoMetas);
				prestacaoMetasPreenche.sendKeys(Keys.TAB);
			}else 
				throw new TesteAutomatizadoException(EnumMensagens.PRESTACAO_DE_METAS_EM_BRANCO, this.getClass());

			if (StringUtils.isNotBlank(descricao)) {
				WebElement descricaoPreenche = driver.findElement(By.id("descricao"));
				descricaoPreenche.sendKeys(descricao);
			}
			
			if (StringUtils.isNotBlank(prestacaoContas)) {
				WebElement prestacaoContasClica = driver.findElement(By.id("prestacaoContas_chosen"));
				prestacaoContasClica.click();
				WebElement prestacaoContasSeleciona = driver.findElement(By.xpath("//*[@id='prestacaoContas_chosen']/div/div/input"));
				prestacaoContasSeleciona.sendKeys(prestacaoContas);
				prestacaoContasSeleciona.sendKeys(Keys.TAB);
			} else
				throw new TesteAutomatizadoException(EnumMensagens.EXTENSAO_EM_BRANCO, this.getClass());
			
			WebElement salvar = driver.findElement(By.id("btnSalvar"));
			salvar.click();

		} catch (BiffException | IOException e) {
			e.printStackTrace();
		}
	}
}
