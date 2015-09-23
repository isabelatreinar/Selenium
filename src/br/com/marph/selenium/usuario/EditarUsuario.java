package br.com.marph.selenium.usuario;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EditarUsuario {
	private final String LOG_NAME = "RAFAEL";
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	
	
	@Before
	public void startBrowser(){
		driver = new FirefoxDriver();
		driver.get("http://172.16.10.115:8081");  
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		}

	
	@Test
	public void realizaBusca(){	
		
		
		log.info("Inicio do teste - editar usuarios");
		
		long timestart = System.currentTimeMillis();		
		
		//MENU
		WebElement fecharbtn = driver.findElement(By.id("closeModalHome"));
		fecharbtn.click();
		
		WebElement btnEntrar = driver.findElement(By.id("btnEntradaSistemaID"));
		btnEntrar.click();
		
		WebElement btnAcessar = driver.findElement(By.id("btnAcessar"));
		btnAcessar.click();
		
		WebElement btnConfirmar = driver.findElement(By.id("confirmarDados"));
		btnConfirmar.click();
		
		WebElement btnAcessarSist = driver.findElement(By.id("acessarSistema"));
		btnAcessarSist.click();			
		
		WebElement menuCadastrar = driver.findElement(By.xpath("//td[@onmouseup='cmItemMouseUp (this,2)']"));
		menuCadastrar.click(); 
		
		WebElement menuUsuario = driver.findElement(By.xpath("//*[@id='usuariosMenu']"));
		menuUsuario.click();		
		//FIM MENU
		
		//EDIÇÂO
		String idUsuario = "rowId2159";
		
		WebElement selecionarUsuario = driver.findElement(By.id(idUsuario));
		selecionarUsuario.click();
		
		WebElement usuario = driver.findElement(By.xpath("//*[@id='"+idUsuario+"']/td[2]"));
		usuario.click();
		
		WebElement btnEditar = driver.findElement(By.id("btnEditar1"));
		btnEditar.click();
		
		WebElement nome = driver.findElement(By.id("usuarioNome"));
		nome.clear();
		//nome.sendKeys("TESTE");
		
		WebElement cargo = driver.findElement(By.id("cargo_chosen"));
		cargo.click();
		
		WebElement selecionarCargo = driver.findElement(By.xpath("//li[@data-option-array-index='4']"));
		selecionarCargo.click();
		
		WebElement salvar = driver.findElement(By.id("btnSalvar"));
		salvar.click();
		
		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='usuarioNome_label']/label/span")).getText())){			
			log.info("Campo de nome estava em branco - Obrigatório");
		}
		
		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='cargo_label']/label/span")).getText())){			
			log.info("Campo de cargo estava em branco - Obrigatório");
		}
		
		//FIM
		float tempoGasto = (System.currentTimeMillis() - timestart );
		float tempoSegundos = tempoGasto/1000;
		
		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos");
	
		if(tempoSegundos>5000){
			log.warn(sb.toString()+"\n");
		}else{
			log.info(sb.toString()+"\n");
		}		
		
	}
}
