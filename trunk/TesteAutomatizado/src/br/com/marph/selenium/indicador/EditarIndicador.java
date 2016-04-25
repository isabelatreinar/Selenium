package br.com.marph.selenium.indicador;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.testeExclusao.VisualizarIndicador;
import br.com.marph.selenium.utils.LogOut;
import br.com.marph.selenium.utils.LogUtils;

public class EditarIndicador {
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

		MenuIndicadorTemplate.prepararAcessoIndicador(driver);

		PesquisarIndicador.pesquisar(driver);

		VisualizarIndicador.visualizar(driver);

		editar();

		if (driver.findElement(By.xpath("//ol[@class='breadcrumb']")).getText()
				.equalsIgnoreCase("Você está em: Indicador > Visualizar Indicador > Editar Indicador")) {
			validar();
		} else LogOut.logOut(driver);		

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

	public void editar() {
		// acessa botao de editar
		driver.findElement(By.id("btnEditar1")).click();

		// tipo de indicador
		driver.findElement(By.id("tipoIndicador_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoIndicador_chosen']/div/div/input")).sendKeys("Finalístico");
		driver.findElement(By.xpath("//*[@id='tipoIndicador_chosen']/div/div/input")).sendKeys(Keys.TAB);

		// tipo de fonte
		driver.findElement(By.id("tipoFonte_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoFonte_chosen']/div/div/input")).sendKeys("Oficial");
		driver.findElement(By.xpath("//*[@id='tipoFonte_chosen']/div/div/input")).sendKeys(Keys.TAB);

		// polaridade
		driver.findElement(By.id("polaridade_chosen")).click();
		driver.findElement(By.xpath("//*[@id='polaridade_chosen']/div/div/input")).sendKeys("Quanto maior, melhor");
		driver.findElement(By.xpath("//*[@id='polaridade_chosen']/div/div/input")).sendKeys(Keys.TAB);

		// media movel
		driver.findElement(By.id("mesesDaMediaMovel")).clear();
		driver.findElement(By.id("mesesDaMediaMovel")).sendKeys("12");

		// meses de defasagem
		driver.findElement(By.id("mesesDeDefasagem")).clear();
		driver.findElement(By.id("mesesDeDefasagem")).sendKeys("21");

		// nome do indicador
		driver.findElement(By.id("nomeIndicador")).clear();
		driver.findElement(By.id("nomeIndicador")).sendKeys("Indicador3");

		// nome da fonte
		driver.findElement(By.id("nomeFonte")).clear();
		driver.findElement(By.id("nomeFonte")).sendKeys("marph");

		// programa
		driver.findElement(By.id("programa_chosen")).click();
		driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input")).sendKeys("Farmácia de minas");
		driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input")).sendKeys(Keys.TAB);

		// descrição
		driver.findElement(By.id("descricao")).clear();
		driver.findElement(By.id("descricao")).sendKeys("TESTEEE");

		// avançar
		driver.findElement(By.id("btnSalvar1")).click();
	}

	private void validar() throws TesteAutomatizadoException {
		if (driver.findElement(By.xpath("//*[@id='tipoIndicador_chosen']/a/span")).getText()
				.equalsIgnoreCase("Tipo de Indicador")) {
			throw new TesteAutomatizadoException(EnumMensagens.TIPO_INDICADOR_EM_BRANCO, this.getClass());
		} else if (driver.findElement(By.xpath("//*[@id='tipoFonte_chosen']/a/span")).getText()
				.equalsIgnoreCase("Tipo de Fonte")) {
			throw new TesteAutomatizadoException(EnumMensagens.TIPO_DE_FONTE_EM_BRANCO, this.getClass());
		} else if (driver.findElement(By.xpath("//*[@id='polaridade_chosen']/a/span")).getText()
				.equalsIgnoreCase("Polaridade")) {
			throw new TesteAutomatizadoException(EnumMensagens.POLARIDADE_EM_BRANCO, this.getClass());
		} else if (StringUtils.isBlank(driver.findElement(By.id("mesesDaMediaMovel")).getAttribute("value"))) {
			throw new TesteAutomatizadoException(EnumMensagens.MESES_DA_MEDIA_MOVEL_EM_BRANCO, this.getClass());
		} else if (StringUtils.isBlank(driver.findElement(By.id("mesesDaMediaMovel")).getAttribute("value"))) {
			throw new TesteAutomatizadoException(EnumMensagens.MESES_DE_DEFASAGEM_EM_BRANCO, this.getClass());
		} else if (StringUtils.isBlank(driver.findElement(By.id("nomeIndicador")).getAttribute("value"))) {
			throw new TesteAutomatizadoException(EnumMensagens.NOME_INDICADOR_EM_BRANCO, this.getClass());
		} else if (StringUtils.isBlank(driver.findElement(By.id("nomeFonte")).getAttribute("value"))) {
			throw new TesteAutomatizadoException(EnumMensagens.NOME_DA_FONTE_EM_BRANCO, this.getClass());
		} else if (driver.findElement(By.xpath("//*[@id='programa_chosen']/a/span")).getText()
				.equalsIgnoreCase("Programa")) {
			throw new TesteAutomatizadoException(EnumMensagens.PROGRAMA_EM_BRANCO, this.getClass());
		} else if (StringUtils.isBlank(driver.findElement(By.id("descricao")).getAttribute("value"))) {
			throw new TesteAutomatizadoException(EnumMensagens.DESCRICAO_EM_BRANCO, this.getClass());
		} else
			throw new TesteAutomatizadoException(EnumMensagens.INDICADOR_JA_CADASTRADO, this.getClass());
	}
}