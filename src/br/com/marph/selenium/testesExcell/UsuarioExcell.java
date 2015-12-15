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

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.usuario.MenuUsuarioTemplate;
import br.com.marph.selenium.utils.LogUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
//TODO: TERMINAR EDIÇÃO. PROGRAMA COM ERROS
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

		MenuUsuarioTemplate.prepararAcessoUsuario(driver);
		WebElement botaoCadastrar = driver.findElement(By.id("btnNovoUsuario"));
		botaoCadastrar.click();

		cadastro();
		
		if ("CPF inválido!"
				.equals(driver.findElement(By.xpath("//*[@id='usuarioCpf_label']/label/span")).getText())) {
			throw new TesteAutomatizadoException(EnumMensagens.CPF_INVALIDO, this.getClass());
		}

		if ("CPF já cadastrado."
				.equals(driver.findElement(By.xpath("//*[@id='usuarioCpf_label']/label/span")).getText())) {
			throw new TesteAutomatizadoException(EnumMensagens.CPF_JA_CADASTRADO, this.getClass());
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

	protected void cadastro() throws TesteAutomatizadoException {
		/**@author rafael.sad
		 * Try catch para tratar a leitura dos dados na planilha e para
		 * alimentar os campos a serem testados no brownser.
		 */

			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setEncoding("ISO-8859-1");
			Workbook wb = null;
			try {
				wb = Workbook.getWorkbook(new File("./data/usuario.xls"), workbookSettings);
			} catch (BiffException | IOException e) {
				e.printStackTrace();
			}
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
				driver.findElement(By.id("usuarioNome")).sendKeys(nome);
			} else
				LogUtils.log(EnumMensagens.NOME_EM_BRANCO, this.getClass());

			if (StringUtils.isNotBlank(email)) {
				driver.findElement(By.id("usuarioEmail")).sendKeys(email);
			}

			if (StringUtils.isNotBlank(cpf)) {
				driver.findElement(By.id("usuarioCpf")).sendKeys(cpf);
			} else
				LogUtils.log(EnumMensagens.CPF_EM_BRANCO, this.getClass());

			if (StringUtils.isNotBlank(cargo)) {
				driver.findElement(By.id("cargo_chosen")).click();
				driver.findElement(By.xpath("//*[@id='cargo_chosen']/div/div/input")).sendKeys(cargo);
				driver.findElement(By.xpath("//*[@id='cargo_chosen']/div/div/input")).sendKeys(Keys.TAB);
			} else
				throw new TesteAutomatizadoException(EnumMensagens.CARGO_EM_BRANCO, this.getClass());

			if (StringUtils.isNotBlank(masp)) {
				driver.findElement(By.id("usuarioMasp")).clear();
				driver.findElement(By.id("usuarioMasp")).sendKeys(masp);
			}

			if (StringUtils.isNotBlank(telefone)) {
				driver.findElement(By.id("usuarioTelefone")).clear();
				driver.findElement(By.id("usuarioTelefone")).sendKeys(telefone);
			}

			if (StringUtils.isNotBlank(celular)) {
				driver.findElement(By.id("usuarioCelular")).clear();
				driver.findElement(By.id("usuarioCelular")).sendKeys(celular);
			}
			

			driver.findElement(By.id("btnSalvar1")).click();
			
			if (StringUtils.isNotBlank(perfil)) {
				driver.findElement(By.id("modalPerfil_chosen")).click();
				driver.findElement(By.xpath("//*[@id='modalPerfil_chosen']/div/div/input")).sendKeys(perfil);
				driver.findElement(By.xpath("//*[@id='modalPerfil_chosen']/div/div/input")).sendKeys(Keys.TAB);
			}

			if (StringUtils.isNotBlank(extensao)) {
				driver.findElement(By.id("modalExtensaoPerfilId")).sendKeys(extensao);
				driver.findElement(By.id("ui-id-1")).click();
				driver.findElement(By.id("ui-id-1")).sendKeys(Keys.TAB);
			} else
				throw new TesteAutomatizadoException(EnumMensagens.EXTENSAO_EM_BRANCO, this.getClass());

			driver.findElement(By.id("btnSalvar1")).click();
	}
}
