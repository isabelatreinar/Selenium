package br.com.maph.selenium.tipoBaseLegal;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;

public class ExportarTipoBaseLegal {
	private final String LOG_NAME = "Karini";
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	
	
	@Before
	public void startBrowser(){
		driver = new FirefoxDriver();
		Conexao.ip(driver);  
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
	}
	
	@Test
	public void realizaBusca(){			
		
		log.info("Inicio do teste - exportar tipo base legal");
		
		long timestart = System.currentTimeMillis();		
		
		MenuTipoBaseLegalTemplate.prepararAcessoTipoBaseLegal(driver);			
		
		exportar();	
		

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
	
	public void exportar(){
		WebElement btnExportar = driver.findElement(By.xpath("//button[@class='btn-Geicom btn-geicom-exportar-horizontal']"));
		btnExportar.click();
	}
}
