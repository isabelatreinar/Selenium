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
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.municipio.MenuMunicipioTemplate;
import br.com.marph.selenium.utils.LogUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class MunicipioPesquisa {
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);

	@Before
	public void startBrowser(){
		driver = new FirefoxDriver();
		Conexao.ip(driver); 
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);		
	}
	
	@Test	
	public void teste(){
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		
		MenuMunicipioTemplate.prepararAcessoBaseLegal(driver);

		try {
			WorkbookSettings workbookSettings = new WorkbookSettings();
		    workbookSettings.setEncoding("ISO-8859-1");
			Workbook wb= Workbook.getWorkbook(new File("./data/municipioPesquisa.xls"), workbookSettings);
			Sheet sheet = wb.getSheet(0);
			String nome = sheet.getCell(0,1).getContents();
			String unidade = sheet.getCell(1,1).getContents();
			String macro = sheet.getCell(2,1).getContents();
			String micro = sheet.getCell(3,1).getContents();
			
			if (StringUtils.isNotBlank(nome)) {
			WebElement nomePesquisar = driver.findElement(By.id("nome"));
			nomePesquisar.sendKeys(nome);
			}
			
			if (StringUtils.isNotBlank(unidade)) {
			WebElement unidadeRegional = driver.findElement(By.id("unidadeRegional_chosen"));
			unidadeRegional.click();
			WebElement procuraTipoRegional = driver.findElement(By.xpath("//*[@id='unidadeRegional_chosen']/div/div/input"));
			procuraTipoRegional.sendKeys(unidade);
			procuraTipoRegional.sendKeys(Keys.TAB);
			}
			
			WebElement abrir = driver.findElement(By.xpath("//*[@id='btnExpandirPesquisaAvancada']"));
			abrir.click();
			
			if (StringUtils.isNotBlank(macro)) {
			WebElement macroSeleciona = driver.findElement(By.id("macros_chosen"));
			macroSeleciona.click();
			WebElement macroEscreve = driver.findElement(By.xpath("//*[@id='macros_chosen']/div/div/input"));
			macroEscreve.sendKeys(macro);
			macroEscreve.sendKeys(Keys.TAB);
			}
			
			if (StringUtils.isNotBlank(micro)) {
			WebElement microSeleciona = driver.findElement(By.id("micro_chosen"));
			microSeleciona.click();
			WebElement microEscreve = driver.findElement(By.xpath("//*[@id='micro_chosen']/div/div/input"));
			microEscreve.sendKeys(micro);
			microEscreve.sendKeys(Keys.TAB);
			}
			
			WebElement btnPesquisar = driver.findElement(By.id("btnPesquisar"));
			btnPesquisar.click();		

		} catch (Exception e) {
			e.printStackTrace();
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
}
