package br.com.marph.selenium.assinatura;

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

public class TermoAditivo {
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
		
		MenuAssinauraTemplate.prepararAcessoBaseLegal(driver);
		
		try {
		    Thread.sleep(10000);           
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		
		WebElement programa = driver.findElement(By.id("programaFiltro_chosen"));
		programa.click();		
		WebElement programaSeleciona = driver.findElement(By.xpath("//li[@data-option-array-index='2']"));
		programaSeleciona.click();
		
		WebElement exibicao = driver.findElement(By.id("statusFiltro_chosen"));
		exibicao.click();		
		WebElement exibicaoSeleciona = driver.findElement(By.xpath("//*[@id='statusFiltro_chosen']/div/ul/li[1]"));
		exibicaoSeleciona.click();
		
		WebElement pesquisar = driver.findElement(By.id("btnPesquisar"));
		pesquisar.click();

		float tempoGasto = (System.currentTimeMillis() - timestart );
		float tempoSegundos = tempoGasto/1000;
		
		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos - FINALIZADO COM SUCESSO\n");
	
		if(tempoSegundos>5000){
			log.warn(sb.toString()+"\n");
		}else{
			log.info(sb.toString()+"\n");
		}			
	}	
}

