package br.com.marph.selenium.teste;

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

public class ExcellLeitura {
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
		WebElement botaoCadastrar = driver.findElement(By.id("btnNovoUsuario"));
		botaoCadastrar.click();
		
		try {
			Workbook wb= Workbook.getWorkbook(new File("./data/teste.xls"));
			Sheet sheet = wb.getSheet(0);
			String nome = sheet.getCell(0,1).getContents();
		String email = sheet.getCell(1,1).getContents();
			String cpf = sheet.getCell(2,1).getContents();
			String cargo = sheet.getCell(3,1).getContents();
			
			WebElement nomeCampo = driver.findElement(By.id("usuarioNome"));
			nomeCampo.sendKeys(nome);
			
			WebElement emailCampo = driver.findElement(By.id("usuarioEmail"));
			emailCampo.sendKeys(email);
			
			WebElement cpfCampo = driver.findElement(By.id("usuarioCpf"));
			cpfCampo.sendKeys(cpf);
			
			WebElement cargoCampo = driver.findElement(By.id("cargo_chosen"));
			cargoCampo.click();
			
			WebElement selecionarCargo = driver.findElement(By.xpath("//li[@data-option-array-index='"+cargo+"']"));
			selecionarCargo.click();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/*public void rafaelDoidao(){
		
		try {				
			Workbook wb= Workbook.getWorkbook(new File("./data/teste.xls"));			
			Sheet sheet = wb.getSheet(0);			
			int linhas = sheet.getRows();
			
			for(int i = 0;i < linhas;i++){
				
				Cell celula1 = sheet.getCell(0, i);

	            Cell celula2 = sheet.getCell(1, i);            

	            System.out.println("celula 1: "+ celula1.getContents());

	            System.out.println("celula 2: "+ celula2.getContents());				
			}			
					
		
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}*/
}
