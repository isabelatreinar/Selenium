package br.com.marph.selenium.usuario;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
  
public class CadastroUsuarioChrome {
	private final String LOG_NAME = System.getProperty("user.name");
	private Logger log = LogManager.getLogger(LOG_NAME);
	
	
	@Test
	public void startBrowser(){		
		System.setProperty("webdriver.chrome.driver", "C://chromedriver.exe");
        WebDriver driver = new ChromeDriver();   
        driver.get("http://www.google.com");        
		driver.manage().window().maximize();
		System.err.println("passei pelo menu usuarios");
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);		
		
		log.info("Inicio do teste");
		
		long timestart = System.currentTimeMillis();			
		
		System.err.println("passei pelo menu usuarios");
		WebElement fecharbtn = driver.findElement(By.id("lst-ib"));
		fecharbtn.click();
		
		WebElement nome = driver.findElement(By.id("lst-ib"));
		nome.sendKeys("Marph");			
		
				
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
