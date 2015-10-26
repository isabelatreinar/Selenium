package br.com.marph.selenium.usuario;

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

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class PesquisarHistoricoUsuario {
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
	public void visualizarUsuario() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessar menu
		MenuUsuarioTemplate.prepararAcessoUsuario(driver);

		// Preencher os filtros de pesquisa
		PesquisarUsuario.pesquisar(driver);

		// visualizar base legal
		VisualizarUsuario.visualizar(driver);
		
		// visualizar histórico
		VisualizarHistoricoUsuario.visualizar(driver);
		
		pesquisar(driver);

		// valida se a tela acessada é a correta
		if (!driver.findElement(By.id("gridSystemModalLabel")).getText().equalsIgnoreCase("Histórico")) {
			throw new TesteAutomatizadoException(EnumMensagens.TELA_INCORRETA, this.getClass());
		}

		// Se a tela e a base legal forem os corretos o teste se encerra
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
		WebElement pesquisa = driver.findElement(By.id("btnExpandirPesquisaAvancada"));
		pesquisa.click();
		
		WebElement dataInicial = driver.findElement(By.id("dataInicialHistorico"));
		dataInicial.sendKeys("-20102015");
		
		WebElement dataFinal = driver.findElement(By.id("dataInicialHistorico"));
		dataFinal.sendKeys("-25102015");
		
		WebElement campoAlterado = driver.findElement(By.xpath("//div[@id='camposUsuario_chosen']/ul/li/input"));
		campoAlterado.click();
		campoAlterado.sendKeys("Cargo");
		campoAlterado.sendKeys(Keys.ENTER);
		
		driver.findElement(By.id("usuariosAlteracao_chosen")).click();
		WebElement modificadoPor = driver.findElement(By.xpath("//div[@id='usuariosAlteracao_chosen']/div/div/input"));
		modificadoPor.click();
		modificadoPor.sendKeys("Usuário Marph");
		modificadoPor.sendKeys(Keys.ENTER);
		
		WebElement btnPesquisar = driver.findElement(By.id("btnPesquisar"));
		btnPesquisar.click();
		
	}
}
