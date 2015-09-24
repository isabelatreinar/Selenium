package br.com.marph.selenium.base;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CadastrarBaseLegalMozilla {
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
		
		log.info("Inicio do teste - cadastrar base legal");
		
		long timestart = System.currentTimeMillis();		
		
		MenuBaseLegalTemplate.prepararAcessoBaseLegal(driver);
		
		cadastro();
		
		validacao();
		
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

	private void cadastro() {
		//CADASTRO
		WebElement btnNovoUsu = driver.findElement(By.id("btnNovoUsuario"));
		btnNovoUsu.click();	
	
		WebElement tipoBase = driver.findElement(By.id("tipoBaseLegal_chosen"));
		tipoBase.click();		
		WebElement procuraTipoBase = driver.findElement(By.xpath("//li[@data-option-array-index='1']"));
		procuraTipoBase.click();
		
		WebElement numero = driver.findElement(By.id("numero"));
		numero.sendKeys("6524456");
		
		WebElement data = driver.findElement(By.id("dataPublicacao"));
		data.click();
		WebElement dataSeleciona = driver.findElement(By.xpath("//td[@class='day']"));
		dataSeleciona.click();
		
		WebElement anoVigencia = driver.findElement(By.id("dataVigencia_chosen"));
		anoVigencia.click();
		
		WebElement anoVigenciaSeleciona = driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/ul/li[1]"));
		anoVigenciaSeleciona.click();
		
		driver.findElement(By.id("textoPublicado")).sendKeys("C:\\Users\\rafael.sad\\Downloads\\TESTEEE.pdf");
		
		WebElement salvar = driver.findElement(By.id("btnSalvar"));
		salvar.click();		
		//FIM CADASTRO 
	}
	
	private void validacao() {
		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='tipoBaseLegal_label']/label/span")).getText())){			
			log.info("Campo de Tipo estava em branco - Obrigatório");
		}
	}
}
