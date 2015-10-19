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
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class VerificarUsuarioCorreto {
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
	public void PesquisaUsuario() throws TesteAutomatizadoException {
		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuUsuarioTemplate.prepararAcessoUsuario(driver);

		PesquisarUsuario.pesquisar(driver);

		String nome = driver.findElement(By.xpath("//td[@class='sorting_1']")).getText();

		WebElement selecionar = driver.findElement(By.xpath("//td[@class='sorting_1']"));
		selecionar.click();

		String nomeCompara = driver.findElement(By.xpath("/html/body/div[2]/div[5]/div[1]/div/div[1]/div/p")).getText();

		if (!nome.equals(nomeCompara)) {
			throw new TesteAutomatizadoException(EnumMensagens.USUARIO_ERRADO, this.getClass());
		} else {
			WebElement perfil = driver.findElement(By.id("btnPerfil1"));
			perfil.click();
		}
		
		String nomeComparaPerfil = driver.findElement(By.xpath("//*[@id='ulNome']/span")).getText();
		
		if (!nome.equals(nomeComparaPerfil)) {
			throw new TesteAutomatizadoException(EnumMensagens.USUARIO_ERRADO_PERFIL, this.getClass());
		} else {
			System.out.println("Passou no teste");
		}

		float tempoGasto = (System.currentTimeMillis() - timestart);
		float tempoSegundos = tempoGasto / 1000;

		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos");

		if (tempoSegundos > 5000) {
			log.warn(sb.toString() + "\n");
		} else {
			log.info(sb.toString() + "\n");
		}

	}

}
