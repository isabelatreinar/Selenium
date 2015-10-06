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

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.usuario.MenuUsuarioTemplate;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class UsuarioExcell {
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
			WorkbookSettings workbookSettings = new WorkbookSettings();
		    workbookSettings.setEncoding("ISO-8859-1");
			Workbook wb= Workbook.getWorkbook(new File("./data/usuario.xls"), workbookSettings);
			Sheet sheet = wb.getSheet(0);
			String nome = sheet.getCell(0,1).getContents();
			String email = sheet.getCell(1,1).getContents();
			String cpf = sheet.getCell(2,1).getContents();
			String cargo = sheet.getCell(3,1).getContents();
			String masp = sheet.getCell(4,1).getContents();
			String telefone = sheet.getCell(5,1).getContents();
			String celular = sheet.getCell(6,1).getContents();
			
			String perfil = sheet.getCell(0,3).getContents();
			String extensao = sheet.getCell(1,3).getContents();
			
			if(!"".equals(nome)){
			WebElement nomeCampo = driver.findElement(By.id("usuarioNome"));
			nomeCampo.sendKeys(nome);
			}
			
			if(!"".equals(email)){
			WebElement emailCampo = driver.findElement(By.id("usuarioEmail"));
			emailCampo.sendKeys(email);
			}
			
			if(!"".equals(cpf)){
			WebElement cpfCampo = driver.findElement(By.id("usuarioCpf"));
			cpfCampo.sendKeys(cpf);
			}			
			
			if(!"".equals(cargo)){
			WebElement cargoCampo = driver.findElement(By.id("cargo_chosen"));
			cargoCampo.click();			
			WebElement selecionarCargo = driver.findElement(By.xpath("//li[@data-option-array-index='"+cargo+"']"));
			selecionarCargo.click();
			}
			
			if(!"".equals(masp)){
				WebElement maspCampo = driver.findElement(By.id("usuarioMasp"));
				maspCampo.sendKeys(masp);
			}
			
			if(!"".equals(telefone)){
				WebElement telefoneCampo = driver.findElement(By.id("usuarioTelefone"));
				telefoneCampo.sendKeys(telefone);
			}
			
			if(!"".equals(celular)){
				WebElement celularCampo = driver.findElement(By.id("usuarioCelular"));
				celularCampo.sendKeys(celular);
			}
			
			WebElement salvar = driver.findElement(By.id("btnSalvar1"));
			salvar.click();
			
			if(!"".equals(perfil)){
			WebElement perfilC = driver.findElement(By.id("modalPerfil_chosen"));
			perfilC.click();			
			WebElement selecionarPerfil = driver.findElement(By.xpath("//li[@data-option-array-index='"+perfil+"']"));
			selecionarPerfil.click();
			}
			
			if(!"".equals(extensao)){
			WebElement extensaoPerfil = driver.findElement(By.id("modalExtensaoPerfilId"));
			extensaoPerfil.sendKeys(extensao);			
			WebElement extensaoSeleciona = driver.findElement(By.id("ui-id-1"));
			extensaoSeleciona.click();
			extensaoPerfil.sendKeys(Keys.TAB);
			}
			WebElement salvar1 = driver.findElement(By.id("btnSalvar1"));
			salvar1.click();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} 
}

