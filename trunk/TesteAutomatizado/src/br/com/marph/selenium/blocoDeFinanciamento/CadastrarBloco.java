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
import br.marph.selenium.validacaoUtils.Validacoes;

public class CadastrarBloco {
	/**
	 * Teste de Cadastro de Bloco de Financiamento (Caminho Feliz)
	 * Dados de Teste:
	 * Nome: Bloco teste
	 * Descrição: Este bloco de financiamento foi cadastrado no teste automatizado.
	 * Status: Ativo (Status Default)
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
	public void testeCadastroBloco() throws InterruptedException, TesteAutomatizadoException {

		// Recolhe informacoes de log
		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		
		// Acessa o sistema
		AcessoSistema.perfilAdministrador(driver);

		// Acessa o menu
		MenuBlocoTemplate.menuBlocoFinanciamento(driver);
		
		// Inicializa list
		erros = new ArrayList<>();
		
		// Acessa tela de cadastro
		driver.findElement(By.id("btnNovoBlocoFinanciamento")).click();
		
		// Insere as informações no formulário
		cadastroBlocoFinanciamento(driver, "Bloco teste", "Este bloco de financiamento foi cadastrado no teste automatizado.");
		
		// Armazena cadastro
		salvarCadastro(driver);
			
		// Realiza as validações do cadastro
		verificaValidacoes(driver, erros, "Bloco teste", "Este bloco de financiamento foi cadastrado no teste automatizado.");
		
		// verifica se existem erros
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

	public static void cadastroBlocoFinanciamento(WebDriver driver, String nome, String descricao) {
		// Preenche o campo "Nome"
		driver.findElement(By.id("nome")).sendKeys(nome);
		
		//Preenche o campo "Descrição"
		driver.findElement(By.id("descricao")).sendKeys(descricao);
		
	}	
	
	public static void salvarCadastro(WebDriver driver){
		// Armazena registro
		driver.findElement(By.id("btnSalvar1")).click();
	}
	
	public static void verificaValidacoes(WebDriver driver, List<String> erros, String nome, String descricao){
		// Verifica se o toast foi exibido
		if(Validacoes.verificaExibicaoToast(driver) == false){
			erros.add(EnumMensagensLog.TOAST_DESABILITADO.getMensagem());
		}
		
		// Valida a mensagem exibida no toast
		if(Validacoes.verificaMensagemToast(driver, "Bloco de Financiamento salvo com sucesso") == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + " Toast");
		}
		
		// Valida as informacoes salvas no campo "Nome"
		if(!driver.findElement(By.id("tipo")).getText().equals(nome)){
			erros.add(EnumMensagensLog.ERRO_SALVAR.getMensagem() + " Nome");
		}
		
		// Valida as informacoes salvas no campo "Descricao"
		if(!driver.findElement(By.id("numero")).getText().equals(descricao)){
			erros.add(EnumMensagensLog.ERRO_SALVAR.getMensagem() + " Descricao");
		}
	}
}
