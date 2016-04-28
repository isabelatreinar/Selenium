package br.com.marph.selenium.beneficiario;

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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class EditarBeneficiarioCF {
	/**
	 * Esta classe valida a funcionalidade de seleção edição de beneficiario (Caminho Feliz)
	 * Pre-Condicao: O CNPJ editado deve estar cadastrado no CAGEC
	 * 
	 * Dados de Teste
	 * Beneficiario a ser editado: Fundo Municipal de Saude de Betim
	 * CNPJ: 13064113000100
	 * CNPJ alterado para: 17878554000199 (Fundacao de Ensino e Tecnologia de Alfenas)
	 **/
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
	public void driverClose(){
		driver.quit();
	}

	@Test
	public void testeEditarBeneficiario() throws TesteAutomatizadoException{

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		
		//Acessar Sistema
		AcessoSistema.perfilAdministrador(driver);

		// Acessar menu
		MenuBeneficiarioTemplate.menuBeneficiario(driver);
		
		erros = new ArrayList<>();

		// Pesquisa e acessa a tela do Beneficiario
		pesquisar(driver, "Betim", "belo horizonte", "13064113000100", " ", " ");

		// Acessar tela de edicao de beneficiario
		driver.findElement(By.id("btnEditar1")).click();
		editar(driver, "17878554000199", "Justificativa de alteracao teste");

		// Verificar Validações
		validacoesEdicaoCF(driver, "17.878.554/0001-99");
		
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

	public static void pesquisar(WebDriver driver, String nome, String unidadeRegional, String cnpj, String tipo, String municipio){
		/**
		 * Pesquisa e acessa a tela de visualizacao do beneficiario
		 */
		// Preenche o filtro "Nome"
		driver.findElement(By.id("buscaNome")).sendKeys(nome);
		
		//Selecionar unidade regional
		driver.findElement(By.id("unidadeRegional_chosen")).click();
		driver.findElement(By.xpath("//*[@id='unidadeRegional_chosen']/div/div/input")).sendKeys(unidadeRegional);
		driver.findElement(By.xpath("//*[@id='unidadeRegional_chosen']/div/div/input")).sendKeys(Keys.TAB);
	

		// Abrir pesquisa avancada
		WebDriverWait waitPesquisaAvancada = new WebDriverWait(driver, 10);
		waitPesquisaAvancada.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnPesquisaAvancada")));
		driver.findElement(By.id("btnPesquisaAvancada")).click();

		//Preenche filtro "CNPJ"
		driver.findElement(By.id("buscaCnpj")).clear();
		driver.findElement(By.id("buscaCnpj")).sendKeys(cnpj);
	
		
		// Seleciona filtro "Tipo"
		if(tipo.isEmpty()){
			driver.findElement(By.id("buscaTipoBeneficiario_chosen")).click();
			driver.findElement(By.xpath("//*[@id='buscaTipoBeneficiario_chosen']/div/div/input")).sendKeys(tipo);
			driver.findElement(By.xpath("//*[@id='buscaTipoBeneficiario_chosen']/div/div/input")).sendKeys(Keys.TAB);
		}
		
		
		// Preenche filtro "Municipio"
		/*driver.findElement(By.xpath("//*[@id='buscaMunicipio_chosen']/ul/li/input")).click();
		driver.findElement(By.xpath("//*[@id='buscaMunicipio_chosen']/ul/li/input")).sendKeys(municipio);	
		driver.findElement(By.xpath("//*[@id='buscaMunicipio_chosen']/div/ul/li")).click();*/
		
		WebElement btnPesquisar = driver.findElement(By.id("btnPesquisar"));
		btnPesquisar.click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("sorting_1")));
		
		// Seleciona Beneficiario pesquisado
		driver.findElement(By.className("sorting_1")).click();
	}

	private void editar(WebDriver driver, String cnpj, String justificativa) {
		
		// Preenchimento do formulario - Campo "CNPJ"
		driver.findElement(By.id("cnpj")).sendKeys(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE);		
		driver.findElement(By.id("cnpj")).sendKeys("17878554000199");
		
		
		// Preencher campo "Justificativa"
		driver.findElement(By.id("justificativa")).sendKeys(justificativa);
		
		// Salvar alteracoes
		driver.findElement(By.id("btnSalvar1")).click();
	}
	
	private void validacoesEdicaoCF(WebDriver driver, String cnpj) {
		
		// Valida exibicao do toast apos salvar registro
		if(!driver.findElement(By.id("toast-container")).isDisplayed()){
			erros.add(EnumMensagens.TOAST_DESABILITADO.getMensagem());
		}
		
		// Valida dado exibido no campo "Numero"
		if (!driver.findElement(By.id("modalVisualizarCnpj")).getText().equalsIgnoreCase(cnpj)) {
			erros.add(EnumMensagens.ERRO_SALVAR.getMensagem() + "'CNPJ'");
		}
	}
		
}
