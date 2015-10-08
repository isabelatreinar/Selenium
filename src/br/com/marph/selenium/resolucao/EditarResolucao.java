package br.com.marph.selenium.resolucao;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.utils.LogUtils;

public class EditarResolucao {
	private final String LOG_NAME = "RAFAEL";
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
		
		LogUtils.log(EnumMensagens.INICIO, this.getClass());	
		
		long timestart = System.currentTimeMillis();		
		
		MenuResolucaoTemplate.prepararAcessoBaseLegal(driver);
		
		String idResolucao = "rowId334";
		
		WebElement selecionarResolucao = driver.findElement(By.id(idResolucao));
		selecionarResolucao.click();
		
		WebElement resolucao = driver.findElement(By.xpath("//*[@id='"+idResolucao+"']/td[2]"));
		resolucao.click();
		
		//if(){
			
		//}
		
		
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
