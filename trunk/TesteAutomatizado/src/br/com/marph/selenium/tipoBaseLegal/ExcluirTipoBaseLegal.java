package br.com.marph.selenium.tipoBaseLegal;

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
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class ExcluirTipoBaseLegal {
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
	public void excluirTipoBase() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuTipoBaseLegalTemplate.prepararAcessoTipoBaseLegal(driver);

		PesquisarTipoBaseLegal.pesquisar(driver);
		
		VisualizarTipoBaseLegal.visualizar(driver);

		excluir(driver);

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

	public static void excluir(WebDriver driver) throws TesteAutomatizadoException {
		WebElement excluir = driver.findElement(By.id("btnExcluir1"));
		excluir.click();
		if ("O tipo de base legal não pode ser excluído pois está vinculado a uma ou mais bases legais."
				.equalsIgnoreCase(
						driver.findElement(By.xpath("/html/body/div[5]/div[2]/div/div/div/div/div[3]")).getText())) {
			throw new TesteAutomatizadoException(EnumMensagens.TIPO_BASE_NAO_PODE_SER_EXCLUIDA,ExcluirTipoBaseLegal.class);
			//WebElement ok = driver.findElement(By.xpath("/html/body/div[5]/div[2]/div/div/div/div/div[4]/button"));
			//ok.click();		
		} else 	if (driver.findElement(By.xpath("/html/body/div[5]/div[2]/div/div/div/div/div[4]/button[1]")).isDisplayed()) {
			WebElement sim = driver.findElement(By.xpath("/html/body/div[5]/div[2]/div/div/div/div/div[4]/button[1]"));
			sim.click();		
		}else throw new TesteAutomatizadoException(EnumMensagens.TIPO_BASE_LEGAL_ERRO,ExcluirTipoBaseLegal.class);
	}
}
