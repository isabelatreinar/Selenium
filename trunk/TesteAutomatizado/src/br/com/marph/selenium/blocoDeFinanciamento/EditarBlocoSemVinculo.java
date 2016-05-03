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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagensLog;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;
import br.com.marph.selenium.utils.WaitUtils;

public class EditarBlocoSemVinculo {
	/**
	 * Teste de edição de bloco de financiamento sem vínculo com programas
	 * Pré-Codicao: Executar o script de teste de cadastro de bloco de financiamento
	 * 
	 * Dados de Teste
	 * Nome: Bloco teste 
	 * Status: Ativo
	 * 
	 */
	
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	private List <String> erros;

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
	public void testeEdicaoSemVinculo() throws TesteAutomatizadoException {
		
		// Recolhendo as informações de log
		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		
		// Acesso ao sistema
		AcessoSistema.perfilAdministrador(driver);
		
		// Acesso ao menu
		MenuBlocoTemplate.menuBlocoFinanciamento(driver);
		
		// Inicializa list
		erros = new ArrayList<>();

		// Pesquisa e seleciona registro a ser editado
		pesquisar(driver, "Bloco teste", "Ativo");
		
		// Acessa a tela de edição do registro
		driver.findElement(By.id("btnEditar1")).click();
		
		// Limpa os dados do formulário
		limparDados();

		// Edição de registro
		CadastrarBloco.cadastroBlocoFinanciamento(driver, "Bloco de financiamento teste", "Teste automatizado de edição de bloco sem vínculos");
		
		// Armazenar alterações
		CadastrarBloco.salvarCadastro(driver);

		// Verificar validações
		CadastrarBloco.verificaValidacoes(driver, erros, "Bloco de financiamento teste", "Teste automatizado de edição de bloco sem vínculos");
		
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
	
	public static void pesquisar(WebDriver driver, String nome, String status) {
		//insere o nome a ser pesquisado
		driver.findElement(By.id("nome")).sendKeys(nome);
		
		//situação
		driver.findElement(By.id("situacao_chosen")).click();
		driver.findElement(By.xpath("//*[@id='situacao_chosen']/div/div/input")).sendKeys(status);
		driver.findElement(By.xpath("//*[@id='situacao_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		//clica em pesquisar
		driver.findElement(By.id("btnPesquisar")).click();
		
		// Aguarda exibição do elemento
		//WaitUtils.waitCondicionalClass(driver, 10, "sorting_1");
		
		// Seleciona registro
		//driver.findElement(By.className("sorting_1")).click();
		
		((JavascriptExecutor)driver).executeScript("$('.sorting_1').click();");
	}
	
	private void limparDados(){
		// Apagar informacoes do campo "Nome"
		driver.findElement(By.id("nome")).sendKeys(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE);
		
		// Apagar informacoes do campo "Nome"
		driver.findElement(By.id("descricao")).sendKeys(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE);
				
	}

}
