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

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.utils.LogUtils;

public class CadastroResolucao {
	private final String LOG_NAME = System.getProperty("user.name");
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
		
		MenuResolucaoTemplate.prepararAcessoResolucao(driver);
		
		cadastrarResolucao();
		

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

	private void cadastrarResolucao() {
		WebElement cadastrarResolucao = driver.findElement(By.id("btnNovaResolucao"));
		cadastrarResolucao.click();
		//Selecionar programa
		WebElement programaSelecionar = driver.findElement(By.id("programa_chosen"));
		programaSelecionar.click();
		
		WebElement programa = driver.findElement(By.xpath("//li[@data-option-array-index='3']"));
		programa.click();		
		//fim
		//numero resolucao
		WebElement numero = driver.findElement(By.id("baseLegal"));
		numero.sendKeys("456");
		
		WebElement numeroSeleciona = driver.findElement(By.xpath("//li[@id='ui-id-6']"));
		numeroSeleciona.click();
		//fim
		//base legal
		WebElement selecionarBase = driver.findElement(By.id("termosBaseLegal_chosen"));
		selecionarBase.click();
		
		WebElement base = driver.findElement(By.xpath("//*[@id='termosBaseLegal_chosen']/div/ul/li[3]"));
		base.click();
		//fim
		WebElement tempo = driver.findElement(By.id("tempoVigencia"));
		tempo.sendKeys("25");
		
		WebElement descricao = driver.findElement(By.id("descricao"));
		descricao.sendKeys("Teste TESTE");
		
		WebElement salvar = driver.findElement(By.id("btnSalvar1"));
		salvar.click();
	}	
}
