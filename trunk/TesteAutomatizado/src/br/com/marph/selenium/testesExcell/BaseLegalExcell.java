package br.com.marph.selenium.testesExcell;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.base.MenuBaseLegalTemplate;
import br.com.marph.selenium.conexao.Conexao;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class BaseLegalExcell {
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
		MenuBaseLegalTemplate.prepararAcessoBaseLegal(driver);
		WebElement botaoCadastrar = driver.findElement(By.id("btnNovoUsuario"));
		botaoCadastrar.click();
		
		try {
			WorkbookSettings workbookSettings = new WorkbookSettings();
		    workbookSettings.setEncoding("ISO-8859-1");
			Workbook wb= Workbook.getWorkbook(new File("./data/baseLegal.xls"), workbookSettings);
			Sheet sheet = wb.getSheet(0);
			String tipo = sheet.getCell(0,1).getContents();
			String numero = sheet.getCell(1,1).getContents();
			String data = sheet.getCell(2,1).getContents();
			String ano = sheet.getCell(3,1).getContents();
			String pdf = sheet.getCell(4,1).getContents();
			
			WebElement tipoBase = driver.findElement(By.id("tipoBaseLegal_chosen"));
			tipoBase.click();
			
			WebElement procuraTipoBase = driver.findElement(By.xpath("//li[@data-option-array-index='"+tipo+"']"));
			procuraTipoBase.click();
			
			WebElement numero1 = driver.findElement(By.id("numero"));
			numero1.sendKeys(numero);
			
			WebElement data1 = driver.findElement(By.id("dataPublicacao"));
			data1.sendKeys(data);			
		
			WebElement anoVigencia = driver.findElement(By.id("dataVigencia_chosen"));
			anoVigencia.click();
			
			WebElement anoVigenciaSeleciona = driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/ul/li["+ano+"]"));
			anoVigenciaSeleciona.click();
			
			driver.findElement(By.id("textoPublicado")).sendKeys(pdf);
			
			WebElement salvar = driver.findElement(By.id("btnSalvar"));
			salvar.click();	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} 
}
