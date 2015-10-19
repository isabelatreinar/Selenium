package br.com.marph.selenium.usuario;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.utils.LogUtils;

public class PesquisarUsuario {

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

	/*
	 * @After public void tearDown(){ driver.quit(); }
	 */

	@Test
	public void PesquisaUsuario() {
		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuUsuarioTemplate.prepararAcessoUsuario(driver);

		pesquisar(driver);

		float tempoGasto = (System.currentTimeMillis() - timestart);
		float tempoSegundos = tempoGasto / 1000;

		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos");

		if (tempoSegundos > 5000) {
			log.warn(sb.toString() + "\n");
		} else {
			log.info(sb.toString() + "\n");
		}

		/*
		 * sb.append("Entrada no sistema - "); sb.append(tempoGasto);
		 * sb.append("segundos");
		 */

	}

	public static void pesquisar(WebDriver driver) {
		WebElement nome = driver.findElement(By.id("nome"));
		nome.sendKeys("Isabela");

		WebElement cpf = driver.findElement(By.id("filtroUsuarioCpf"));
		cpf.sendKeys("-08936836633");
		/*
		 * WebElement botaoExpandir =
		 * driver.findElement(By.id("btnExpandirPesquisaAvancada"));
		 * botaoExpandir.click();
		 * 
		 * WebElement perfil = driver.findElement(By.id("perfil_chosen"));
		 * perfil.click(); WebElement perfilSeleciona =
		 * driver.findElement(By.xpath("//*[@id='perfil_chosen']/div/div/input")
		 * ); perfilSeleciona.sendKeys("Comissão de avaliação");
		 * perfilSeleciona.sendKeys(Keys.TAB);
		 */

		WebElement botaoPesquisar = driver.findElement(By.id("btnPesquisar"));
		botaoPesquisar.click();
	}
}
