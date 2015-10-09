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

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class EditarUsuario {
	private final String LOG_NAME = "RAFAEL";
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
	public void realizaBusca() throws Exception {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuUsuarioTemplate.prepararAcessoBaseLegal(driver);

		cadastrar();

		validacao();

		boolean validar = driver.findElement(By.id("toast-container")).isDisplayed();

		if (validar == true) {
			LogUtils.log(EnumMensagens.BASE_LEGAL_VALIDADO, this.getClass());
		} else {
			throw new TesteAutomatizadoException(EnumMensagens.BASE_LEGAL_NAO_VALIDADO, this.getClass());
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

	private void validacao() {
		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='usuarioNome_label']/label/span")).getText())) {
			log.info("Campo de nome estava em branco - Obrigatório");
		}

		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='cargo_label']/label/span")).getText())) {
			log.info("Campo de cargo estava em branco - Obrigatório");
		}
	}

	private void cadastrar() {
		String idUsuario = "rowId2159";

		WebElement selecionarUsuario = driver.findElement(By.id(idUsuario));
		selecionarUsuario.click();

		WebElement usuario = driver.findElement(By.xpath("//*[@id='" + idUsuario + "']/td[2]"));
		usuario.click();

		WebElement btnEditar = driver.findElement(By.id("btnEditar1"));
		btnEditar.click();

		WebElement nome = driver.findElement(By.id("usuarioNome"));
		nome.clear();
		// nome.sendKeys("TESTE");

		WebElement cargo = driver.findElement(By.id("cargo_chosen"));
		cargo.click();

		WebElement selecionarCargo = driver.findElement(By.xpath("//li[@data-option-array-index='4']"));
		selecionarCargo.click();

		WebElement salvar = driver.findElement(By.id("btnSalvar"));
		salvar.click();
	}
}
