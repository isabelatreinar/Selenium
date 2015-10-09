package br.com.marph.selenium.testesExcell;

import java.io.File;
import java.io.IOException;
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
import jxl.read.biff.BiffException;

public class UsuarioExcell {
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

		MenuUsuarioTemplate.prepararAcessoBaseLegal(driver);
		WebElement botaoCadastrar = driver.findElement(By.id("btnNovoUsuario"));
		botaoCadastrar.click();

		cadastro();
		
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

	protected void cadastro() throws TesteAutomatizadoException {
		/**@author rafael.sad
		 * Try catch para tratar a leitura dos dados na planilha e para
		 * alimentar os campos a serem testados no brownser.
		 */

		try {
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setEncoding("ISO-8859-1");
			Workbook wb = Workbook.getWorkbook(new File("./data/usuario.xls"), workbookSettings);
			Sheet sheet = wb.getSheet(0);
			String nome = sheet.getCell(0, 1).getContents();
			String email = sheet.getCell(1, 1).getContents();
			String cpf = sheet.getCell(2, 1).getContents();
			String cargo = sheet.getCell(3, 1).getContents();
			String masp = sheet.getCell(4, 1).getContents();
			String telefone = sheet.getCell(5, 1).getContents();
			String celular = sheet.getCell(6, 1).getContents();

			String perfil = sheet.getCell(0, 3).getContents();
			String extensao = sheet.getCell(1, 3).getContents();

			if (StringUtils.isNotBlank(nome)) {
				WebElement nomeCampo = driver.findElement(By.id("usuarioNome"));
				nomeCampo.sendKeys(nome);
			} else
				LogUtils.log(EnumMensagens.NOME_EM_BRANCO, this.getClass());

			if (StringUtils.isNotBlank(email)) {
				WebElement emailCampo = driver.findElement(By.id("usuarioEmail"));
				emailCampo.sendKeys(email);
			}

			if (StringUtils.isNotBlank(cpf)) {
				WebElement cpfCampo = driver.findElement(By.id("usuarioCpf"));
				cpfCampo.sendKeys(cpf);
			} else
				LogUtils.log(EnumMensagens.CPF_EM_BRANCO, this.getClass());

			if (StringUtils.isNotBlank(cargo)) {
				WebElement cargoCampo = driver.findElement(By.id("cargo_chosen"));
				cargoCampo.click();
				WebElement selecionarCargo = driver
						.findElement(By.xpath("//*[@id='cargo_chosen']/div/div/input"));
				selecionarCargo.sendKeys(cargo);
				selecionarCargo.sendKeys(Keys.TAB);
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
				celularCampo.clear();
				celularCampo.sendKeys(celular);
			}
			if ("CPF inválido!"
					.equals(driver.findElement(By.xpath("//*[@id='usuarioCpf_label']/label/span")).getText())) {
				throw new TesteAutomatizadoException(EnumMensagens.CPF_INVALIDO, this.getClass());
			}

			if ("CPF já cadastrado."
					.equals(driver.findElement(By.xpath("//*[@id='usuarioCpf_label']/label/span")).getText())) {
				throw new TesteAutomatizadoException(EnumMensagens.CPF_JA_CADASTRADO, this.getClass());
			}

			WebElement avancar = driver.findElement(By.id("btnSalvar1"));
			avancar.click();

			if (StringUtils.isNotBlank(perfil)) {
				WebElement perfilClica = driver.findElement(By.id("modalPerfil_chosen"));
				perfilClica.click();
				WebElement perfilPreenche = driver.findElement(By.xpath("//*[@id='modalPerfil_chosen']/div/div/input"));
				perfilPreenche.sendKeys(perfil);
				perfilPreenche.sendKeys(Keys.TAB);
			}

			if (StringUtils.isNotBlank(extensao)) {
				WebElement extensaoPerfil = driver.findElement(By.id("modalExtensaoPerfilId"));
				extensaoPerfil.sendKeys(extensao);
				WebElement extensaoSeleciona = driver.findElement(By.id("ui-id-1"));
				extensaoSeleciona.click();
				extensaoPerfil.sendKeys(Keys.TAB);
			} else
				throw new TesteAutomatizadoException(EnumMensagens.EXTENSAO_EM_BRANCO, this.getClass());

			WebElement salvar = driver.findElement(By.id("btnSalvar1"));
			salvar.click();

		} catch (BiffException | IOException e) {
			e.printStackTrace();
		}
	}
}
