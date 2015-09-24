package br.com.marph.selenium.municipio;

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


public class PesquisarMunicipio {
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
	public void realizaCadastro(){			
		
		log.info("Inicio do teste - pesquisar municipio");
		
		long timestart = System.currentTimeMillis();		
		
		MenuMunicipioTemplate.prepararAcessoBaseLegal(driver);
			
		pesquisar();
		
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

	private void pesquisar() {
		WebElement nome = driver.findElement(By.id("nome"));
		nome.sendKeys("BARBACENA");
		
		//Selecionar unidade regional
		WebElement unidadeRegional = driver.findElement(By.id("unidadeRegional_chosen"));
		unidadeRegional.click();
		WebElement procuraTipoRegional = driver.findElement(By.xpath("//li[@data-option-array-index='2']"));
		procuraTipoRegional.click(); 
		//fim
		
		WebElement btnPesquisar = driver.findElement(By.id("btnPesquisar"));
		btnPesquisar.click();
	}
}

