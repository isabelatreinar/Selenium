package br.com.marph.selenium.acessibilidade;

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
import br.com.marph.selenium.usuario.MenuUsuarioTemplate;
import br.com.marph.selenium.utils.LogUtils;

public class TestarAcessibilidade {
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
	public void TestarAcessibilidadePagina() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessar menu
		MenuUsuarioTemplate.prepararAcessoUsuario(driver);

		for (int i = 0; i <= 8; i++) {
			driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/ul/li[4]/a")).click();
		}
		
		for (int i = 0; i <= 3; i++) {
			driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/ul/li[2]/a")).click();
		}
		
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/ul/li[3]/a")).click();

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
