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
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class EditarBaseLegalMozilla {
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
		
		PesquisarBaseLegal.pesquisar(driver);
		
		VisualizarBaseLegal.visualizar(driver);

		edicaoCampos();
		
		if (driver.findElement(By.xpath("//ol[@class='breadcrumb small']")).getText()
				.equalsIgnoreCase("Você está em: Base Legal > Visualizar Base Legal > Editar Base Legal")) {
			validarToolTip();
		}	

		boolean validar = driver.findElement(By.id("toast-container")).isDisplayed();

		if (validar == true) {
			LogUtils.log(EnumMensagens.BASE_LEGAL_VALIDADO, this.getClass());
		} else {
			throw new TesteAutomatizadoException(EnumMensagens.BASE_LEGAL_NAO_VALIDADO, this.getClass());
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

	private void edicaoCampos() throws TesteAutomatizadoException {

		//editar
		driver.findElement(By.id("btnEditar1")).click();

		//tipoBase
		driver.findElement(By.id("tipoBaseLegal_chosen")).click();		
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys("Resolução");
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		//numero
		driver.findElement(By.id("numero")).clear();
		driver.findElement(By.id("numero")).sendKeys("5005");

		//data
		driver.findElement(By.id("dataPublicacao")).clear();
		driver.findElement(By.id("dataPublicacao")).sendKeys("-22092015");
		driver.findElement(By.id("dataPublicacao")).sendKeys(Keys.TAB);
		
		boolean present = true;
		try {
			//numero valida
			driver.findElement(By.id("numero")).click();
			driver.findElement(By.xpath("//*[@id='numero_maindiv']/div")).isDisplayed();
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
		}

		if (present == true) {
			driver.findElement(By.id("numero")).click();
			if (driver.findElement(By.xpath("//*[@id='numero_maindiv']/div")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='numero_maindiv']/div")).getText()
							.equalsIgnoreCase("Existe tipo de base legal cadastrado com esse número")) {
				throw new TesteAutomatizadoException(EnumMensagens.BASE_LEGAL_JA_CADASTRADA, this.getClass());
			}
		}
		
		//pdf
		driver.findElement(By.id("textoPublicado")).sendKeys("C:\\Users\\rafael.sad\\Documents\\TESTEEE.pdf");
		
		boolean present1 = true;
		try {
			//data
			driver.findElement(By.id("dataVigencia_chosen")).click();
			driver.findElement(By.xpath("//*[@role='tooltip']")).isDisplayed();
			present1 = true;
		} catch (NoSuchElementException e) {
			present1 = false;
		}

		if (present1 == true) {
			throw new TesteAutomatizadoException(EnumMensagens.DATA_PUBLICACAO_EM_BRANCO, this.getClass());
		}

		//ano vigencia
		driver.findElement(By.id("dataVigencia_chosen")).click();
		driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys("2019");
		driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		//salvar
		driver.findElement(By.id("btnSalvar")).click();
	}
	
	private void validarToolTip() throws TesteAutomatizadoException {
		if (driver.findElement(By.id("tipoBaseLegal_chosen")).isDisplayed()
				&& driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/a/span")).getText().equals("Tipo")) {
			//valida tipo
			driver.findElement(By.id("tipoBaseLegal_chosen")).click();
			if (driver.findElement(By.xpath("//*[@id='tipoBaseLegal_maindiv']/div[2]")).getText()
					.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.TIPO_EM_BRANCO, this.getClass());
			}
		}

		if (StringUtils.isBlank(driver.findElement(By.id("numero")).getAttribute("value"))) {
			//valida numero
			driver.findElement(By.id("numero")).click();
			if (driver.findElement(By.xpath("//*[@id='numero_maindiv']/div")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='numero_maindiv']/div")).getText()
							.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.NUMERO_EM_BRANCO, this.getClass());
			}

		}

		if (driver.findElement(By.id("dataVigencia_chosen")).isDisplayed()
				&& driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/a/span")).getText().equals("Ano do início da vigência")) {
			//valida vigencia
			driver.findElement(By.id("dataVigencia_chosen")).click();
			if (driver.findElement(By.xpath("//*[@id='dataVigencia_maindiv']/div[2]")).getText()
					.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.ANO_EM_BRANCO, this.getClass());
			}
		}
		
		if (StringUtils.isBlank(driver.findElement(By.id("textoPublicado_hide")).getAttribute("value"))) {
			//valida texto
			driver.findElement(By.id("textoPublicado-txt")).click();
			if (driver.findElement(By.xpath("//*[@class='col-md-6 uploadFile']/div")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@class='col-md-6 uploadFile']/div")).getText()
							.equalsIgnoreCase("Preenchimento obrigatório!")) {
				throw new TesteAutomatizadoException(EnumMensagens.NUMERO_EM_BRANCO, this.getClass());
			}
		}
		
	}
}
