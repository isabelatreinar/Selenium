package br.com.marph.selenium.beneficiario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class CadastrarBeneficiario {
	/**
	 * Teste de Cadastro de Beneficiário (Caminho Feliz)
	 * Pre-condicao: O beneficiario deve ser cadastrado no CAGEC
	 * Dados de Teste
	 * CNPJ: 17217985003472
	 * Beneficiario: Hospital das Clinicas da Universidade Federal de Minas Gerais
	 * 
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
	public void testeCadastroBeneficiario() throws TesteAutomatizadoException {

		// Recolhendo informações de log
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		
		//Acessar Sistema
		AcessoSistema.perfilAdministrador(driver);

		// Acessar menu
		MenuBeneficiarioTemplate.menuBeneficiario(driver);
		
		// Inicializa list de mensagens
		erros = new ArrayList<>();
		
		//Acessar tela de Cadastro
		driver.findElement(By.id("btnNovoBeneficiario")).click();

		// Realizar cadastro de novo beneficiário (preencher formulario)
		cadastro(driver, "17217985003472", "Entidade");
		
		// Salvar Cadastro
		salvarCadastro();
		
		// Verifica as validações
		verificaValidacao(driver, "17.217.985/0034-72", "Entidade");
		
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

	public static void cadastro(WebDriver driver, String cnpj, String tipo) {
		
		// Preenche campo "CNPJ"
		driver.findElement(By.id("modalNovoCnpj")).clear();
		driver.findElement(By.id("modalNovoCnpj")).sendKeys(cnpj);

		// Seleciona campo "Tipo" da entidade 
		if(!tipo.isEmpty()){
			driver.findElement(By.id("modalNovoTipo_chosen")).click();
			driver.findElement(By.xpath("//*[@id='modalNovoTipo_chosen']/div/div/input")).sendKeys(tipo);
			driver.findElement(By.xpath("//*[@id='modalNovoTipo_chosen']/div/div/input")).sendKeys(Keys.TAB);
		}
	}
	
	public void verificaValidacao(WebDriver driver, String cnpj, String tipo){
		// Valida exibicao do toast apos salvar registro
		if(!driver.findElement(By.id("toast-container")).isDisplayed()){
			erros.add(EnumMensagens.TOAST_DESABILITADO.getMensagem());
		}
		
		// Valida dado exibido no campo "Numero"
		if (!driver.findElement(By.id("modalVisualizarCnpj")).getText().equalsIgnoreCase(cnpj)) {
			erros.add(EnumMensagens.ERRO_SALVAR.getMensagem() + "'CNPJ'");
		}
		
		// Valida dado exibido no campo "Tipo"
		if (!driver.findElement(By.id("modalVisualizarTipo")).getText().equalsIgnoreCase(tipo)) {
			erros.add(EnumMensagens.ERRO_SALVAR.getMensagem() + "'Tipo'");
		}
	}
	
	public void salvarCadastro(){
		// Clica no botão "Salvar"
		driver.findElement(By.id("btnAvancar1")).click();
	}
	
}
