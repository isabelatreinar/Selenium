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
	public void realizaBusca() {

		log.info("Inicio do teste - Cadastro usuarios invalidos");

		long timestart = System.currentTimeMillis();

		MenuUsuarioTemplate.prepararAcessoUsuario(driver);

		WebElement botaoCadastrar = driver.findElement(By.id("btnNovoUsuario"));
		botaoCadastrar.click();

		cadastro();

		validacao();

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
		nome.sendKeys("TESTE");

		WebElement cpf = driver.findElement(By.id("usuarioCpf"));
		cpf.sendKeys("-46333558133");

		WebElement cargo = driver.findElement(By.id("cargo_chosen"));
		cargo.click();
		WebElement selecionarCargo = driver.findElement(By.xpath("//*[@id='cargo_chosen']/div/div/input"));
		selecionarCargo.sendKeys("Prefeito");
		selecionarCargo.sendKeys(Keys.TAB);

		WebElement btnAvancar = driver.findElement(By.id("btnSalvar"));
		btnAvancar.click();

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

		// WebElement voltar = driver.findElement(By.id("btnVoltar"));
		// voltar.click();
	}

	protected void validacao() {
		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='usuarioNome_label']/label/span")).getText())) {
			LogUtils.log(EnumMensagens.NOME_EM_BRANCO, this.getClass());
		}

		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='usuarioCpf_label']/label/span")).getText())) {
			LogUtils.log(EnumMensagens.CPF_EM_BRANCO, this.getClass());
		}

		if ("CPF inválido!".equals(driver.findElement(By.xpath("//*[@id='usuarioCpf_label']/label/span")).getText())) {
			LogUtils.log(EnumMensagens.CPF_INVALIDO, this.getClass());
		}

		if ("CPF já cadastrado."
				.equals(driver.findElement(By.xpath("//*[@id='usuarioCpf_label']/label/span")).getText())) {
			LogUtils.log(EnumMensagens.CPF_JA_CADASTRADO, this.getClass());
		}

		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='cargo_label']/label/span")).getText())) {
			LogUtils.log(EnumMensagens.CARGO_EM_BRANCO, this.getClass());
		}

		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='modalPerfil_label']/label/span")).getText())) {
			LogUtils.log(EnumMensagens.PERFIL_EM_BRANCO, this.getClass());
		}

		if ("Obrigatório!"
				.equals(driver.findElement(By.xpath("//*[@id='modalExtensaoPerfilId_label']/label/span")).getText())) {
			LogUtils.log(EnumMensagens.EXTENSAO_EM_BRANCO, this.getClass());
		}
	}
}
