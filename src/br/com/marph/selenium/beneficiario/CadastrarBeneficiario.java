package br.com.marph.selenium.beneficiario;

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

public class CadastrarBeneficiario {
	/**
	 * Esta classe valida a funcionalidade de seleção edição de beneficiario
	 */
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
	public void editarBeneficiario() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessar menu
		MenuBeneficiarioTemplate.prepararAcessoBeneficiario(driver);

		// Acessar tela de edição e editar beneficiario

		cadastrar();

		// se o toast for exibido e a mensagem estiver correta o teste se
		// encerra
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

	private void cadastrar() throws TesteAutomatizadoException {

		WebElement cadastrar = driver.findElement(By.id("btnNovoBeneficiario"));
		cadastrar.click();
		/**
		 * Verifica se a tela de edição acessada corresponde ao beneficiário
		 * selecionado O sinal de - é colocado devido a máscara no componente
		 */

		WebElement cnpj = driver.findElement(By.id("modalNovoCnpj"));
		cnpj.sendKeys("-17082892000113");

		WebElement tipo = driver.findElement(By.id("modalNovoTipo_chosen"));
		tipo.click();
		WebElement tipoSeleciona = driver.findElement(By.xpath("//*[@id='modalNovoTipo_chosen']/div/div/input"));
		tipoSeleciona.sendKeys("Entidade");
		tipoSeleciona.sendKeys(Keys.TAB);
		
		//VALIDAÇÃO EM DESENVOLVIMENTO.NÃO ESTÁ FUNCIONANDO CORRETAMENTE.

		if (driver.findElement(By.id("modalNovoCnpj_maindiv")).isDisplayed()
				&& driver.findElement(By.xpath("//*[@class='form-group  has-error']")).isDisplayed()) {
			cnpj.click();
			if (driver.findElement(By.xpath("//*[@id='modalNovoCnpj_maindiv']/div")).getText()
					.equalsIgnoreCase("CNPJ inválido!")) {
				throw new TesteAutomatizadoException(EnumMensagens.CNPJ_INVALIDO, getClass());
			}
		}

		if (driver.findElement(By.id("modalNovoCnpj_maindiv")).isDisplayed()
				&& driver.findElement(By.xpath("//*[@class='form-group  has-error']")).isDisplayed()) {
			cnpj.click();
			if (driver.findElement(By.xpath("//*[@id='modalNovoCnpj_maindiv']/div")).getText()
					.equalsIgnoreCase("Não foi possível buscar as informações do CAGEC.[CNPJ-CPF não localizado]!")) {
				System.err.println("UOLL");
				throw new TesteAutomatizadoException(EnumMensagens.CNPJ_INVALIDO, getClass());
			}
		}

		if (driver.findElement(By.id("modalNovoTipo_maindiv")).isDisplayed()
				&& driver.findElement(By.xpath("//*[@class='form-group has-error']")).isDisplayed()) {
			tipo.click();
			if (driver.findElement(By.xpath("//*[@id='modalNovoTipo_maindiv']/div")).getText()
					.equalsIgnoreCase("Obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.TIPO_EM_BRANCO, getClass());
			}
		} else {
			// Botao salvar superior
			WebElement salvar = driver.findElement(By.id("btnAvancar1"));
			salvar.click();
		}

	}

}
