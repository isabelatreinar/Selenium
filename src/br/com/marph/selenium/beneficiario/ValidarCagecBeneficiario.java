package br.com.marph.selenium.beneficiario;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class ValidarCagecBeneficiario {
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
	public void atualizarCagecBeneficiario() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessar menu
		MenuBeneficiarioTemplate.prepararAcessoBeneficiario(driver);

		// Pesquisar Beneficiario
		PesquisarBeneficiarioMozilla.pesquisar(driver);

		// Acessa a tela de visualização do beneficiário
		VisualizarBeneficiario.visualizar(driver);

		String dataHoraUltimaAtualizacao = driver.findElement(By.id("dataHoraUltimaAtualizacao")).getText();
		
		// Atualiza os dados de acordo com o CAGEC
		driver.findElement(By.id("atualizarCagec")).click();
		
		// Valida se os dados foram atualizados
		// se o campo Ultima atualização não sofrer alteração significa que os dados não foram atualizados
		if(dataHoraUltimaAtualizacao.equalsIgnoreCase(driver.findElement(By.id("dataHoraUltimaAtualizacao")).getText())){
			new TesteAutomatizadoException(EnumMensagens.CAGEC_NAO_VALIDADO, this.getClass());
		}
		
		else{
			// se o campo última atualização tiver mudado o teste é finalizado com sucesso
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
}
