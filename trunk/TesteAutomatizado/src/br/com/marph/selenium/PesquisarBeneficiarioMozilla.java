package br.com.marph.selenium;

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

public class PesquisarBeneficiarioMozilla {
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
		
		log.info("Inicio do teste");
		
		long timestart = System.currentTimeMillis();		
		
		//ENTRADA
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
		
		WebElement menuUsuario = driver.findElement(By.xpath("//*[@id='beneficiarioMenu']"));
		menuUsuario.click();
		
		//PESQUISAR
	
		WebElement nome = driver.findElement(By.id("buscaNome"));
		nome.sendKeys("CONFERÊNCIA DE SÃO VICENTE DE PAULO DE TURMALINA");
		
		//Selecionar unidade regional
		WebElement unidadeRegional = driver.findElement(By.id("unidadeRegional_chosen"));
		unidadeRegional.click();
		WebElement procuraTipoRegional = driver.findElement(By.xpath("//li[@data-option-array-index='2']"));
		procuraTipoRegional.click(); 
		//fim
		
		WebElement btnPesquisar = driver.findElement(By.id("btnPesquisar"));
		btnPesquisar.click();
		
		//FIM PESQUISAR
		
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
