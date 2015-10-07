package br.com.marph.selenium.testesExcell;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.usuario.MenuUsuarioTemplate;
import br.com.marph.selenium.utils.LogUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class UsuarioPesquisarExcell {
	private final String LOG_NAME = "RAFAEL";
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
		MenuUsuarioTemplate.prepararAcessoBaseLegal(driver);		
		
		try {
			WorkbookSettings workbookSettings = new WorkbookSettings();
		    workbookSettings.setEncoding("ISO-8859-1");
			Workbook wb= Workbook.getWorkbook(new File("./data/usuarioPesquisa.xls"), workbookSettings);
			Sheet sheet = wb.getSheet(0);
			String nome = sheet.getCell(0,1).getContents();
			String cpf = sheet.getCell(1,1).getContents();
			String nomeEditado = sheet.getCell(2,1).getContents();
			String email = sheet.getCell(3,1).getContents();
			
			if(StringUtils.isNotBlank(nome)){
			WebElement nome1 = driver.findElement(By.id("nome"));
			nome1.sendKeys(nome);
			}
			if(StringUtils.isNotBlank(cpf)){
			WebElement cpf1 = driver.findElement(By.id("filtroUsuarioCpf"));
			cpf1.sendKeys(cpf);
			}
			
			WebElement botaoPesquisar = driver.findElement(By.id("btnPesquisar"));
			botaoPesquisar.click();
			
			WebElement usuario = driver.findElement(By.xpath("//td[@class='sorting_1']"));
			usuario.click();
			
			WebElement botaoEditar = driver.findElement(By.id("btnEditar1"));
			botaoEditar.click();
			
			if(StringUtils.isNotBlank(nomeEditado)){
			WebElement nomeEditar = driver.findElement(By.id("usuarioNome"));
			nomeEditar.clear();
			nomeEditar.sendKeys(nomeEditado);
			}
			
			if(StringUtils.isNotBlank(email)){
			WebElement email1 = driver.findElement(By.id("usuarioEmail"));
			email1.clear();
			email1.sendKeys(email);
			}
			
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
