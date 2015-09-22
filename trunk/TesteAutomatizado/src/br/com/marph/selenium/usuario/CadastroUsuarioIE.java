package br.com.marph.selenium.usuario;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;


public class CadastroUsuarioIE {
	private final String LOG_NAME = "RAFAEL";
	private Logger log = LogManager.getLogger(LOG_NAME);
	
	
	@Test
	public void startBrowser(){		
		System.setProperty("webdriver.ie.driver", "C://IEDriverServer.exe");
		WebDriver driver=new InternetExplorerDriver();  
        driver.get("http://172.16.10.115:8081");        
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);		
		
		log.info("Inicio do teste");
		
		long timestart = System.currentTimeMillis();			
		
		System.err.println("passei pelo menu usuarios");
		WebElement fecharbtn = driver.findElement(By.id("closeModalHome"));
		fecharbtn.click();
		
		WebElement btnEntrar = driver.findElement(By.id("btnEntradaSistemaID"));
		btnEntrar.click();		
		
				
		float tempoGasto = (System.currentTimeMillis() - timestart);
		
		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoGasto).append(" segundos");
		

		if(tempoGasto>5000){
			log.warn(sb.toString());
		}else{
			log.info(sb.toString());
		}		
	}
}
