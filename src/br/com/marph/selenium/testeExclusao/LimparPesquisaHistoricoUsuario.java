package br.com.marph.selenium.testeExclusao;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagensLog;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.usuario.MenuUsuarioTemplate;
import br.com.marph.selenium.usuario.PesquisarHistoricoUsuario;
import br.com.marph.selenium.usuario.PesquisarUsuario;
import br.com.marph.selenium.utils.LogUtils;

public class LimparPesquisaHistoricoUsuario {
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

		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessar menu
		MenuUsuarioTemplate.prepararAcessoUsuario(driver);

		// Preencher os filtros de pesquisa
		PesquisarUsuario.pesquisar(driver);

		// visualizar base legal
		VisualizarUsuario.visualizar(driver);
		
		// visualizar histórico
		VisualizarHistoricoUsuario.visualizar(driver);
		
		PesquisarHistoricoUsuario.pesquisar(driver);
		
		limpar(driver);

		// valida se a tela acessada é a correta
		if (!driver.findElement(By.id("gridSystemModalLabel")).getText().equalsIgnoreCase("Histórico")) {
			throw new TesteAutomatizadoException(EnumMensagensLog.TELA_INCORRETA, this.getClass());
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
	
	private void limpar(WebDriver driver) throws TesteAutomatizadoException{
		WebElement btnLimpar = driver.findElement(By.id("btnLimparPesquisa"));
		btnLimpar.click();
		
		WebElement pesquisar = driver.findElement(By.id("btnPesquisar"));
		pesquisar.click();
		
		// validar exclusão de dados dos campos 
		if (StringUtils.isNotBlank(driver.findElement(By.id("dataInicialHistorico")).getText())
				|| StringUtils.isNotBlank(driver.findElement(By.id("dataFinalHistorico")).getText())
				|| StringUtils.isNotBlank(driver.findElement(By.id("camposUsuario_chosen")).getText())
				|| !driver.findElement(By.id("usuariosAlteracao_chosen")).getText().equalsIgnoreCase("Modificado Por:")) {
			throw new TesteAutomatizadoException(EnumMensagensLog.CAMPO_PREENCHIDO, LimparPesquisaHistoricoMunicipio.class);
		}
	}
}
