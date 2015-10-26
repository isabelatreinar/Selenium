package br.com.marph.selenium.municipio;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.utils.LogUtils;


public class PesquisarMunicipio {
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
		
		MenuMunicipioTemplate.prepararAcessoMunicipio(driver);
			
		pesquisar(driver);
		
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

	public static void pesquisar(WebDriver driver) {
		WebElement nome = driver.findElement(By.id("nome"));
		nome.sendKeys("BELO VALE");
		
		//Selecionar unidade regional
		WebElement unidadeRegional = driver.findElement(By.id("unidadeRegional_chosen"));
		unidadeRegional.click();
		WebElement selecionaRegional = driver.findElement(By.xpath("//*[@id='unidadeRegional_chosen']/div/div/input"));
		selecionaRegional.sendKeys("Belo Horizonte");
		selecionaRegional.sendKeys(Keys.ENTER);
		
		// exibir pesquisa avançada
		driver.findElement(By.id("btnExpandirPesquisaAvancada")).click();
		
		/* Selecionar Macro e micro
		 * Esta parte da pesquisa está comentada, pois os filtros Unidade Regional, macro e micro são aninhados
		 */
		/*driver.findElement(By.id("macros_chosen")).click();
		WebElement macro = driver.findElement(By.xpath("//*[@id='macros_chosen']/div/div/input"));
		macro.sendKeys("Centro");
		macro.sendKeys(Keys.ENTER);
		
		driver.findElement(By.id("micro_chosen")).click();
		WebElement micro =  driver.findElement(By.xpath("//*[@id='micro_chosen']/div/div/input"));
		micro.sendKeys("Belo Horizonte");
		micro.sendKeys(Keys.ENTER);*/
		
		
		WebElement btnPesquisar = driver.findElement(By.id("btnPesquisar"));
		btnPesquisar.click();
	}
}

