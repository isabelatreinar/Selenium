package br.com.marph.selenium.subSecretaria;

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
import br.com.marph.selenium.utils.LogOut;
import br.com.marph.selenium.utils.LogUtils;

public class AtivarInativarSubSecretaria {
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
	public void realizaBusca() throws InterruptedException, TesteAutomatizadoException {
		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuSubSecretariaTemplate.prepararAcessoSubSecretaria(driver);

		PesquisarSubSecretaria.pesquisar(driver);

		VisualizarSubSecretaria.visualizar(driver);
		
		ativarInativar();
		
		LogOut.logOut(driver);

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

	private void ativarInativar() {
		driver.findElement(By.id("btnAtivarInativar")).click();
		
		if(driver.findElement(By.id("btnAtivarInativar")).getText().equalsIgnoreCase("Inativar Subsecretaria")){
			LogUtils.log(EnumMensagens.BLOCO_ATIVADO, this.getClass());
		}else if(driver.findElement(By.id("btnAtivarInativar")).getText().equalsIgnoreCase("Ativar Subsecretaria")){
			LogUtils.log(EnumMensagens.BLOCO_INATIVADO, this.getClass());
		}
	}	
}
