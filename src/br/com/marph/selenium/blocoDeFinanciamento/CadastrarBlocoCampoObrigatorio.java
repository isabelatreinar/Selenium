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
import br.com.marph.selenium.enums.EnumMensagensSistema;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.javaScriptUtils.JavaScript;
import br.com.marph.selenium.utils.LogUtils;
import br.marph.selenium.validacaoUtils.Validacoes;

public class CadastrarBlocoCampoObrigatorio {
	/**
	 * Teste de cadastro de Bloco de Financiamento validação de campo obrigatório
	 * 
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
	public void testeCadastroDuplicado() throws InterruptedException, TesteAutomatizadoException {

		// Recolhendo infomacoes de log
		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		
		// Acesso ao sistema
		AcessoSistema.perfilAdministrador(driver);

		// Acesso ao menu
		MenuBlocoTemplate.menuBlocoFinanciamento(driver);
		
		// Inicializa list
		erros = new ArrayList<>();
		
		//Acesso a tela de cadastro
		driver.findElement(By.id("btnNovoBlocoFinanciamento")).click();
		
		// Clica em "Salvar" para marcar os campos obrigatórios
		driver.findElement(By.id("btnSalvar1")).click();
		
		// Verifica as validações nos campos
		verificaValidacoes();
		
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
	
	private void verificaValidacoes(){
		boolean marcacao = true;
		// Verifica BreadCrumb
		if(Validacoes.verificaBreadCrumb(driver, "Bloco de financiamento > Novo Bloco de Financiamento") == false){
			erros.add(EnumMensagensLog.BREADCRUMB_INCORRETO.getMensagem());
		}
		
		// Verifica exibicao do toast
		if(Validacoes.verificaExibicaoToast(driver) == false){
			erros.add(EnumMensagensLog.TOAST_DESABILITADO.getMensagem());
		}
		
		//Verifica mensagem do toast
		if(Validacoes.verificaMensagemToast(driver, EnumMensagensSistema.ERRO.getMensagem()) == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA + " Toast");
		}
		
		// ---------------------------------- Validações no campo "Nome" --------------------------------------
		// Verifica marcação no campo "Nome"
		if(Validacoes.verificaMarcacaoErroId(driver, "nome_maindiv") == false){
			erros.add(EnumMensagensLog.CAMPO_OBRIGATORIO.getMensagem() + "'Nome'");
			marcacao = false;
		}
		
		// Verifica tooltip e tipo de validacao
		if(marcacao == true){
			JavaScript.getTooltipClear(driver, "nome");	// abre tooltip
			if(Validacoes.verificaMensagemTooltip(driver, EnumMensagensSistema.PREENCHIMENTO_OBRIGATORIO.getMensagem()) == false){
				erros.add(EnumMensagensLog.VALIDACAO_INCORRETA.getMensagem() + "Nome");
			}
		}
		
		// ---------------------------------- Validações no campo "Nome" --------------------------------------
		// Verifica marcação no campo "Descricao"
		marcacao = true;
		if(Validacoes.verificaMarcacaoErroxPath(driver, "//*[@id='dadosVisualizar']/div/div[2]/div[1]/div") == false){
			erros.add(EnumMensagensLog.CAMPO_OBRIGATORIO.getMensagem() + "'Descrição'");
			marcacao = false;
		}
		
		// Verifica tooltip e tipo de validacao
		if(marcacao == true){
			JavaScript.getTooltipClear(driver, "descricao");	// abre tooltip
			if(Validacoes.verificaMensagemTooltip(driver, EnumMensagensSistema.PREENCHIMENTO_OBRIGATORIO.getMensagem()) == false){
				erros.add(EnumMensagensLog.VALIDACAO_INCORRETA.getMensagem() + "Descricao");
			}
		}
	}
}
