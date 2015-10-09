package br.com.marph.selenium.usuario;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class CadastrarUsuarioMozilla {

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

	/*
	 * @After public void tearDown(){ driver.quit(); }
	 */

	@Test
	public void realizaBusca() throws Exception {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuUsuarioTemplate.prepararAcessoBaseLegal(driver);

		cadastro();

		boolean validar = driver.findElement(By.id("toast-container")).isDisplayed();

		if (validar == true) {
			LogUtils.log(EnumMensagens.BASE_LEGAL_VALIDADO, this.getClass());
		} else {
			throw new TesteAutomatizadoException(EnumMensagens.BASE_LEGAL_NAO_VALIDADO, this.getClass());
		}

		// *[@id="modalPerfil_chosen"]/div/div/input
		// FIM

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
		WebElement botaoCadastrar = driver.findElement(By.id("btnNovoUsuario"));
		botaoCadastrar.click();

		WebElement nome = driver.findElement(By.id("usuarioNome"));
		nome.sendKeys("Ana Maria Oliveira");

		WebElement email = driver.findElement(By.id("usuarioEmail"));
		email.sendKeys("ana@gmail.com");

		WebElement cpf = driver.findElement(By.id("usuarioCpf"));
		cpf.sendKeys("-88752594378");

		WebElement cargo = driver.findElement(By.id("cargo_chosen"));
		cargo.click();

		WebElement selecionarCargo = driver.findElement(By.xpath("//*[@id='cargo_chosen']/div/div/input"));
		selecionarCargo.sendKeys("Prefeito");
		selecionarCargo.sendKeys(Keys.TAB);

		WebElement btnAvancar = driver.findElement(By.id("btnSalvar"));
		btnAvancar.click();

		WebElement perfil = driver.findElement(By.id("modalPerfil_chosen"));
		perfil.click();

		WebElement selecionarPerfil = driver.findElement(By.xpath("//li[@data-option-array-index='3']"));
		selecionarPerfil.click();

		WebElement extensao = driver.findElement(By.id("modalExtensaoPerfilId"));
		extensao.sendKeys("uni");
		WebElement extensaoSeleciona = driver.findElement(By.id("ui-id-2"));
		extensaoSeleciona.click();

		WebElement salvar = driver.findElement(By.id("btnSalvar"));
		salvar.click();
	}
}
