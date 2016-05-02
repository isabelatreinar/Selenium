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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class CadastrarBaseLegal {
	/** 
	 * Teste do Cadastro de Base Legal (Caminho Feliz)
	 * 
	 * Pre-condicao: O caso de teste cadastro de tipo de base legal já foi executado
	 * Dados de Teste
	 * Tipo de Base: Deliberação
	 * Numero: 567
	 * Data da Publicacao: 28/04/2015
	 * Ano de Inicio da Vigencia: 2016	
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
	public void testeCadastro() throws Exception {
		// Recolhendo informações do log
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessar o sistema
		AcessoSistema.perfilAdministrador(driver);
		
		// Acessar menu
		MenuBaseLegalTemplate.menuBaseLegal(driver);
		
		// Inicializa list de mensagens
		erros = new ArrayList<>();

		// Realizar o cadastro
		cadastro(driver, erros, "Deliberação", "567", "28-04-2015", "2016");
		
		// Salva registro
		salvarCadastro(driver);
		
		// Realizar verificacoes
		verificaValidacao(driver, erros, "Deliberação", "567", "28/04/2015", "2016");
		
		// Verifica se existem erros
		if(erros.size() != 0){
			throw new TesteAutomatizadoException(erros, getClass());
		}
		
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

	public static void cadastro(WebDriver driver, List<String> erros, String tipo, String numero, String data, String anoVigencia) {
		// Botão Nova Base Legal
		driver.findElement(By.id("btnNovaBaseLegal")).click();

		// Preenchimento do formulário
		// Tipo de Base Legal
		driver.findElement(By.id("tipoBaseLegal_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(tipo);
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		// Valida se o campo "Ano do inicio da vigencia" esta habilitando apos o preenchimento do campo "Data da publicacao"
		if(driver.findElement(By.id("dataVigencia_chosen")).getAttribute("class").equals(
				"chosen-container chosen-container-single chosen-container-single-nosearch chosen-disabled")){
			erros.add(EnumMensagens.ANO_VIGENCIA_DESABILITADO.getMensagem());
		}

		// Número
		driver.findElement(By.id("numero")).sendKeys(numero);
		
		//Data da Publicação 
		String script = "$('.input-group.date').datepicker('update', '" + data +"');";
		((JavascriptExecutor)driver).executeScript(script);
		
		// Valida se o campo "Ano do inicio da vigencia" esta habilitando apos o preenchimento do campo "Data da publicacao"
		if(driver.findElement(By.id("dataVigencia_chosen")).getAttribute("class").equals(
				"chosen-container chosen-container-single chosen-container-single-nosearch chosen-disabled")){
			erros.add(EnumMensagens.ANO_VIGENCIA_DESABILITADO.getMensagem());
		}
		
		// Ano vigência
		/*PROBLEMA: O "Ano de inicio da vigencia" não está habilitando, mesmo com a data selecionada */
		driver.findElement(By.id("dataVigencia_chosen")).click();
		driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys(anoVigencia);
		driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		//Selecionar arquivo (superior)
		/*File arquivo = new File("./data/TESTEEE.pdf");
		driver.findElement(By.id("liAnexarArquivo")).click();
		//driver.findElement(By.id("textoPublicado")).sendKeys(arquivo.getAbsolutePath());*/

		// Não tem como anexar arquivo, pois não há como interagir com a janela do windows
	}
	
	public static void verificaValidacao(WebDriver driver, List<String> erros, String tipo, String numero, String data, String anoVigencia){
		// Valida exibicao do toast apos salvar registro
		if(!driver.findElement(By.id("toast-container")).isDisplayed()){
			erros.add(EnumMensagens.TOAST_DESABILITADO.getMensagem());
		}
		
		// Valida dado exibido no campo "Tipo"
		if (!driver.findElement(By.id("tipo")).getText().equalsIgnoreCase(tipo)) {
			erros.add(EnumMensagens.ERRO_SALVAR.getMensagem() + "'Tipo'");
		}
		
		// Valida dado exibido no campo "Numero"
		if (!driver.findElement(By.id("numero")).getText().equalsIgnoreCase(numero)) {
			erros.add(EnumMensagens.ERRO_SALVAR.getMensagem() + "'Número'");
		}
		
		// Valida dado exibido no campo "Data da publicacao"
		if (!driver.findElement(By.id("dataPublicacao")).getText().equalsIgnoreCase(data)) {
			erros.add(EnumMensagens.ERRO_SALVAR.getMensagem() + "'Data'");
		} 
		
		// Valida dado exibido no campo "Ano do inicio da vigencia"
		if (driver.findElement(By.id("anoVigencia")).getText().equalsIgnoreCase(anoVigencia)) {
			erros.add(EnumMensagens.ERRO_SALVAR.getMensagem() + "'Ano de vigência'");
		}
	}
	
	public static void salvarCadastro(WebDriver driver){
		// Salvar
		driver.findElement(By.id("btnSalvar1")).click();
	}
}
