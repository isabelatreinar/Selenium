package br.com.marph.selenium.usuario;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
  
public class PesquisarUsuario{

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
	
/*	@After
	public void tearDown(){
		driver.quit();
		}*/
	
	@Test
	public void PesquisarUsuario(){
		log.info("Inicio do teste - Pesquisar usuario");
		
		long timestart = System.currentTimeMillis();
		
		MenuUsuarioTemplate.prepararAcessoBaseLegal(driver);
		
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
		
/*		sb.append("Entrada no sistema - ");
		sb.append(tempoGasto);
		sb.append("segundos");*/
		
	}

private void pesquisar() {
	WebElement nome = driver.findElement(By.id("nome"));
	nome.sendKeys("Isabela");
	
	WebElement cpf = driver.findElement(By.id("filtroUsuarioCpf"));
	cpf.sendKeys("-08936836633");

	WebElement botaoExpandir = driver.findElement(By.id("btnExpandirPesquisaAvancada"));
	botaoExpandir.click();
	
	/*Select papel = new Select(driver.findElement(By.id("perfil")));
	papel.selectByValue("COMISSAO_AVALIACAO");*/
	
	
	WebElement botaoPesquisar = driver.findElement(By.id("btnPesquisar"));
	botaoPesquisar.click();
}
}
