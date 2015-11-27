package br.com.marph.selenium.municipio;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class LimparPesquisaHistoricoMunicipio {
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
	public void limparPesquisaHistoricoMunicipio() throws TesteAutomatizadoException{			
		
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();		
		
		// Acessar menu Cadastro > Município
		MenuMunicipioTemplate.prepararAcessoMunicipio(driver);
		
		// Pesquisar um município
		PesquisarMunicipio.pesquisar(driver);
		
		// Acessar o município pesquisado
		VisualizarMunicipio.visualizar(driver);
		
		// Acessar a tela de histórico
		VisualizarHistoricoMunicipio.visualizar(driver);
		
		// Pesquisar
		PesquisarHistoricoMunicipio.pesquisar(driver);
		
		// limpar
		limpar(driver);
		
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
	
	private void limpar(WebDriver driver) throws TesteAutomatizadoException{
		driver.findElement(By.id("btnLimparPesquisa")).click();
		
		// validar exclusão de dados dos campos 
		if (StringUtils.isNotBlank(driver.findElement(By.id("dataInicialHistorico")).getText())
				|| StringUtils.isNotBlank(driver.findElement(By.id("dataFinalHistorico")).getText())
				|| StringUtils.isNotBlank(driver.findElement(By.id("camposMunicipio_chosen")).getText())
				|| !driver.findElement(By.id("usuariosAlteracao_chosen")).getText().equalsIgnoreCase("Modificado Por:")) {
			throw new TesteAutomatizadoException(EnumMensagens.CAMPO_PREENCHIDO, LimparPesquisaHistoricoMunicipio.class);
		}
	}
}
