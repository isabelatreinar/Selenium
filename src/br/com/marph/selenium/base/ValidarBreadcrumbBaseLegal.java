package br.com.marph.selenium.base;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class ValidarBreadcrumbBaseLegal {
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
	public void visualizarBreadcrumbBaseLegal() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessar menu
		MenuBaseLegalTemplate.menuBaseLegal(driver);

		// Preencher os filtros de pesquisa
		PesquisarBaseLegal.pesquisar(driver);

		// visualizar base legal
		VisualizarBaseLegal.visualizar(driver);
		
		// visualizar histórico
		VisualizarHistoricoBaseLegal.visualizar(driver);
		
		// validar breadcrumb
		if(!driver.findElement(By.xpath("//ol[@class='breadcrumb small']")).getText().equalsIgnoreCase("Você está em: Base Legal > Visualizar Base Legal > Histórico")){
			throw new TesteAutomatizadoException(EnumMensagens.BREADCRUMB_INCORRETO, this.getClass());
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

}
