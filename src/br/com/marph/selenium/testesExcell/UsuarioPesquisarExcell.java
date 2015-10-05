package br.com.marph.selenium.testesExcell;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.usuario.MenuUsuarioTemplate;
import jxl.Sheet;
import jxl.Workbook;

public class UsuarioPesquisarExcell {
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
		MenuUsuarioTemplate.prepararAcessoBaseLegal(driver);
		
		
		try {
			Workbook wb= Workbook.getWorkbook(new File("./data/usuarioPesquisa.xls"));
			Sheet sheet = wb.getSheet(0);
			String nome = sheet.getCell(0,1).getContents();
			String cpf = sheet.getCell(1,1).getContents();
			String nomeEditado = sheet.getCell(2,1).getContents();
			String email = sheet.getCell(3,1).getContents();
			
			WebElement nome1 = driver.findElement(By.id("nome"));
			nome1.sendKeys(nome);
			
			WebElement cpf1 = driver.findElement(By.id("filtroUsuarioCpf"));
			cpf1.sendKeys(cpf);			
			WebElement botaoPesquisar = driver.findElement(By.id("btnPesquisar"));
			botaoPesquisar.click();
			
			WebElement usuario = driver.findElement(By.xpath("//td[@class='sorting_1']"));
			usuario.click();
			
			WebElement botaoEditar = driver.findElement(By.id("btnEditar1"));
			botaoEditar.click();
			
			WebElement nomeEditar = driver.findElement(By.id("usuarioNome"));
			nomeEditar.clear();
			nomeEditar.sendKeys(nomeEditado);
			
			WebElement email1 = driver.findElement(By.id("usuarioEmail"));
			email1.clear();
			email1.sendKeys(email);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} 
}
