package br.com.marph.selenium.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class CadastrarBaseLegalValidacoes {
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	private List<String> erros;

	
	@Before
	public void startBrowser(){
		driver = new FirefoxDriver();
		Conexao.ip(driver);  
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		}
		
	@Test
	public void testeCadastroValidacoes() throws Exception {
		
		/** 
		 * Teste da validação dos campos obrigatórios no cadastro de uma nova Base Legal
		 */

		// Recolhe informações do log
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessa o sistema
		AcessoSistema.perfilAdministrador(driver);
		
		// Acessa o menu
		MenuBaseLegalTemplate.menuBaseLegal(driver);
		
		// Acessa a tela de cadastro
		driver.findElement(By.id("btnNovaBaseLegal")).click();
		
		// Clica no botão "Salvar"
		driver.findElement(By.id("btnSalvar1")).click();
		
		// Inicializa ArrayList com as mensagens
		erros = new ArrayList<>();
		
		// Verifica as validações dos campos obrigatórios da tela
		verificaValidacoes();
		
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
	
	public void verificaValidacoes() throws TesteAutomatizadoException, InterruptedException{
		/**
		 * Verifica se o campo está marcado em vermelho. Se não estiver marcado em vermelho, significa que o campo está
		 * sem validação.
		 * Quando o campo fica em branco o sistema possui o seguinte HTML class = "chosen-container chosen-container-single chosen-container-active"
		 * Quando está em vermelho o sistema remove o HTML "chosen-container-active"
		 * Logo, o teste da validação é realizado verificando se o valor do atributo class do campo é igual a classe acima.
		 */
		
		// Valida toast de erro
		if(!driver.findElement(By.id("toast-container")).isDisplayed())
			erros.add(EnumMensagens.TOAST_DESABILITADO.getMensagem());
		
		// Valida breadCrumb
		if (!driver.findElement(By.xpath("//ol[@class='breadcrumb']")).getText()
				.equalsIgnoreCase("Você está em: Base Legal > Nova Base Legal")) {
			 erros.add(EnumMensagens.BREADCRUMB_INCORRETO.getMensagem());
		}
	
		//------------------- Validação dos campos habilitados--------------------
		
		// Valida obrigatoriedade do campo "Tipo"
		if(driver.findElement(By.id("tipoBaseLegal_chosen")).getAttribute("class").equals("chosen-container chosen-container-single chosen-container-active"))
			erros.add(EnumMensagens.TIPO_VALIDACAO.getMensagem());
						
		// Valida obrigatoriedade da data da publicação
		if(driver.findElement(By.id("dataPublicacao")).getAttribute("class").equals("chosen-container chosen-container-single chosen-container-active"))
			erros.add(EnumMensagens.DATA_PUBLICACAO_VALIDACAO.getMensagem());
		
		//-------------------Habilitar o restante dos campos para validação--------------------
		
		//Preenche o campo "Tipo" para que o campo "Número" fique habilitado
		driver.findElement(By.id("tipoBaseLegal_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys("Deliberação");
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		// Preenche o campo "Data da publicação" para que o campo "Ano do início da vigência" seja validado
		driver.findElement(By.id("dataPublicacao")).click();
		
		//Passando o dataPicker para uma tabela
		WebElement datePicker = driver.findElement(By.xpath("/html/body/div[5]/div[1]"));
	//	List<WebElement> rows = datePicker.findElements(By.tagName("tr"));
		List<WebElement> columns = datePicker.findElements(By.tagName("td"));
		for(WebElement cell : columns){ 
			if(cell.getText().equals("23")){
				cell.click();
				break;
			}
		}
		
		// Clicar em "Salvar" para verificar validação
		driver.findElement(By.id("btnSalvar1")).click();
		
		//---------------------Validação dos campos que estavam desabilitados-----------------------------
		
		// Valida obrigatoriedade do número da base legal
		if(driver.findElement(By.id("numero")).getAttribute("class").equals("chosen-container chosen-container-single chosen-container-active"))
			erros.add(EnumMensagens.NUMERO_VALIDACAO.getMensagem());
		
		// Valida obrigatoriedade da data da vigência
		if (driver.findElement(By.id("dataVigencia_chosen")).getAttribute("class").equals("chosen-container chosen-container-single chosen-container-active"))
			erros.add(EnumMensagens.DATA_VIGENCIA_VALIDACAO.getMensagem());
		
		// Verifica se existem mensagens de erro
		if(erros.size() != 0){
			throw new TesteAutomatizadoException(erros, Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}
}
