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

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.utils.LogUtils;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;

public class VisualizarBeneficiario {
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);

	public static String beneficiarioSelecionado;

	@Before
	public void startBrowser() {
		driver = new FirefoxDriver();
		Conexao.ip(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void visualizarBeneficiario() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessa menu
		MenuBeneficiarioTemplate.prepararAcessoBaseLegal(driver);

		// Pesquisa um beneficiário na base de dados
		PesquisarBeneficiarioMozilla.pesquisar(driver);

		// visualizar beneficiario
		visualizar(driver);

		// valida se a tela acessada é a correta
		if (!driver.findElement(By.id("gridSystemModalLabel")).getText().equalsIgnoreCase("Visualizar beneficiário")) {
			throw new TesteAutomatizadoException(EnumMensagens.TELA_INCORRETA, this.getClass());
		}
		// se a tela é a correta -> verifica se é do beneficiario correto
		if (!beneficiarioSelecionado.equalsIgnoreCase(driver.findElement(By.id("modalVisualizarNome")).getText())) {
			throw new TesteAutomatizadoException(EnumMensagens.BENEFICIARIO_INCORRETO, this.getClass());
		}

		// Se a tela e o beneficiario forem os corretos o teste se encerra
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

	public static void visualizar(WebDriver driver) {
		WebElement beneficiario = driver.findElement(By.xpath("//td[@class='sorting_1']"));
		beneficiarioSelecionado = beneficiario.getText();
		beneficiario.click();
	}

	public static String getBeneficiarioSelecionado() {
		return beneficiarioSelecionado;
	}
}
