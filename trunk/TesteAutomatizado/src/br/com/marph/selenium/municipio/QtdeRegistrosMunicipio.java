package br.com.marph.selenium.municipio;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class QtdeRegistrosMunicipio {
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
	public void qtdeRegistrosMunicipio() throws TesteAutomatizadoException, InterruptedException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessar menu
		MenuMunicipioTemplate.prepararAcessoMunicipio(driver);

		// Validar quantidade selecionada --> 10
		selecionarQuantidade(driver, "10");
		Thread.sleep(1000);
		System.out.println("10: " + contaRegistros(driver));
		if (contaRegistros(driver) != 10) {
			throw new TesteAutomatizadoException(EnumMensagens.QUANTIDADE_EXCEDIDA, this.getClass());
		}

		// validar quantidade selecionada --> 50
		selecionarQuantidade(driver, "50");
		Thread.sleep(1000); 
		System.out.println("50: " + contaRegistros(driver));
		if (contaRegistros(driver) != 50) {
			throw new TesteAutomatizadoException(EnumMensagens.QUANTIDADE_EXCEDIDA, this.getClass());
		}

		// validar quantidade selecionada --> 100
		selecionarQuantidade(driver, "100");
		Thread.sleep(1000); 
		System.out.println("100: " + contaRegistros(driver));
		if (contaRegistros(driver) != 100) {
			throw new TesteAutomatizadoException(EnumMensagens.QUANTIDADE_EXCEDIDA, this.getClass());
		}

		// se o campo estiver vazio o teste é finalizado com sucesso
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

	public void selecionarQuantidade(WebDriver driver, String valor) {
		Select selecionarValor = new Select(
				driver.findElement(By.xpath("//div[@id='municipioDataTable_length']/label/select")));
		selecionarValor.selectByVisibleText(valor);
	}

	public int contaRegistros(WebDriver driver) {
		List<WebElement> trColecao = driver.findElements(By.cssSelector("tbody tr"));
		return trColecao.size();
	}
}
