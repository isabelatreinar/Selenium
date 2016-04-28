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
import org.openqa.selenium.firefox.FirefoxDriver;
import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.enums.EnumValidacao;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;


public class CadastrarBaseDuplicada {
	/** 
	 * Teste da validacao de cadastro de base legal duplicada
	 * Pre-condicao: Existir uma base legal cadastrada com o mesmo numero e do mesmo tipo
	 * 
	 * Dados de Teste
	 * Tipo de Base: Deliberação
	 * Número: 1235
	 * 
	 * */
	
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
	public void testeCadastroValidacoes() throws Exception {
		
	
		// Recolhe informacoes do log
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
	
		// Acessa o sistema
		AcessoSistema.perfilAdministrador(driver);
		
		// Acessa o menu
		MenuBaseLegalTemplate.menuBaseLegal(driver);
		
		
		// Inicializa ArrayList com as mensagens
		erros = new ArrayList<>();
		
		// Realiza Cadastro
		CadastrarBaseLegal.cadastro(driver, erros, "Deliberação", "1235", "12", "2016");
		
		// Verifica as validacoes dos campos obrigatorios da tela
		verificaBaseDuplicada();
		
		// Recolhendo informacoes do teste
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
	
	public void verificaBaseDuplicada() throws TesteAutomatizadoException{
		/** 
		 * Este metodo verifica a validacao de cadastro de base legal duplicado
		 * Esta validacao e realizada sobre o campo "Numero", quando o foco sair do mesmo
		 * 
		 * Verifica se o campo esta marcado em vermelho. Se nao estiver marcado em vermelho, significa que o campo esta
		 * sem validacao.
		 * Quando o campo fica em branco o sistema possui o seguinte HTML class = "chosen-container chosen-container-single chosen-container-active"
		 * Quando esta em vermelho o sistema remove o HTML "chosen-container-active"
		 * Logo, o teste da validacao e realizado verificando se o valor do atributo class do campo e igual a classe acima.
		 */
		
		if(driver.findElement(By.id("numero")).getAttribute("class").equals(EnumValidacao.MARCACAO_ERRO.getHtml()))
			erros.add(EnumMensagens.REGISTRO_DUPLICADO.getMensagem() + " Base Legal");
		
		// Verifica se existem mensagens de erro
		if(erros.size() != 0){
			throw new TesteAutomatizadoException(erros, getClass());
		}
	}
	
}
