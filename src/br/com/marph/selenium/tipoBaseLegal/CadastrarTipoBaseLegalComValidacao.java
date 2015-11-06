package br.com.marph.selenium.tipoBaseLegal;

import java.util.concurrent.TimeUnit;

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

public class CadastrarTipoBaseLegalComValidacao {
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
	public void cadastroTipoBaseLegal() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuTipoBaseLegalTemplate.prepararAcessoTipoBaseLegal(driver);

		cadastro();

		if (driver.findElement(By.xpath("//ol[@class='breadcrumb small']")).getText()
				.equalsIgnoreCase("Você está em: Tipo de Base Legal > Novo Tipo de Base Legal")) {
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

	public void cadastro() throws TesteAutomatizadoException {
		WebElement cadastrar = driver.findElement(By.id("btnNovoTipoBaseLegal"));
		cadastrar.click();

		WebElement nome = driver.findElement(By.id("nomeTipoBaseLegal"));
		nome.sendKeys("TesteiMA");

		WebElement transferencia = driver.findElement(By.id("transferenciaRecursosFinanceiros_chosen"));
		transferencia.click();
		WebElement transferenciaSeleciona = driver
				.findElement(By.xpath("//*[@id='transferenciaRecursosFinanceiros_chosen']/div/div/input"));
		transferenciaSeleciona.sendKeys("Sim");
		transferenciaSeleciona.sendKeys(Keys.TAB);

		WebElement prestacao = driver.findElement(By.id("prestacaoMetas_chosen"));
		prestacao.click();
		WebElement prestacaoSeleciona = driver.findElement(By.xpath("//*[@id='prestacaoMetas_chosen']/div/div/input"));
		prestacaoSeleciona.sendKeys("Sim");
		prestacaoSeleciona.sendKeys(Keys.TAB);

		WebElement prestacaoContas = driver.findElement(By.id("prestacaoContas_chosen"));
		prestacaoContas.click();
		WebElement prestacaoContasSeleciona = driver
				.findElement(By.xpath("//*[@id='prestacaoContas_chosen']/div/div/input"));
		prestacaoContasSeleciona.sendKeys("Sim");
		prestacaoContasSeleciona.sendKeys(Keys.TAB);

		WebElement salvar = driver.findElement(By.id("btnSalvar"));
		salvar.click();
	}

	private void validarToolTip() throws TesteAutomatizadoException {

		boolean present = true;
		try {
			WebElement data1 = driver.findElement(By.id("nomeTipoBaseLegal"));
			data1.click();
			driver.findElement(By.xpath("//*[@id='nomeTipoBaseLegal_maindiv']/div")).isDisplayed();
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
		}
		if (present == true) {
			WebElement nome = driver.findElement(By.id("nomeTipoBaseLegal"));
			nome.click();

			if (driver.findElement(By.xpath("//*[@id='nomeTipoBaseLegal_maindiv']/div")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='nomeTipoBaseLegal_maindiv']/div")).getText()
							.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.NOME_EM_BRANCO, this.getClass());
			}

			if (driver.findElement(By.xpath("//*[@id='nomeTipoBaseLegal_maindiv']/div")).getText()
					.equalsIgnoreCase("Tipo de base legal já cadastrado.")) {
				throw new TesteAutomatizadoException(EnumMensagens.TIPO_DE_BASE_LEGAL_JA_CADASTRADA, this.getClass());
			}
		}

		if (driver.findElement(By.id("transferenciaRecursosFinanceiros_chosen")).isDisplayed()
				&& driver.findElement(By.xpath("//*[@id='transferenciaRecursosFinanceiros_chosen']/a/span")).getText()
						.equalsIgnoreCase("Transferência de recursos financeiros")) {
			WebElement transferencia = driver.findElement(By.id("transferenciaRecursosFinanceiros_chosen"));
			transferencia.click();
			if (driver.findElement(By.xpath("//*[@id='transferenciaRecursosFinanceiros_maindiv']/div[2]")).getText()
					.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.TRANSFERENCIA_DE_RECURSOS_EM_BRANCO,
						this.getClass());
			}
		}

		if (driver.findElement(By.id("prestacaoMetas_chosen")).isDisplayed()
				&& driver.findElement(By.xpath("//*[@id='prestacaoMetas_chosen']/a/span")).getText()
						.equalsIgnoreCase("Prestação de metas")) {
			WebElement transferencia = driver.findElement(By.id("prestacaoMetas_chosen"));
			transferencia.click();
			if (driver.findElement(By.xpath("//*[@id='prestacaoMetas_maindiv']/div[2]")).getText()
					.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.PRESTACAO_DE_METAS_EM_BRANCO, this.getClass());
			}
		}

		if (driver.findElement(By.id("prestacaoContas_chosen")).isDisplayed()
				&& driver.findElement(By.xpath("//*[@id='prestacaoContas_chosen']/a/span")).getText()
						.equalsIgnoreCase("Prestação de contas")) {
			WebElement transferencia = driver.findElement(By.id("prestacaoContas_chosen"));
			transferencia.click();
			if (driver.findElement(By.xpath("//*[@id='prestacaoContas_maindiv']/div[2]")).getText()
					.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.PRESTACAO_DE_CONTAS_EM_BRANCO, this.getClass());
			}
		}
	}
}
