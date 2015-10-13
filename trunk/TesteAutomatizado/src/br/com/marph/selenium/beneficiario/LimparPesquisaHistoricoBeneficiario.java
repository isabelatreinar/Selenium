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
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class LimparPesquisaHistoricoBeneficiario {
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
	public void limparPesquisaHistoricoBeneficiario() throws TesteAutomatizadoException {
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();		
		
		MenuBeneficiarioTemplate.prepararAcessoBeneficiario(driver);		
		
		WebElement nome = driver.findElement(By.id("buscaNome"));
		nome.sendKeys("FUNDO MUNICIPAL DE SAÚDE DE CAMPO BELO");		
		
		WebElement unidadeRegional = driver.findElement(By.id("unidadeRegional_chosen"));
		unidadeRegional.click();
		WebElement procuraTipoRegional = driver.findElement(By.xpath("//li[@data-option-array-index='6']"));
		procuraTipoRegional.click(); 		
		
		WebElement btnPesquisar = driver.findElement(By.id("btnPesquisar"));
		btnPesquisar.click();
		
		WebElement entrar = driver.findElement(By.xpath("//td[@class='sorting_1']"));
		entrar.click();
		
		WebElement historico = driver.findElement(By.id("btnHistorico1"));
		historico.click();
		
		WebElement pesquisa = driver.findElement(By.id("btnPesquisaAvancada"));
		pesquisa.click();
		
		WebElement intervalo = driver.findElement(By.id("dataInicialHistorico"));
		intervalo.sendKeys("-09102015");
		
		WebElement dataFinal = driver.findElement(By.id("dataFinalHistorico"));
		dataFinal.sendKeys("-15102015");
		
		/*WebElement campoAlterado = driver.findElement(By.id("camposBeneficiario_chosen"));
		campoAlterado.sendKeys("CNJP");*/		
		
		WebElement modificado = driver.findElement(By.id("usuariosAlteracao_chosen"));
		modificado.click();
		WebElement modificadoInsere = driver.findElement(By.xpath("//*[@id='usuariosAlteracao_chosen']/div/div/input"));		
		modificadoInsere.sendKeys("Usuário MARPH");
		modificadoInsere.sendKeys(Keys.TAB);
		
		WebElement pesquisar = driver.findElement(By.id("btnPesquisar"));
		pesquisar.click();
		
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
}
