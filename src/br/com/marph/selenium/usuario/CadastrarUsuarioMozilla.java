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

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class CadastrarUsuarioMozilla {

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

		//nome
		driver.findElement(By.id("usuarioNome")).sendKeys("Jóse hau");

		//email
		driver.findElement(By.id("usuarioEmail")).sendKeys("hua@gmail.com");

		//cpf
		driver.findElement(By.id("usuarioCpf")).sendKeys("-78662617760");	//- E NECESSARIO PARA INSERIR O CPF CORRETAMENTE

		//cargo
		driver.findElement(By.id("cargo_chosen")).click();
		driver.findElement(By.xpath("//*[@id='cargo_chosen']/div/div/input")).sendKeys("Prefeito");
		driver.findElement(By.xpath("//*[@id='cargo_chosen']/div/div/input")).sendKeys(Keys.TAB);

		//avançar
		driver.findElement(By.id("btnSalvar")).click();

		//perfil
		driver.findElement(By.id("modalPerfil_chosen")).click();
		driver.findElement(By.xpath("//*[@id='modalPerfil_chosen']/div/div/input")).sendKeys("Gestor do Beneficiário");
		driver.findElement(By.xpath("//*[@id='modalPerfil_chosen']/div/div/input")).sendKeys(Keys.TAB);

		//extensão
		driver.findElement(By.id("modalExtensaoPerfilId")).sendKeys("FUNDO MUNICIPAL DE SAÚDE DE ABAETÉ");
		driver.findElement(By.id("ui-id-2")).click();
		
		//salvar
		driver.findElement(By.id("btnSalvar")).click();
	}
}
