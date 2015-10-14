package br.com.marph.selenium.testesExcell;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.usuario.MenuUsuarioTemplate;
import br.com.marph.selenium.utils.LogUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class UsuarioPesquisarExcell {
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
	public void teste() throws Exception {
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		MenuUsuarioTemplate.prepararAcessoUsuario(driver);

		pesquisaEdicao();

		validar();

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

	protected void validar() throws TesteAutomatizadoException {
		boolean validar = driver.findElement(By.id("toast-container")).isDisplayed();

		if (validar == true) {
			LogUtils.log(EnumMensagens.USUARIO_VALIDADO, this.getClass());
		} else {
			throw new TesteAutomatizadoException(EnumMensagens.USUARIO_NAO_VALIDADO, this.getClass());
		}
	}

	protected void pesquisaEdicao() {
		try {
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setEncoding("ISO-8859-1");
			Workbook wb = Workbook.getWorkbook(new File("./data/usuarioPesquisa.xls"), workbookSettings);
			Sheet sheet = wb.getSheet(0);
			String nome = sheet.getCell(0, 2).getContents();
			String cpf = sheet.getCell(1, 2).getContents();
			String perfil = sheet.getCell(2, 2).getContents();
			String extensao = sheet.getCell(3, 2).getContents();
			String situacao = sheet.getCell(4, 2).getContents();
			String cargoPesquisa = sheet.getCell(5, 2).getContents();
			String nomeEditado = sheet.getCell(0, 5).getContents();
			String email = sheet.getCell(1, 5).getContents();
			String cargo = sheet.getCell(2, 5).getContents();
			String masp = sheet.getCell(3, 5).getContents();
			String telefone = sheet.getCell(4, 5).getContents();
			String celular = sheet.getCell(5, 5).getContents();

			if (StringUtils.isNotBlank(nome)) {
				WebElement nome1 = driver.findElement(By.id("nome"));
				nome1.sendKeys(nome);
			}
			if (StringUtils.isNotBlank(cpf)) {
				WebElement cpf1 = driver.findElement(By.id("filtroUsuarioCpf"));
				cpf1.sendKeys(cpf);
			}

			WebElement pesquisaAvancada = driver.findElement(By.id("btnExpandirPesquisaAvancada"));
			pesquisaAvancada.click();

			if (StringUtils.isNotBlank(perfil)) {
				WebElement perfilClica = driver.findElement(By.id("perfil_chosen"));
				perfilClica.click();
				WebElement perfilPreenche = driver.findElement(By.xpath("//*[@id='perfil_chosen']/div/div/input"));
				perfilPreenche.sendKeys(perfil);
				perfilPreenche.sendKeys(Keys.TAB);
			}

			if (StringUtils.isNotBlank(extensao)) {
				WebElement extensaoPreenche = driver.findElement(By.id("extensaoPerfilId"));
				extensaoPreenche.sendKeys(extensao);
				extensaoPreenche.sendKeys(Keys.TAB);
			}

			if (StringUtils.isNotBlank(situacao)) {
				WebElement situacaoClica = driver.findElement(By.id("statusSituacaoPerfil_chosen"));
				situacaoClica.click();
				WebElement situacaoPreenche = driver
						.findElement(By.xpath("//*[@id='statusSituacaoPerfil_chosen']/div/div/input"));
				situacaoPreenche.sendKeys(situacao);
				situacaoPreenche.sendKeys(Keys.TAB);
			}

			if (StringUtils.isNotBlank(cargoPesquisa)) {
				WebElement cargoClica = driver.findElement(By.id("cargo_chosen"));
				cargoClica.click();
				WebElement cargoPreenche = driver.findElement(By.xpath("//*[@id='cargo_chosen']/div/div/input"));
				cargoPreenche.sendKeys(cargoPesquisa);
				cargoPreenche.sendKeys(Keys.TAB);
			}

			WebElement botaoPesquisar = driver.findElement(By.id("btnPesquisar"));
			botaoPesquisar.click();

			WebElement usuario = driver.findElement(By.xpath("//td[@class='sorting_1']"));
			usuario.click();

			WebElement botaoEditar = driver.findElement(By.id("btnEditar1"));
			botaoEditar.click();

			if (StringUtils.isNotBlank(nomeEditado)) {
				WebElement nomeEditar = driver.findElement(By.id("usuarioNome"));
				nomeEditar.clear();
				nomeEditar.sendKeys(nomeEditado);
			}

			if (StringUtils.isNotBlank(email)) {
				WebElement email1 = driver.findElement(By.id("usuarioEmail"));
				email1.clear();
				email1.sendKeys(email);
			}

			if (StringUtils.isNotBlank(cargo)) {
				WebElement cargoCampo = driver.findElement(By.id("cargo_chosen"));
				cargoCampo.click();
				WebElement selecionarCargo = driver.findElement(By.xpath("//*[@id='cargo_chosen']/div/div/input"));
				selecionarCargo.sendKeys(cargo);
			} else
				throw new TesteAutomatizadoException(EnumMensagens.CARGO_EM_BRANCO, this.getClass());

			if (StringUtils.isNotBlank(masp)) {
				WebElement maspCampo = driver.findElement(By.id("usuarioMasp"));
				maspCampo.clear();
				maspCampo.sendKeys(masp);
			}

			if (StringUtils.isNotBlank(telefone)) {
				WebElement telefoneCampo = driver.findElement(By.id("usuarioTelefone"));
				telefoneCampo.clear();
				telefoneCampo.sendKeys(telefone);
			}

			if (StringUtils.isNotBlank(celular)) {
				WebElement celularCampo = driver.findElement(By.id("usuarioCelular"));
				celularCampo.sendKeys(celular);
			}

			WebElement salvar = driver.findElement(By.id("btnSalvar"));
			salvar.click();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
