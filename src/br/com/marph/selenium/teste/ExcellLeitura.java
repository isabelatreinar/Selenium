package br.com.marph.selenium.teste;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
			String perfil = sheet.getCell(4,1).getContents();
			String extensao = sheet.getCell(5,1).getContents();
			
			
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
			
			WebElement salvar = driver.findElement(By.id("btnSalvar1"));
			salvar.click();
			
			WebElement perfilC = driver.findElement(By.id("modalPerfil_chosen"));
			perfilC.click();
			
			WebElement selecionarPerfil = driver.findElement(By.xpath("//li[@data-option-array-index='"+perfil+"']"));
			selecionarPerfil.click();
			
			WebElement extensaoPerfil = driver.findElement(By.id("modalExtensaoPerfilId"));
			extensaoPerfil.sendKeys(extensao);
			
			WebElement extensaoSeleciona = driver.findElement(By.id("ui-id-1"));
			extensaoSeleciona.click();
			extensaoPerfil.sendKeys(Keys.TAB);
			
			WebElement salvar1 = driver.findElement(By.id("btnSalvar1"));
			salvar1.click();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} 
}
