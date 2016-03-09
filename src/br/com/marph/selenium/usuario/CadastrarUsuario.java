package br.com.marph.selenium.usuario;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.utils.LogUtils;

public class CadastrarUsuario {

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
	public void realizaBusca() throws Exception {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuUsuarioTemplate.prepararAcessoUsuario(driver);

		cadastro();

		perfil();

		float tempoGasto = (System.currentTimeMillis() - timestart);
		float tempoSegundos = tempoGasto / 1000;

		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos - FINALIZADO COM SUCESSO\n");

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

	private void cadastro() {

		driver.findElement(By.id("btnNovoUsuario")).click();

		// nome
		driver.findElement(By.id("usuarioNome")).sendKeys("Jóse hau");

		// email
		driver.findElement(By.id("usuarioEmail")).sendKeys("hua@gmail.com");

		/**
		 * É NECESSÁRIO O - PARA INSERIR O CPF CORRETAMENTE
		 * E O MESMO NUNCA DEVE SER CADASTRADO REPETIDO
		 * POIS SE JÁ EXISTIR NO BANCO,SERÁ RECUSADO.
		 */

		// cpf
		driver.findElement(By.id("usuarioCpf")).sendKeys("-78662617760"); 
		
		// cargo
		driver.findElement(By.id("cargo_chosen")).click();
		driver.findElement(By.xpath("//*[@id='cargo_chosen']/div/div/input")).sendKeys("Prefeito");
		driver.findElement(By.xpath("//*[@id='cargo_chosen']/div/div/input")).sendKeys(Keys.TAB);

		// avançar
		driver.findElement(By.id("btnSalvar1")).click();
	}

	private void perfil() throws InterruptedException {
		// perfil
		driver.findElement(By.id("modalPerfil_chosen")).click();
		driver.findElement(By.xpath("//*[@id='modalPerfil_chosen']/div/div/input")).sendKeys("Gestor do Beneficiário");
		driver.findElement(By.xpath("//*[@id='modalPerfil_chosen']/div/div/input")).sendKeys(Keys.TAB);

		// extensão
		driver.findElement(By.id("modalExtensaoPerfilId")).sendKeys("FUNDO MUNICIPAL DE SAÚDE DE ABAETÉ");
		Thread.sleep(5000);
		driver.findElement(By.id("ui-id-2")).click();

		// salvar
		driver.findElement(By.id("btnSalvar1")).click();
	}
}
