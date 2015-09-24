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

public class InativarUsuario {
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
		
		log.info("Inicio do teste - Inativar Usuario");
		
		long timestart = System.currentTimeMillis();		
		
		MenuUsuarioTemplate.prepararAcessoBaseLegal(driver);
		
		inativar();

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

	private void inativar() {
		String idUsuario = "rowId2163";
		
		WebElement selecionarUsuario = driver.findElement(By.id(idUsuario));
		selecionarUsuario.click();
		
		WebElement Usuario = driver.findElement(By.xpath("//*[@id='"+idUsuario+"']/td[2]"));
		Usuario.click();
		
		WebElement btnPerfil = driver.findElement(By.id("btnPerfil1"));
		btnPerfil.click();
		
		WebElement btnInativar = driver.findElement(By.id("btnInativar1"));
		btnInativar.click();		
		
		WebElement btnSim = driver.findElement(By.xpath("//button[@class='btn btn-primary btn-sm']"));
		btnSim.click();
	}
}
