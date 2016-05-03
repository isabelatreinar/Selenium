package br.com.marph.selenium.blocoDeFinanciamento;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagensLog;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class AtivarInativarBloco {
	/**
	 * Teste Ativar e Inativar Bloco de Financiamento (Caminho Feliz)
	 * Pré-Condicao: O bloco deve estar cadastrado na base de dados
	 * Dados de Teste
	 * Nome: Teste Ativar Inativar bloco de financiamento
	 * Status Inicial: Ativo
	 */
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	private List<String> erros;
	
	@Before
	public void startBrowser() {
		driver = new FirefoxDriver();
		Conexao.ip(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@After
	public void closeBrowser(){
		driver.quit();
	}
	
	@Test
	public void testeAtivarInativarBloco() throws TesteAutomatizadoException {
		// Recolhendo informacoes de log
		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acesso o sistema
		AcessoSistema.perfilAdministrador(driver);
		
		// Acesso menu
		MenuBlocoTemplate.menuBlocoFinanciamento(driver);
		
		// Inicializar List
		erros = new ArrayList<>();
		
		// Pesquisa e seleciona registro a ser editado
		EditarBlocoSemVinculo.pesquisar(driver, "Teste Ativar Inativar bloco de financiamento", "Ativo");
		
		// Inativa o bloco e realiza a validação
		if(verificaStatus(driver, "btnInativar", "inativo") == false){
			erros.add(EnumMensagensLog.ALTERACAO_STATUS.getMensagem() + "Ativo/Inativo");
		}
		
		// Ativa o bloco e realiza a validação
		if(verificaStatus(driver, "btnAtivar", "ativo") == false){
			erros.add(EnumMensagensLog.ALTERACAO_STATUS.getMensagem() + "Inativo/Ativo");
		}
		
		// Verifica se existem erros
		if(erros.size() != 0){
			throw new TesteAutomatizadoException(erros, getClass());
		}
		
		
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
	
	private boolean verificaStatus(WebDriver driver, String idBotao, String status) {
		// Clica no botão "Inativar"
		driver.findElement(By.id(idBotao)).click();
		
		// Verifica a alteração de status
		if(driver.findElement(By.id("pSituacao")).getAttribute("class").equals(status))
			return true;
		return false;
	}	
		
}
