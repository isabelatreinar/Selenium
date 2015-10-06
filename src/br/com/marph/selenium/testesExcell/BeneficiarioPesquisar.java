package br.com.marph.selenium.testesExcell;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.beneficiario.MenuBeneficiarioTemplate;
import br.com.marph.selenium.conexao.Conexao;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class BeneficiarioPesquisar {
	private WebDriver driver;	

	@Before
	public void startBrowser(){
		driver = new FirefoxDriver();
		Conexao.ip(driver); 
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);		
	}
	
	@Test	
	public void teste(){
			MenuBeneficiarioTemplate.prepararAcessoBaseLegal(driver);
				
		try {
			WorkbookSettings workbookSettings = new WorkbookSettings();
		    workbookSettings.setEncoding("ISO-8859-1");
			Workbook wb= Workbook.getWorkbook(new File("./data/beneficiarioPesquisa.xls"), workbookSettings);
			Sheet sheet = wb.getSheet(0);
			String nome = sheet.getCell(0,1).getContents();
			String unidade = sheet.getCell(1,1).getContents();

			if(!"".equals(nome)){
				WebElement nome1 = driver.findElement(By.id("buscaNome"));
				nome1.sendKeys(nome);
			}
			
			if(!"".equals(unidade)){
				WebElement unidadePesquisa = driver.findElement(By.id("unidadeRegional_chosen"));
				unidadePesquisa.click();
				WebElement unidadeDigita = driver.findElement(By.xpath("//*[@id='unidadeRegional_chosen']/div/div/input"));
				unidadeDigita.sendKeys(unidade);
				unidadeDigita.sendKeys(Keys.TAB);
			}else{
				System.out.println("VAZIOO");
			}
			
			WebElement pesquisar = driver.findElement(By.id("btnPesquisar"));
			pesquisar.click();
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} 
}
