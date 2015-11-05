package br.com.marph.selenium.usuario;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class CadastroUsuarioInvalido {

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
	public void realizaBusca() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuUsuarioTemplate.prepararAcessoUsuario(driver);

		WebElement botaoCadastrar = driver.findElement(By.id("btnNovoUsuario"));
		botaoCadastrar.click();

		cadastro();

		if (driver.findElement(By.xpath("//ol[@class='breadcrumb small']")).getText()
				.equalsIgnoreCase("Você está em: Usuário > Novo Usuário")) {
			validacaoToolTip();
		} // Só valida se permanecer na página de novo usuário

		perfilCadastra();

		if (driver.findElement(By.xpath("//ol[@class='breadcrumb small']")).getText()
				.equalsIgnoreCase("Você está em: Usuário > Novo Usuário > Novo Perfil")) {
			validacaoToolTipPerfil();
		} // Só valida se permanecer na página de novo perfil

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

	protected void cadastro() {

		WebElement nome = driver.findElement(By.id("usuarioNome"));
		nome.sendKeys("TESTEE");

		WebElement cpf = driver.findElement(By.id("usuarioCpf"));
		cpf.sendKeys("-38555260876");

		WebElement cargo = driver.findElement(By.id("cargo_chosen"));
		cargo.click();
		WebElement selecionarCargo = driver.findElement(By.xpath("//*[@id='cargo_chosen']/div/div/input"));
		selecionarCargo.sendKeys("Prefeito");
		selecionarCargo.sendKeys(Keys.TAB);

		WebElement btnAvancar = driver.findElement(By.id("btnSalvar"));
		btnAvancar.click();
	}

	protected void validacaoToolTip() throws TesteAutomatizadoException {
		if (StringUtils.isBlank(driver.findElement(By.id("usuarioNome")).getAttribute("value"))) {
			WebElement input = driver.findElement(By.id("usuarioNome"));
			input.click();
			if (driver.findElement(By.xpath("//*[@id='usuarioNome_maindiv']/div")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='usuarioNome_maindiv']/div")).getText()
							.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.NOME_EM_BRANCO, this.getClass());
			}
		}

		boolean present = true;
		try {
			WebElement cpf = driver.findElement(By.id("usuarioCpf"));
			cpf.click();
			driver.findElement(By.xpath("//*[@id='usuarioCpf_maindiv']/div")).isDisplayed();
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
		}

		if (present == true) {
			WebElement cpf = driver.findElement(By.id("usuarioCpf"));
			cpf.click();
			if (driver.findElement(By.xpath("//*[@id='usuarioCpf_maindiv']/div")).getText()
					.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.CPF_EM_BRANCO, this.getClass());
			}

			if (driver.findElement(By.xpath("//*[@id='usuarioCpf_maindiv']/div")).getText()
					.equalsIgnoreCase("CPF já cadastrado.")) {
				throw new TesteAutomatizadoException(EnumMensagens.CPF_JA_CADASTRADO, this.getClass());
			}

			if (driver.findElement(By.xpath("//*[@id='usuarioCpf_maindiv']/div")).getText()
					.equalsIgnoreCase("CPF inválido!")) {
				throw new TesteAutomatizadoException(EnumMensagens.CPF_INVALIDO, this.getClass());
			}
		}

		if (driver.findElement(By.id("cargo_maindiv")).isDisplayed()
				&& driver.findElement(By.xpath("//*[@class='form-group has-error']")).isDisplayed()) {
			WebElement cargo = driver.findElement(By.id("cargo_chosen"));
			cargo.click();
			if (driver.findElement(By.xpath("//*[@id='cargo_maindiv']/div[2]")).getText()
					.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.CARGO_EM_BRANCO, this.getClass());
			}
		}
	}

	public void perfilCadastra() {
		WebElement perfil = driver.findElement(By.id("modalPerfil_chosen"));
		perfil.click();
		WebElement selecionarPerfil = driver.findElement(By.xpath("//*[@id='modalPerfil_chosen']/div/div/input"));
		selecionarPerfil.sendKeys("Gestor do Beneficiário");
		selecionarPerfil.sendKeys(Keys.TAB);

		WebElement extensao = driver.findElement(By.id("modalExtensaoPerfilId"));
		extensao.sendKeys("uni");
		WebElement extensaoSeleciona = driver.findElement(By.id("ui-id-2"));
		extensaoSeleciona.click();

		WebElement salvar = driver.findElement(By.id("btnSalvar"));
		salvar.click();

	}

	protected void validacaoToolTipPerfil() throws TesteAutomatizadoException {
		if (driver.findElement(By.id("modalPerfil_maindiv")).isDisplayed()
				&& driver.findElement(By.xpath("//*[@class='form-group has-error']")).isDisplayed()) {
			WebElement perfil = driver.findElement(By.id("modalPerfil_chosen"));
			perfil.click();
			if (driver.findElement(By.xpath("//*[@id='modalPerfil_maindiv']/div[2]")).getText()
					.equalsIgnoreCase("Obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.PERFIL_EM_BRANCO, this.getClass());
			}
		}

		// FAZER VALIDAÇÃO DE EXTENSÃO,POIS ESTÁ SENDO CORRIGIDO O ERRO.
	}

}
