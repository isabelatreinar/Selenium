package br.com.marph.selenium.beneficiario;

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

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.utils.LogUtils;

public class PesquisarBeneficiarioMozilla {
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);

	@Before
	public void startBrowser() {
		driver = new FirefoxDriver();
		Conexao.ip(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Test
	public void pesquisarBeneficiario(){
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();		
		
		// Acessar menu
		MenuBeneficiarioTemplate.prepararAcessoBeneficiario(driver);
		
		//Pesquisar Beneficiario
		pesquisar(driver);
		
		
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
	
	public static void pesquisar(WebDriver driver) {		
		WebElement nome = driver.findElement(By.id("buscaNome"));
		nome.sendKeys("FUNDO MUNICIPAL DE SAÚDE DE BETIM");
		
		//Selecionar unidade regional
//		driver.findElement(By.id("unidadeRegional_chosen")).click();
		WebElement unidadeRegional = driver.findElement(By.xpath("//*[@id='unidadeRegional_chosen']/div/div/input"));
		unidadeRegional.sendKeys("Belo Horizonte");
		unidadeRegional.sendKeys(Keys.ENTER);
		
		// Abrir pesquisa avançada
		WebElement pesquisaAvancada = driver.findElement(By.id("btnPesquisaAvancada"));
		pesquisaAvancada.click();
		
		//Preenche CNPJ - O sinal '-' é necessário para pegar a máscara do campo
		WebElement cnpj = driver.findElement(By.id("buscaCnpj"));
		cnpj.sendKeys("-13064113000100");
		cnpj.sendKeys(Keys.ENTER);
		
		// Seleciona tipo de beneficiário
		driver.findElement(By.id("buscaTipoBeneficiario_chosen")).click();
		WebElement tipo = driver.findElement(By.xpath("//*[@id='buscaTipoBeneficiario_chosen']/div/div/input"));
		tipo.sendKeys("Município");
		tipo.sendKeys(Keys.ENTER);
		
		// Preenche o município
		WebElement municipio = driver.findElement(By.id("buscaMunicipio_chosen"));
		municipio.sendKeys("Betim");
		
		WebElement btnPesquisar = driver.findElement(By.id("btnPesquisar"));
		btnPesquisar.click();
	}
}
