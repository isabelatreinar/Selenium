package br.com.marph.selenium.blocoDeFinanciamento;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class CadastrarBloco {
	/**
	 * Teste de Cadastro de Bloco de Financiamento (Caminho Feliz)
	 * Dados de Teste:
	 * Nome: Bloco teste
	 * Descrição: Este bloco de financiamento foi cadastrado no teste automatizado.
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
	
	@Test
	public void testeCadastroBloco() throws InterruptedException, TesteAutomatizadoException {

		// Recolhe informacoes de log
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
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
		
		// Armazena as informacoes
		salvarCadastro();
		
		// Realiza as validações do cadastro
		verificaValidacoes(driver, "Bloco teste", "Este bloco de financiamento foi cadastrado no teste automatizado.");
		
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
	
	private void salvarCadastro(){
		// Armazena registro
		driver.findElement(By.id("btnSalvar1")).click();
	}
	
	private void verificaValidacoes(WebDriver driver, String nome, String descricao){
		
	}
	
}
