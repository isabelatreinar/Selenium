package br.com.marph.selenium.blocoDeFinanciamento;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class CadastrarBlocoComValidacao {
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	//JavascriptExecutor js;
	
	@Before
	public void startBrowser() {
		driver = new FirefoxDriver();
		Conexao.ip(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//js = (JavascriptExecutor) driver;
	}
	
	@Test
	public void realizaBusca() throws InterruptedException, TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		
		AcessoSistema.perfilAdministrador(driver);

		MenuBlocoTemplate.menuBlocoFinanciamento(driver);
		
		//acessa pagina de cadastro
		driver.findElement(By.id("btnNovoBlocoFinanciamento")).click();
		
		//Testes básicos
		validar();
		
		//cadastro
		cadastrar();		
		
		float tempoGasto = (System.currentTimeMillis() - timestart);
		float tempoSegundos = tempoGasto / 1000;

		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos - FINALIZADO COM SUCESSO\n");

		if (tempoSegundos > 5000) {
			log.warn(sb.toString() + "\n");
		} else {
			log.info(sb.toString() + "\n");
		}
		
		driver.quit();
	}

	private void cadastrar() throws TesteAutomatizadoException {		
		//insere o nome
		driver.findElement(By.id("nome")).sendKeys("marphg");
		
		//insere descrição
		driver.findElement(By.id("descricao")).sendKeys("marphhhhhhhhhhh");
		//js.executeScript("$('#descricao').val()");
		//clica em salvar
		driver.findElement(By.id("btnSalvar1")).click();
		
		if(driver.findElement(By.className("toast-message")).getText().equalsIgnoreCase("Existem erros no formulário.")){
			driver.findElement(By.id("nome")).click();
			if(!driver.findElement(By.xpath("//*[@id='nome_maindiv']/div")).getText().equalsIgnoreCase("Bloco de financiamento já cadastrado.")){
				throw new TesteAutomatizadoException(EnumMensagens.BLOCO_JA_CADASTRADO, this.getClass());
			}
		}
		
	}	
	
	private void validar() throws TesteAutomatizadoException {
		// validação do breadcrumb
		if(!driver.findElement(By.className("breadcrumb")).getText().equalsIgnoreCase("Você está em: Bloco de financiamento > Novo Bloco de Financiamento")){
			throw new TesteAutomatizadoException(EnumMensagens.BREADCRUMB_INCORRETO, this.getClass());
		}
		
		//clica em salvar
		driver.findElement(By.id("btnSalvar1")).click();
		driver.findElement(By.id("nome")).click();
		try {
			//Valida se existe validação de preenchimento obrigatório no campo
			if(!driver.findElement(By.xpath("//*[@id='nome_maindiv']/div")).getText().equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.NOME_VALIDACAO, this.getClass());
			}
			
		} catch (NoSuchElementException e) {
			
		}
		/*VERIFICAR COMO PASSAR UMA LISTA DE MENSAGENS
		 * if(StringUtils.isBlank(driver.findElement(By.id("descricao")).getAttribute("value"))){
			throw new TesteAutomatizadoException(EnumMensagens.DESCRICAO_EM_BRANCO, this.getClass());
		}*/
	}
}