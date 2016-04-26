package br.com.marph.selenium.base;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;

public class CadastrarBaseDuplicada {
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	private List<String> erros;

	
	@Before
	public void startBrowser(){
		driver = new FirefoxDriver();
		Conexao.ip(driver);  
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		}
		
	@Test
	public void verificaBaseDuplicada() throws TesteAutomatizadoException{
		/** 
		 * Este método verifica a validação de cadastro de base legal duplicado
		 * Esta validação é realizada sobre o campo "Número", quando o foco sair do mesmo
		 */
		// A base legal utilizada no teste é do tipo "Deliberação" e número "155" e já consta no banco de dados
		//Preenche o campo "Tipo"
		driver.findElement(By.id("tipoBaseLegal_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys("Deliberação");
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		//Preenche o campo "Número"
		driver.findElement(By.id("numero")).sendKeys("155");
		driver.findElement(By.id("numero")).sendKeys(Keys.TAB);
		
		// Clica novamente no campo para subir o tooltip da validação
		driver.findElement(By.id("numero")).click();
		if(!driver.findElement(By.className("tooltip tooltip-error fade top in")).isDisplayed())
			erros.add(EnumMensagens.BASE_LEGAL_VALIDACAO.getMensagem());
		
		// Verifica se existem mensagens de erro
		if(erros != null){
			throw new TesteAutomatizadoException(erros, Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}
}
