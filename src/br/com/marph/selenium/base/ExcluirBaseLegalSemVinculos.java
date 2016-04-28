package br.com.marph.selenium.base;

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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class ExcluirBaseLegalSemVinculos {
	/** 
	 * Teste de Exclusão da Base Legal sem vinculos
	 * Pré-Condição do Teste: Ter executado o script de teste de cadastro de base legal
	 * Dados de Teste
	 * Tipo de Base: Deliberação
	 * Número: 567
	 */
	
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	private List<String> erros = new ArrayList<>();

	@Before
	public void startDriver(){
		driver = new FirefoxDriver();
		Conexao.ip(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@After
	public void driverClose(){
		driver.quit();
	}
		
	@Test
	public void testeExclusao() throws Exception {
		// Recolhe informações do log
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessa o sistema
		AcessoSistema.perfilAdministrador(driver);
		
		// Acessa o menu
		MenuBaseLegalTemplate.menuBaseLegal(driver);
		
		// Exclusão de Base Legal sem vínculos
		exclusaoSemVinculos();
		
		// Fechar o browser
		driver.quit();
		
		// Recolhendo informações do teste
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
	
	public void exclusaoSemVinculos() throws TesteAutomatizadoException{
		/**
		 * Método para teste de exclusão de base legal sem vínculos
		 */
		// Pesquisa Base Legal (Mesma Cadastrada no caso de teste de cadastro)
		EditarBaseLegal.pesquisarBaseLegal(driver, "Deliberação", "567");

		// Click no botão "Excluir"
		driver.findElement(By.id("btnExcluir1")).click();
		
		// Modal de Confirmação de Exclusão
		// Verifica se o sistema exibiu o modal de confirmação (método getWindowHandle())
		if(driver.getWindowHandle().isEmpty()){
			erros.add(EnumMensagens.CONFIRMACAO_DESABILITADA.getMensagem());
		}
		WebElement divExterna = driver.findElement(By.className("jconfirm-box"));
		
		// Verifica mensagem de confirmação
		if(!divExterna.findElement(By.className("content")).getText().equals("Tem certeza que deseja excluir a Base Legal?")){
			erros.add(EnumMensagens.MENSAGEM_INCORRETA.getMensagem());
		}
		
		// Confirma exclusão
		WebElement divBotoes = divExterna.findElement(By.className("buttons"));
		divBotoes.findElement(By.xpath("button[1]")).click();
		
		// Verifica a Exibição do Toast
		if(!driver.findElement(By.id("toast-container")).isDisplayed()){
			erros.add(EnumMensagens.TOAST_DESABILITADO.getMensagem());
		}
		
		//Thread.currentThread().getStackTrace()[1].getMethodName() -> Retorna o nome do Método em Execução
		if(erros.size() != 0)
			throw new TesteAutomatizadoException(erros, getClass());
	}
	
}
