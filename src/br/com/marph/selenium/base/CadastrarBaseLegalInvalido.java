
package br.com.marph.selenium.base;

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

public class CadastrarBaseLegalInvalido {
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
	public void realizaCadastro() throws Exception {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuBaseLegalTemplate.prepararAcessoBaseLegal(driver);

		cadastrar();
		
		if (driver.findElement(By.xpath("//ol[@class='breadcrumb small']")).getText()
				.equalsIgnoreCase("Você está em: Base Legal > Nova Base Legal")) {
			validarToolTip();
		}		

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
		// CADASTRO
		WebElement btnNovoUsu = driver.findElement(By.id("btnNovaBaseLegal"));
		btnNovoUsu.click();

		WebElement tipoBase = driver.findElement(By.id("tipoBaseLegal_chosen"));
		tipoBase.click();
		WebElement procuraTipoBase = driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input"));
		procuraTipoBase.sendKeys("Deliberação");
		procuraTipoBase.sendKeys(Keys.TAB);

		WebElement numero = driver.findElement(By.id("numero"));
		numero.sendKeys("123711856");

		WebElement data = driver.findElement(By.id("dataPublicacao"));
		data.sendKeys("-12082015");
		data.sendKeys(Keys.TAB);

		boolean present = true;
		try {
			WebElement numero1 = driver.findElement(By.id("numero"));
			numero1.click();
			driver.findElement(By.xpath("//*[@id='numero_maindiv']/div")).isDisplayed();
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
		}

		if (present == true) {
			WebElement input = driver.findElement(By.id("numero"));
			input.click();
			if (driver.findElement(By.xpath("//*[@id='numero_maindiv']/div")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='numero_maindiv']/div")).getText()
							.equalsIgnoreCase("Existe tipo de base legal cadastrado com esse número")) {
				throw new TesteAutomatizadoException(EnumMensagens.BASE_LEGAL_JA_CADASTRADA, this.getClass());
			}
		}

		driver.findElement(By.id("textoPublicado")).sendKeys("C:\\Users\\rafael.sad\\Documents\\TESTEEE.pdf");

		boolean present1 = true;
		try {
			WebElement data1 = driver.findElement(By.id("dataVigencia_chosen"));
			data1.click();
			driver.findElement(By.xpath("//*[@role='tooltip']")).isDisplayed();
			present1 = true;
		} catch (NoSuchElementException e) {
			present1 = false;
		}

		if (present1 == true) {
			throw new TesteAutomatizadoException(EnumMensagens.DATA_PUBLICACAO_EM_BRANCO, this.getClass());
		}

		WebElement anoVigencia = driver.findElement(By.id("dataVigencia_chosen"));
		anoVigencia.click();
		WebElement anoVigenciaSeleciona = driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input"));
		anoVigenciaSeleciona.sendKeys("2015");
		anoVigenciaSeleciona.sendKeys(Keys.TAB);

		WebElement salvar = driver.findElement(By.id("btnSalvar"));
		salvar.click();
	}

	private void validarToolTip() throws TesteAutomatizadoException {

		if (driver.findElement(By.id("tipoBaseLegal_chosen")).isDisplayed()
				&& driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/a/span")).getText().equalsIgnoreCase("Tipo")) {
			WebElement tipo = driver.findElement(By.id("tipoBaseLegal_chosen"));
			tipo.click();
			if (driver.findElement(By.xpath("//*[@id='tipoBaseLegal_maindiv']/div[2]")).getText()
					.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.TIPO_EM_BRANCO, this.getClass());
			}
		}

		if (StringUtils.isBlank(driver.findElement(By.id("numero")).getAttribute("value"))) {
			WebElement numero = driver.findElement(By.id("numero"));
			numero.click();
			if (driver.findElement(By.xpath("//*[@id='numero_maindiv']/div")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='numero_maindiv']/div")).getText()
							.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.NUMERO_EM_BRANCO, this.getClass());
			}

		}

		if (driver.findElement(By.id("dataVigencia_chosen")).isDisplayed()
				&& driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/a/span")).getText()
				.equalsIgnoreCase("Ano do início da vigência")) {
			WebElement tipo = driver.findElement(By.id("dataVigencia_chosen"));
			tipo.click();
			if (driver.findElement(By.xpath("//*[@id='dataVigencia_maindiv']/div[2]")).getText()
					.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.ANO_EM_BRANCO, this.getClass());
			}
		}
		
		if (StringUtils.isBlank(driver.findElement(By.id("textoPublicado_hide")).getAttribute("value"))) {
			WebElement numero = driver.findElement(By.id("textoPublicado-txt"));
			numero.click();
			if (driver.findElement(By.xpath("//*[@class='col-md-6 uploadFile']/div")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@class='col-md-6 uploadFile']/div")).getText()
							.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.NUMERO_EM_BRANCO, this.getClass());
			}
		}
	}
}