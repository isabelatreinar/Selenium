package br.com.marph.selenium.indicador;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagensLog;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;
import br.marph.selenium.validacaoUtils.Validacoes;

public class CadastrarIndicador {
	/** 
	 * Teste de Cadastro de Indicador (Caminho Feliz)
	 * Dados de Teste
	 * Informações do indicador
	 * Tipo de Indicador: Finalístico
	 * Nome: Indicador teste CRUD Indicador
	 * Tipo de Fonte: Declaratório
	 * Nome da Fonte: Beneficiário
	 * Polaridade: Quanto maior, melhor
	 * Programa: Programa Teste
	 * Meses da média Móvel: 12
	 * Meses de Defasagem: 13
	 * Descrição: Teste automatizado do cadastro de indicador
	 * 
	 * Informações da variável
	 * Nome: Variável Teste
	 * Descrição: Variável utilizada no teste automatizado.
	 * 
	 * Fórmula do Indicador
	 * Fórmula: VARIAVEL_TESTE_25
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
	public void testeCadastroIndicador() throws TesteAutomatizadoException {

		// Recolhendo informações de log
		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acesso ao sistema
		AcessoSistema.perfilAdministrador(driver);
		
		// Acesso ao menu
		MenuIndicadorTemplate.menuIndicador(driver);
		
		// Inicializa List de erros
		erros = new ArrayList<>();

		// Acessar o formulário de cadastro de indicador
		driver.findElement(By.id("btnNovoIndicador")).click();
		
		// Preencher as informações da primeira etapa do cadastro
		cadastroIndicador(driver, "Finalístico", "Indicador teste CRUD Indicador", "Declaratório", "Beneficiário", "Quanto maior, melhor", "Programa Teste", "12", "13", "Teste automatizado do cadastro de indicador");
		
		// Avançar para o cadastro de variável
		avancarCadastro();
		
		// Abrir collapse para cadastro da variável
		criarVariavel(driver);

		// Cadastrar variável
		cadastroVariavel(driver, "Variável Teste", "Variável utilizada no teste automatizado.");

		// Avançar para o cadastro da fórmula do indicador
		avancarCadastro();

		// Cadastra fórmula do indicador
		formulaIndicador();

		// Verifica se não existem erros
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

	public static void cadastroIndicador(WebDriver driver, String tipoIndicador, String nomeIndicador, String tipoFonte, String nomeFonte, String polaridade, String programa, 
			String mesesMediaMovel, String mesesDefasagem, String descricao) {

		// Preenche o campo "Tipo de indicador"
		driver.findElement(By.id("tipoIndicador_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoIndicador_chosen']/div/div/input")).sendKeys(tipoIndicador);
		driver.findElement(By.xpath("//*[@id='tipoIndicador_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		// Preenche o campo "Nome do indicador"
		driver.findElement(By.id("nomeIndicador")).sendKeys(nomeIndicador);

		// Preenche o campo "Tipo de fonte"
		driver.findElement(By.id("tipoFonte_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoFonte_chosen']/div/div/input")).sendKeys(tipoFonte);
		driver.findElement(By.xpath("//*[@id='tipoFonte_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		// Preenche o campo "Nome da fonte"
		driver.findElement(By.id("nomeFonte")).sendKeys(nomeFonte);

		// Preenche o campo "Polaridade"
		driver.findElement(By.id("polaridade_chosen")).click();
		driver.findElement(By.xpath("//*[@id='polaridade_chosen']/div/div/input")).sendKeys(polaridade);
		driver.findElement(By.xpath("//*[@id='polaridade_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		// Preenche o campo "Programa"
		driver.findElement(By.id("programa_chosen")).click();
		driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input")).sendKeys(programa);
		driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input")).sendKeys(Keys.TAB);

		// Preenche o campo "Meses da média Móvel"
		driver.findElement(By.id("mesesDaMediaMovel")).sendKeys(mesesMediaMovel);

		// Preenche o campo "Meses de defasagem"
		driver.findElement(By.id("mesesDeDefasagem")).sendKeys(mesesDefasagem);

		// Preenche o campo "Descrição"
		driver.findElement(By.id("descricao")).sendKeys(descricao);

	}
	
	private void avancarCadastro(){
		// avançar
		driver.findElement(By.id("btnSalvar1")).click();
	}
	
	public static void criarVariavel(WebDriver driver){
		// Clica em "Criar Variável"
		driver.findElement(By.id("criar")).click();
	}

	public void cadastroVariavel(WebDriver driver, String nome, String descricao) {
		// "Preenche o campo "Nome"
		driver.findElement(By.xpath("//*[@class='titAccordion']/input")).sendKeys(nome);

		// Preenche o campo "Descrição"
		driver.findElement(By.xpath("//*[@class='_descricao']/textarea")).sendKeys(descricao);

		// Armazena a variável
		driver.findElement(By.className("_salvarVariavel")).click();
		
		// Verifica a exibição do toast
		if(Validacoes.verificaExibicaoToast(driver) == false){
			erros.add(EnumMensagensLog.TOAST_DESABILITADO.getMensagem() + " [Cadastro de Variável]");
		}
		// Valida a mensagem exibida para o usuário
		else if(Validacoes.verificaMensagemToast(driver, "Variável salva com sucesso.") == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Toast - Cadastro de Variável");
		}
	}

	public void formulaIndicador()  {
		/**
		 * Adiciona sempre uma única variável à fórmula do indicador
		 */
		// Clica na variável para adicioná-la a fórmula
		driver.findElement(By.xpath("//*[@id='heading0']/div[3]/ul/li/a")).click();

		// Salva a fórmula
		driver.findElement(By.id("btnSalvar1")).click();

		// Valida a exibição do toast
		if(Validacoes.verificaExibicaoToast(driver) == false){
			erros.add(EnumMensagensLog.TOAST_DESABILITADO.getMensagem() + "[Cadastro de Fórmula]");
		}
		// Valida a mensagem exibida para o usuário
		else if(Validacoes.verificaMensagemToast(driver, "Indicador salvo com sucesso") == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Toast CRUD Indicador (Fórmula)");
		}
	}
}
