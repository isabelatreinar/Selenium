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

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class NaoCancelarEdicaoBeneficiario {
	/*
	 * Esta classe valida o cancelamento da solicitação de cancelamento da edição
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
	public void cancelarEdicaoBeneficiario() throws TesteAutomatizadoException, InterruptedException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessa menu
		MenuBeneficiarioTemplate.prepararAcessoBeneficiario(driver);

		// Pesquisa um beneficiário na base de dados
		PesquisarBeneficiarioMozilla.pesquisar(driver);

		// visualizar beneficiario
		VisualizarBeneficiario.visualizar(driver);
		
		// Acessa a tela de edição do beneficiario
		driver.findElement(By.id("btnEditar1")).click();

		// Cancela a edição através do botão cancelar superior
		//driver.findElement(By.id("btnCancelar1")).click();
		
		// Cancela a edição através do botão cancelar inferior
		driver.findElement(By.id("btnCancelar")).click();
		
		// verifica se exibe o popup de confirmação
		/*if(driver.findElement(By.className("jconfirm-box")).isDisplayed()){
			throw new TesteAutomatizadoException(EnumMensagens.CONFIRMACAO_DESABILITADA, this.getClass());
		}
		else{*/
			WebElement btnNao = driver.findElement(By.xpath("/html/body/div[5]/div[2]/div/div/div/div/div[4]/button[2]"));
			btnNao.click();
			
			// verifica se o sistema redireciona para tela de visualizar beneficiario
			if(!driver.findElement(By.id("gridSystemModalLabel")).getText().equalsIgnoreCase("Editar beneficiário")){
				throw new TesteAutomatizadoException(EnumMensagens.TELA_INCORRETA, this.getClass());
			}
		//}

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
}
