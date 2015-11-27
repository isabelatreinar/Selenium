package br.com.marph.selenium.base;

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

public class LimparPesquisaBaseLegal {
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);

	@Before
	public void startBrowser() {
		driver = new FirefoxDriver();
		Conexao.ip(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void limparPesquisaBaseLegal() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		
		// Acessar menu
		MenuBaseLegalTemplate.prepararAcessoBaseLegal(driver);
		
		// Preencher os filtros de pesquisa
		PesquisarBaseLegal.pesquisar(driver);
		
		//Limpar os filtros
		limpar();
		
		validar();
		
		// se o campo estiver vazio o teste é finalizado com sucesso
		float tempoGasto = (System.currentTimeMillis() - timestart);
		float tempoSegundos = tempoGasto / 1000;

		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos - FINALIZADO COM SUCESSO\n");
		

		if (tempoSegundos > 5000) {
			log.warn(sb.toString() + "\n");
		} else {
			log.info(sb.toString() + "\n");
		}
		
	}

	private void validar() throws TesteAutomatizadoException {
		// validar exclusão de dados dos campos
		if(StringUtils.isNotBlank(driver.findElement(By.id("tipoBaseLegal")).getText()) || 
				StringUtils.isNotBlank(driver.findElement(By.id("numero")).getText()) ||
				StringUtils.isNotBlank(driver.findElement(By.id("dataPublicacao")).getText()) || 
				StringUtils.isNotBlank(driver.findElement(By.id("dataVigencia")).getText())){
			throw new TesteAutomatizadoException(EnumMensagens.CAMPO_PREENCHIDO, this.getClass());
		}
	}
	
	public void limpar(){	
		driver.findElement(By.id("btnLimparPesquisa")).click();
	}

}
