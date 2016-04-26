package br.com.marph.selenium.base;

import java.util.List;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class EditarBaseLegal {
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	private List<String> erros;
	

	@Before
	public void startBrowser() {
		driver = new FirefoxDriver();
		Conexao.ip(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void realizaCadastro() throws Exception {

		/** 
		 * Teste de edição de Base Legal (Caminho Feliz)
		 * Neste teste são realizados em conjunto o teste de pesquisa de base legal
		 */
		
		// Recolhe informações de log
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acesso ao sistema com perfil "Administrador"
		AcessoSistema.perfilAdministrador(driver);
		
		// Acesso ao menu
		MenuBaseLegalTemplate.menuBaseLegal(driver);

		pesquisarBaseLegal(driver, "Resolução", "159");

		// Editar formulário
		testeEdicao();

		verificaValidacoes();

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
	
	public static void pesquisarBaseLegal(WebDriver driver, String tipo, String numero) {
		//Preencher campo "Tipo"
		driver.findElement(By.id("tipoBaseLegal_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(tipo);
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		//Preencher campo "Número"
		driver.findElement(By.id("numero")).sendKeys(numero);

		/*Pesquisar com data
		// Abrir o dataPicker 
		driver.findElement(By.id("dataPublicacao")).click();
		
		//Passando o dataPicker para uma tabela
		WebElement datePicker = driver.findElement(By.xpath("/html/body/div[5]/div[1]"));
		//List<WebElement> rows = datePicker.findElements(By.tagName("tr"));
		List<WebElement> columns = datePicker.findElements(By.tagName("td"));
		for(WebElement cell : columns){
			if(cell.getText().equals("23")){
				cell.click();
				break;
			}
		}		

		//Pesquisar com anoVigencia
		driver.findElement(By.id("dataVigencia_chosen")).click();
		driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys("2017");
		driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys(Keys.TAB); */
		
		//Clicar no botão pesquisar
		driver.findElement(By.id("btnPesquisar")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("sorting_1")));
		
		// Acessa a tela do registro pesquisado
		driver.findElement(By.className("sorting_1")).click();
	}
	
	private void testeEdicao() throws TesteAutomatizadoException {
		// Acesso formulário
		driver.findElement(By.id("btnEditar1")).click();

		// Alteração do campo "Tipo de Base Legal"
		driver.findElement(By.id("tipoBaseLegal_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys("Resolução");
		driver.findElement(By.xpath("//*[@id='tipoBaseLegal_chosen']/div/div/input")).sendKeys(Keys.TAB);

		// Alteração do campo "Número"
		driver.findElement(By.id("numero")).clear();
		driver.findElement(By.id("numero")).sendKeys("159");

		// Alteração da "Data da publicação"
		// Abrir o dataPicker 
		driver.findElement(By.id("dataPublicacao")).click();
		
		//Passando o dataPicker para uma tabela
		WebElement datePicker = driver.findElement(By.xpath("/html/body/div[5]/div[1]"));
		//List<WebElement> rows = datePicker.findElements(By.tagName("tr"));
		List<WebElement> columns = datePicker.findElements(By.tagName("td"));
		for(WebElement cell : columns){
			if(cell.getText().equals("24")){
				cell.click();
				break;
			}
		}		

		// Alteração do "Ano do início da vigência"
		driver.findElement(By.id("dataVigencia_chosen")).click();
		driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys("2014");
		driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		//driver.findElement(By.id("textoPublicado_inputFileType")).sendKeys("./data/TESTEEE.pdf");
		
		// Salvar
		driver.findElement(By.id("btnSalvar1")).click();
	}

	private void verificaValidacoes() throws TesteAutomatizadoException {
		// Valida breadCrumb
		if (!driver.findElement(By.xpath("//ol[@class='breadcrumb']")).getText()
				.equalsIgnoreCase("Você está em: Base Legal > Visualizar Base Legal > Editar Base Legal")) {
			 erros.add(EnumMensagens.BREADCRUMB_INCORRETO.getMensagem());
		}
		
		// Valida obrigatoriedade do tipo de base legal
		if (driver.findElement(By.id("tipoBaseLegal_chosen")).getText().equalsIgnoreCase("Tipo")) {
			erros.add(EnumMensagens.TIPO_VALIDACAO.getMensagem());
		}
		
		// Valida obrigatoriedade do número da base legal
		if (StringUtils.isBlank(driver.findElement(By.id("numero")).getAttribute("value"))) {
			erros.add(EnumMensagens.NUMERO_VALIDACAO.getMensagem());
		}
		
		// Valida obrigatoriedade da data da publicação
		if (StringUtils.isBlank(driver.findElement(By.id("dataPublicacao")).getAttribute("value"))) {
			erros.add(EnumMensagens.DATA_PUBLICACAO_VALIDACAO.getMensagem());
		} 
		
		// Valida obrigatoriedade da data da vigência
		if (driver.findElement(By.id("dataVigencia_chosen")).getText().equalsIgnoreCase("Ano do início da vigência")) {
			erros.add(EnumMensagens.DATA_VIGENCIA_VALIDACAO.getMensagem());
		}
	}
}

