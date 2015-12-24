package br.com.marph.selenium.subSecretaria;

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

public class CadastrarSubSecretaria {
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
	public void realizaBusca() throws InterruptedException, TesteAutomatizadoException {
		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuSubSecretariaTemplate.prepararAcessoSubSecretaria(driver);

		cadastrar();

		if (driver.findElement(By.xpath("//ol[@class='breadcrumb small']")).getText()
				.equalsIgnoreCase("Você está em: Subsecretaria > Nova Subsecretaria")) {
			validar();
		}

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

	private void cadastrar() {
		driver.findElement(By.id("liSalvar")).click();

		driver.findElement(By.id("nome")).sendKeys("Teeste");

		driver.findElement(By.id("sigla")).sendKeys("te");
		
		//situação
		//driver.findElement(By.id("situacaoSubsecretaria")).click();

		driver.findElement(By.id("btnSalvar")).click();
	}

	private void validar() throws TesteAutomatizadoException {

		driver.findElement(By.id("nome")).click();
		if (driver.findElement(By.xpath("//*[@id='nome_maindiv']/div")).getText()
				.equalsIgnoreCase("Preenchimento obrigatório!")) {
			throw new TesteAutomatizadoException(EnumMensagens.NOME_EM_BRANCO, this.getClass());
		} else if (driver.findElement(By.xpath("//*[@id='nome_maindiv']/div")).getText()
				.equalsIgnoreCase("Subsecretaria já cadastrada.")) {
			throw new TesteAutomatizadoException(EnumMensagens.SUBSECRETARIA_JA_CADASTRADA, this.getClass());
		} else
			driver.findElement(By.id("sigla")).click();

		if (driver.findElement(By.xpath("//*[@id='sigla_maindiv']/div")).getText()
				.equalsIgnoreCase("Preenchimento obrigatório!")) {
			throw new TesteAutomatizadoException(EnumMensagens.SIGLA_EM_BRANCO, this.getClass());
		}
	}
}
