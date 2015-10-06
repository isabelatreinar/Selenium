package br.com.marph.selenium.beneficiario;

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

public class VisualizarBeneficiario {
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
	public void visualizarResponsavel(){
		
		log.info("Início do teste Visualizar beneficiário");
		
		long timestart = System.currentTimeMillis();
		
		//Acessa Menu Cadastros Beneficiário
		MenuBeneficiarioTemplate.prepararAcessoBaseLegal(driver);
		
		// Pesquisa um beneficiário na base de dados
		PesquisarBeneficiarioMozilla.pesquisar(driver);
		
		// visualizar beneficiario
		visualizar(driver);
		
		//teste botão voltar superior
		voltarSuperior();
		
		// teste botão voltar inferior
		visualizar(driver);
		voltarInferior();
		
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
	
	public static void visualizar(WebDriver driver){
		WebElement beneficiario = driver.findElement(By.xpath("//td[@class='sorting_1']"));
		beneficiario.click();
	}
	
	public void voltarSuperior(){
		WebElement btnVoltar = driver.findElement(By.id("btnVoltarBarraTarefas"));
		btnVoltar.click();
	}
	
	public void voltarInferior(){
		WebElement btnVoltar = driver.findElement(By.id("btnVoltar"));
		btnVoltar.click();
	}
}
