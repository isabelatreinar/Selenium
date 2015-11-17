package br.com.marph.selenium.blocoDeFinanciamento;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

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

		MenuBlocoTemplate.prepararAcessoBloco(driver);
		
		cadastrar();		
		
		if (driver.findElement(By.xpath("//ol[@class='breadcrumb small']")).getText()
				.equalsIgnoreCase("Você está em: Bloco de financiamento > Novo Bloco de Financiamento")) {
			validar();
		}
		
		float tempoGasto = (System.currentTimeMillis() - timestart);
		float tempoSegundos = tempoGasto / 1000;

		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos");

		if (tempoSegundos > 5000) {
			log.warn(sb.toString() + "\n");
		} else {
			log.info(sb.toString() + "\n");
		}
	}

	private void cadastrar() {
		//acessa pagina para cadastrar
		driver.findElement(By.id("btnNovoBlocoFinanciamento")).click();
		
		//insere o nome
		driver.findElement(By.id("nome")).sendKeys("marphg");
		
		//insere descrição
		driver.findElement(By.id("descricao")).sendKeys("marphhhhhhhhhhh");
		//js.executeScript("$('#descricao').val()");
		//clica em salvar
		driver.findElement(By.id("btnSalvar")).click();
	}	
	
	private void validar() throws TesteAutomatizadoException {
		driver.findElement(By.id("nome")).click();
		try {
			if(driver.findElement(By.xpath("//*[@id='nome_maindiv']/div")).getText().equalsIgnoreCase("Bloco de financiamento já cadastrado.")){
				throw new TesteAutomatizadoException(EnumMensagens.BLOCO_JA_CADASTRADO, this.getClass());
			}else if(driver.findElement(By.xpath("//*[@id='nome_maindiv']/div")).getText().equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.NOME_EM_BRANCO, this.getClass());
			}
		} catch (NoSuchElementException e) {
			
		}
		if(StringUtils.isBlank(driver.findElement(By.id("descricao")).getAttribute("value"))){
			throw new TesteAutomatizadoException(EnumMensagens.DESCRICAO_EM_BRANCO, this.getClass());
		}
	}
}