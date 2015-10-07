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

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.utils.LogUtils;

public class PesquisarTipoBaseLegal {
	private final String LOG_NAME = "RAFAEL" ;
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
	public void pesquisaTipoBaseLegal(){
		
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		
		long timestart = System.currentTimeMillis();
		
		MenuTipoBaseLegalTemplate.prepararAcessoTipoBaseLegal(driver);
		
		pesquisar();
		
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
	
	public void pesquisar(){
		WebElement nome = driver.findElement(By.id("nome"));
		nome.sendKeys("Nota Técnica");
		
		WebElement pesqAvancada = driver.findElement(By.id("btnExpandirPesquisaAvancada"));
		pesqAvancada.click();
		
		WebElement transfRecursos = driver.findElement(By.id("transferenciaRecursosFinanceiros_chosen"));
		transfRecursos.click();
		
		WebElement transSeleciona = driver.findElement(By.xpath("//li[@data-option-array-index='1']"));
		transSeleciona.click();
		
		WebElement prestacaoMetas = driver.findElement(By.id("prestacaoMetas_chosen"));
		prestacaoMetas.click();
		
		WebElement prestacaoMetasSeleciona = driver.findElement(By.xpath("//*[@id='prestacaoMetas_chosen']/div/ul/li[1]"));
		prestacaoMetasSeleciona.click();
		
		WebElement prestacaoContas = driver.findElement(By.id("prestacaoContas_chosen"));
		prestacaoContas.click();
		
		WebElement prestacaoContasSeleciona = driver.findElement(By.xpath("//*[@id='prestacaoContas_chosen']/div/ul/li[2]"));
		prestacaoContasSeleciona.click();
		
		WebElement btnPesquisar = driver.findElement(By.id("btnPesquisar"));
		btnPesquisar.click();
		
	}
}
