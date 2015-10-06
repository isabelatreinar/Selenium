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

public class BaseLegalPesquisarExcell {

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
				
		try {
			Workbook wb= Workbook.getWorkbook(new File("./data/baseLegalPesquisa.xls"));
			Sheet sheet = wb.getSheet(0);
			String tipo = sheet.getCell(0,2).getContents();
			String numero = sheet.getCell(1,2).getContents();
			String data = sheet.getCell(2,2).getContents();
			String ano = sheet.getCell(3,2).getContents();
			String tipoEditar = sheet.getCell(5,2).getContents();
			String numeroEditar = sheet.getCell(6,2).getContents();
			String dataEditar = sheet.getCell(7,2).getContents();
			String anoEditar = sheet.getCell(8,2).getContents();
			String pdf = sheet.getCell(9,2).getContents();
			
			WebElement tipo1 = driver.findElement(By.id("tipoBaseLegal_chosen"));
			tipo1.click();
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
			
			WebElement pesquisar = driver.findElement(By.id("btnPesquisar"));
			pesquisar.click();

			WebElement usuario = driver.findElement(By.xpath("//td[@class='sorting_1']"));
			usuario.click();
			
			WebElement botaoEditar = driver.findElement(By.id("btnEditar1"));
			botaoEditar.click();
			
			WebElement tipo2 = driver.findElement(By.id("tipoBaseLegal_chosen"));
			tipo2.click();
			WebElement procuraTipoBase1 = driver.findElement(By.xpath("//li[@data-option-array-index='"+tipoEditar+"']"));
			procuraTipoBase1.click();
			
			WebElement numero2 = driver.findElement(By.id("numero"));
			numero2.clear();
			numero2.sendKeys(numeroEditar);
			
			WebElement data2 = driver.findElement(By.id("dataPublicacao"));
			data2.clear();
			data2.sendKeys(dataEditar);
			
			WebElement anoVigencia1 = driver.findElement(By.id("dataVigencia_chosen"));
			anoVigencia1.click();
			
			
			WebElement anoVigenciaSeleciona1 = driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/ul/li["+anoEditar+"]"));
			anoVigenciaSeleciona1.click();
			
			driver.findElement(By.id("textoPublicado")).sendKeys(pdf);
			
			WebElement salvar = driver.findElement(By.id("btnSalvar"));
			salvar.click();	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} 
}
