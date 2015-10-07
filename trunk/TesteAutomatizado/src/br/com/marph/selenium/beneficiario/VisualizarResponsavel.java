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

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.utils.LogUtils;

public class VisualizarResponsavel {
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
	public void visualizarResponsavel(){
		
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		
		long timestart = System.currentTimeMillis();
		
		//Acessa Menu Cadastros Beneficiário
		MenuBeneficiarioTemplate.prepararAcessoBaseLegal(driver);
		
		// Pesquisa um beneficiário na base de dados
		PesquisarBeneficiarioMozilla.pesquisar(driver);
		
		//Visualizar beneficiario
		VisualizarBeneficiario.visualizar(driver);
		
		//Visualizar responsável legal
		visualizar();
		
		// teste botão voltar superior
		voltarSuperior();		
		
		// teste botão voltar inferior
		visualizar();
		voltarInferior();
		
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

	public void visualizar() {
		// clica no botão de responsável legal
		WebElement btnResponsavel = driver.findElement(By.id("btnPerfil1"));
		btnResponsavel.click();
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
