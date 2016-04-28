package br.com.marph.selenium.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class EditarBaseLegal {
	/** 
	 * Teste de edição de Base Legal (Caminho Feliz)
	 * Neste teste são realizados em conjunto o teste de pesquisa de base legal
	 * 
	 * Dados de Teste
	 * Tipo de Base Legal: Resolução
	 * Numero: 1678
	 * Data da Publicacao: 10/10/2015
	 * Ano do inicio da vigencia: 2015
	 */
	
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	private List<String> erros;
	

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
	public void testeEditarBaseLegal() throws Exception {
		
		// Recolhe informações de log
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		
		erros = new ArrayList<>();

		// Acesso ao sistema com perfil "Administrador"
		AcessoSistema.perfilAdministrador(driver);
		
		// Acesso ao menu
		MenuBaseLegalTemplate.menuBaseLegal(driver);

		// Pesquisar Base Legal
		pesquisarBaseLegal(driver, "Resolução", "159");

		// Editar formulario
		CadastrarBaseLegal.cadastro(driver, erros, "Resolução", "1678", "10-10-2015", "2015");
		
		// Salvar edição
		CadastrarBaseLegal.salvarCadastro(driver);

		// Verifica os resultados dos testes
		CadastrarBaseLegal.verificaValidacao(driver, erros, "Resolução", "1678", "10/10/2015", "2015");
		
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
	
	public static void pesquisarBaseLegal(WebDriver driver, String tipo, String numero) {
		//Preencher campo "Tipo"
		driver.findElement(By.id("tipoBaseLegal_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(tipo);
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		//Preencher campo "Número"
		driver.findElement(By.id("numero")).sendKeys(numero);

		/*Pesquisar com data
		// Altera a data da publicação via java script 
		String script = "$('.input-group.date').datepicker('update', '" + data +"');";
		((JavascriptExecutor)driver).executeScript(script);

		//Pesquisar com anoVigencia
		driver.findElement(By.id("dataVigencia_chosen")).click();
		driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys("2017");
		driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys(Keys.TAB); */
		
		//Clicar no botão pesquisar
		driver.findElement(By.id("btnPesquisar")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("sorting_1")));
		
		// Acessa a tela do registro pesquisado
		driver.findElement(By.className("sorting_1")).click();
	}
}

