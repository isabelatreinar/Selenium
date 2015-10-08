package br.com.marph.selenium.beneficiario;

import java.io.File;
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

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;
import jxl.Sheet;
import jxl.Workbook;

public class PesquisarHistoricoBeneficiario {
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
	public void pesquisarHistoricoBeneficiario() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessar menu
		MenuBeneficiarioTemplate.prepararAcessoBaseLegal(driver);

		// Lê e armazena os dados do Excel

		// Pesquisar Beneficiario
		PesquisarBeneficiarioMozilla.pesquisar(driver);

		// Visualizar Beneficiário
		VisualizarBeneficiario.visualizar(driver);

		// Visualizar Histórico
		visualizarHistorico();

		
		// Pesquisar (Exibir pesquisa e pesquisar)
		pesquisar();

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

	public void visualizarHistorico() {
		WebElement btnHistorico = driver.findElement(By.id("btnHistorico1"));
		btnHistorico.click();
	}

	public void pesquisar() {
		try {
			Workbook wb = Workbook.getWorkbook(new File("./data/baseLegalPesquisa.xls"));
			Sheet sheet = wb.getSheet(0);
			String dataInicial = sheet.getCell(0, 2).getContents();
			String dataFinal = sheet.getCell(1, 2).getContents();
			String campoAlterado = sheet.getCell(2, 2).getContents();
			String modificadoPor = sheet.getCell(2, 2).getContents();

			WebElement exibirPesquisa = driver.findElement(By.xpath("//button[@class='btn btCollapseOpen']"));
			exibirPesquisa.click();

			WebElement pDataInicial = driver.findElement(By.id("dataInicialHistorico"));
			pDataInicial.sendKeys(dataInicial);
			
			WebElement pDataFinal = driver.findElement(By.id("dataFinalHistorico"));
			pDataFinal.sendKeys(dataFinal);

			WebElement pCampoAlterado = driver.findElement(By.id("camposBeneficiario_chosen"));
			pCampoAlterado.click();
			pCampoAlterado.sendKeys(campoAlterado);
			pCampoAlterado.sendKeys(Keys.TAB);
			
			// verificar
			WebElement pModificadoPor = driver.findElement(By.id("usuariosAlteracao_chosen"));
			pModificadoPor.click();
			WebElement modificadoPorSeleciona = driver.findElement(By.xpath("//*[@id='usuariosAlteracao_chosen']/div/ul/li"));
			modificadoPorSeleciona.click();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
