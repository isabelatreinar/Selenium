package br.com.marph.selenium.beneficiario;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class EditarBeneficiario {
	/**
	 * Esta classe valida a funcionalidade de seleção edição de beneficiario
	 **/
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

		// Pesquisar Beneficiario
		PesquisarBeneficiarioMozilla.pesquisar(driver);

		// Acessa a tela de visualização do beneficiário
		VisualizarBeneficiario.visualizar(driver);

		// Acessar tela de edição e editar beneficiario
		driver.findElement(By.id("btnEditar1"));
		editar();

		// se o toast for exibido e a mensagem estiver correta o teste se encerra
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

	private void editar() throws TesteAutomatizadoException {
		/*
		 * Verifica se a tela de edição acessada corresponde ao beneficiário
		 * selecionado O sinal de - é colocado devido a máscara no componente
		 */
		if (driver.findElement(By.id("modalVisualizarNome")).getText()
				.equalsIgnoreCase(VisualizarBeneficiario.getBeneficiarioSelecionado())) {
			WebElement cnpj = driver.findElement(By.id("cnpj"));
			cnpj.sendKeys("-10954933000171");

			WebElement justificativa = driver.findElement(By.id("justificativa"));
			justificativa.sendKeys("Teste");

			WebElement salvar = driver.findElement(By.id("btnSalvar"));
			salvar.click();

			// valida exibição do toast
			if (driver.findElement(By.id("toast-conteiner")).isDisplayed()) {
				throw new TesteAutomatizadoException(EnumMensagens.TOAST_DESABILITADO, this.getClass());
			}
			// valida mensagem exibida
			else if (!driver.findElement(By.xpath("/div/div[2]")).getText()
					.equalsIgnoreCase("Beneficiário salvo com sucesso.")) {
				throw new TesteAutomatizadoException(EnumMensagens.MENSAGEM_INCORRETA, this.getClass());
			}
		}
		// se o beneficiário não for o selecionado -> erro
		else {
			throw new TesteAutomatizadoException(EnumMensagens.BENEFICIARIO_INCORRETO, this.getClass());
		}
	}

}
