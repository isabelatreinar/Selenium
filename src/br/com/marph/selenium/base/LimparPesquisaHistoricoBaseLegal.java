package br.com.marph.selenium.base;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class LimparPesquisaHistoricoBaseLegal {
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
	public void limparPesquisaBaseLegal() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessa menu Base Legal
		MenuBaseLegalTemplate.prepararAcessoBaseLegal(driver);

		// Realiza pesquisa de uma base
		PesquisarBaseLegal.pesquisar(driver);

		// Acessa a página da base legal selecionada
		VisualizarBaseLegal.visualizar(driver);

		// Acessa Histórico da Base Legal selecionada
		VisualizarHistoricoBaseLegal.visualizar(driver);

		// Acessa a pesquisa do histórico, se houver histórico
		pesquisar();
		
		// Limpar os filtros
		limpar();
		
		// validar exclusão de dados dos campos  ALTERAR TEXTO DO CAMPO MODIFICADO POR PARA data-placeholder
		if(StringUtils.isNotBlank(driver.findElement(By.id("dataInicialHistorico")).getText()) || 
				StringUtils.isNotBlank(driver.findElement(By.id("dataFinalHistorico")).getText()) ||
				StringUtils.isNotBlank(driver.findElement(By.id("camposUsuario_chosen")).getText()) || 
				!driver.findElement(By.id("usuariosAlteracao_chosen")).getText().equalsIgnoreCase("Modificado Por")){
			throw new TesteAutomatizadoException(EnumMensagens.CAMPO_PREENCHIDO, this.getClass());
		}
		
		// Se todos os campos estiverem em branco o teste é finalizado com sucesso
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

	public void pesquisar(){
		WebElement exibirPesquisa = driver.findElement(By.xpath("//button[@class='btn btCollapseOpen']"));
		exibirPesquisa.click();

		/*
		 * O sinal de menos é colocado antes da data para a máscara do campo
		 * seja considerada.
		 */
		WebElement dataInicial = driver.findElement(By.id("dataInicialHistorico"));
		dataInicial.sendKeys("-14012015");
		dataInicial.sendKeys(Keys.TAB);

		WebElement dataFinal = driver.findElement(By.id("dataFinalHistorico"));
		dataFinal.sendKeys("-14012015");
		dataFinal.sendKeys(Keys.TAB);

		WebElement campoAlterado = driver.findElement(By.xpath("//div[@id='camposUsuario_chosen']/ul/li/input"));
		campoAlterado.click();
		campoAlterado.sendKeys("Arquivo Importado");
		campoAlterado.sendKeys(Keys.ENTER);

		driver.findElement(By.id("usuariosAlteracao_chosen")).click();
		WebElement modificadoPor = driver.findElement(By.xpath("//div[@id='usuariosAlteracao_chosen']/div/div/input"));
		modificadoPor.click();
		modificadoPor.sendKeys("Usuário Marph");
		modificadoPor.sendKeys(Keys.ENTER);

		WebElement btnPesquisar = driver.findElement(By.id("btnPesquisar"));
		btnPesquisar.click();
	}
	
	private void limpar(){
		WebElement btnLimpar = driver.findElement(By.id("btnLimparPesquisa"));
		btnLimpar.click();
	}
}
