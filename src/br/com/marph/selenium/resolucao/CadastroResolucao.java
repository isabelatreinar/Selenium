package br.com.marph.selenium.resolucao;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class CadastroResolucao {
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	JavascriptExecutor js;

	@Before
	public void startBrowser() {
		driver = new FirefoxDriver();
		Conexao.ip(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		js = (JavascriptExecutor) driver;
	}

	@Test
	public void realizaBusca() throws InterruptedException, TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuResolucaoTemplate.prepararAcessoResolucao(driver);

		// cadastra resolucao
		cadastrarResolucao();

		// valida se ainda está na pagina de resolucao para validar e achar o
		// erro.
		if (driver.findElement(By.xpath("//*[@id='RESOLUCAO']")).getAttribute("class").equalsIgnoreCase("current")) {
			validarResolucao();
		}

		// cadastro de beneficiario
		beneficiarios();

		// valida se ainda esta na pagina de beneficiario e valida para achar o
		// erro.
		if (driver.findElement(By.xpath("//*[@id='BENEFICIARIOS_CONTEMPLADOS']")).getAttribute("class")
				.equalsIgnoreCase("current")) {
			validarBeneficiarios();
		}

		// cadastro de indicadores.
		indicadores();

		// cadastro de periodo.
		periodo();

		// cadastro de cronograma
		cronograma();

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

	protected void cadastrarResolucao() throws TesteAutomatizadoException {
		// clica no botão
		driver.findElement(By.id("btnNovaResolucao")).click();

		// Selecionar programa
		driver.findElement(By.id("programa_chosen")).click();
		driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input")).sendKeys("ProHosp");
		driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		// fim

		// valida programa

		if (driver.findElement(By.id("programa_chosen")).isDisplayed() && driver
				.findElement(By.xpath("//*[@id='programa_chosen']/a/span")).getText().equalsIgnoreCase("Programa")) {
			throw new TesteAutomatizadoException(EnumMensagens.PROGRAMA_EM_BRANCO, this.getClass());
		}

		// fim
		// numero resolucao
		driver.findElement(By.id("baseLegal")).sendKeys("405");
		driver.findElement(By.xpath("//li[@id='ui-id-7']"))
				.click(); /* NUMERO PARA PEGAR UTRA RESOLUÇÃO NA LISTAGEM */

		if (StringUtils.isBlank(driver.findElement(By.id("baseLegal-label")).getAttribute("value"))) {
			throw new TesteAutomatizadoException(EnumMensagens.NUMERO_EM_BRANCO, this.getClass());
		}

		if (StringUtils.isNotBlank(driver.findElement(By.id("baseLegal-label")).getAttribute("value"))) {
			boolean present1 = true;
			try {
				// data
				driver.findElement(By.id("baseLegal")).click();
				driver.findElement(By.xpath("//*[@id='baseLegal_maindiv']/div")).isDisplayed();
				present1 = true;
			} catch (NoSuchElementException e) {
				present1 = false;
			}

			if (present1 == true) {
				throw new TesteAutomatizadoException(EnumMensagens.RESOLUCAO_JA_CADASTRADA, this.getClass());
			}

		}

		// seleciona base
		driver.findElement(By.id("termosBaseLegal_chosen")).click();
		driver.findElement(By.xpath("//*[@id='termosBaseLegal_chosen']/div/ul/li[2]")).click();

		// tempo
		driver.findElement(By.id("tempoVigencia")).sendKeys("25");

		// tempo
		driver.findElement(By.id("descricao")).sendKeys("Teste TESTE");

		// salvar
		driver.findElement(By.id("btnSalvar1")).click();

		// avanca
		driver.findElement(By.id("btnProximo")).click();
	}

	protected void validarResolucao() throws TesteAutomatizadoException {

		try {
			driver.findElement(By.xpath("//*[@class='search-choice']")).isDisplayed();
		} catch (NoSuchElementException e) {
			throw new TesteAutomatizadoException(EnumMensagens.BASE_LEGAL_EM_BRANCO, this.getClass());
		}

		if (StringUtils.isBlank(driver.findElement(By.id("tempoVigencia")).getAttribute("value"))) {
			throw new TesteAutomatizadoException(EnumMensagens.TEMPO_EM_BRANCO, this.getClass());
		} else if (StringUtils
				.isBlank((String) js.executeScript("return document.getElementById('descricao').value"))) {
			throw new TesteAutomatizadoException(EnumMensagens.DESCRICAO_EM_BRANCO, this.getClass());
		} else {
			throw new TesteAutomatizadoException(EnumMensagens.DESCRICAO_EM_BRANCO, this.getClass());
		}
	}

	protected void beneficiarios() throws InterruptedException, TesteAutomatizadoException {

		// arquivo
		driver.findElement(By.id("uploadBeneficiariosContemplados"))
				.sendKeys("C:\\Users\\rafael.sad\\Documents\\Export.xlsx");// Export.xlsx

		// importa
		driver.findElement(By.id("buttonImportar")).click();

		// Thread.sleep(1000);

		try {
			if (driver.findElement(By.xpath("//*[@class='tooltip tooltip-error fade top in']")).getText()
					.equalsIgnoreCase(
							"Formato de arquivo inválido. Por favor selecione um arquivo no formato XLS ou XLSX.")) {
				throw new TesteAutomatizadoException(EnumMensagens.FORMATO_DE_ARQUIVO_INVALIDO, this.getClass());
			} else if (driver.findElement(By.xpath("//*[@class='tooltip tooltip-error fade top in']")).getText()
					.equalsIgnoreCase("Tamanho de arquivo não suportado. Selecione um arquivo com até 5 MB.")) {
				throw new TesteAutomatizadoException(EnumMensagens.TAMANHO_NAO_SUPORTADO, this.getClass());
			}
		} catch (NoSuchElementException e) {

		}

		// avancar
		driver.findElement(By.id("btnProximo")).click();
	}

	protected void validarBeneficiarios() throws TesteAutomatizadoException {

		driver.findElement(By.id("uploadBeneficiariosContemplados-txt")).click();

		try {
			if (driver.findElement(By.id("downloadTxt")).isDisplayed()) {
				throw new TesteAutomatizadoException(EnumMensagens.PDF_ERRO_DE_LOG, this.getClass());
			}

		} catch (NoSuchElementException e) {

		}

	}

	protected void indicadores() throws TesteAutomatizadoException {

		// criar
		driver.findElement(By.id("criar")).click();

		// nome
		driver.findElement(By.id("nome")).sendKeys("Teste");

		// INDICADOR
		driver.findElement(By.xpath("//*[@id='collapseNovo']/div/ul/li[1]/a")).click();
		driver.findElement(By.xpath("//*[@data-label-field='nomeIndicador']")).sendKeys("taxa");
		driver.findElement(By.id("ui-id-2")).click();

		// ponto
		driver.findElement(By.xpath("//*[@id='tabelaIndicadoresNovo']/div[2]/div[3]/input")).sendKeys("5000");

		// peso
		driver.findElement(By.xpath("//*[@id='tabelaIndicadoresNovo']/div[2]/div[4]/input")).sendKeys("10000");

		// PreRequisito
		driver.findElement(By.xpath("//*[@id='tabelaIndicadoresNovo']/div[2]/div[5]/a")).click();

		// salvar
		driver.findElement(By.xpath("//*[@id='headingNovo']/ul/li[1]/a")).click();

		try {
			if (driver.findElement(By.xpath("//*[@id='toast-container']")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='toast-container']/div/div[2]")).getText()
							.equalsIgnoreCase("O nome do modelo não pode ser vazio.")) {
				throw new TesteAutomatizadoException(EnumMensagens.NOME_DO_MODELO_EM_BRANCO, this.getClass());
			} else if (driver.findElement(By.xpath("//*[@id='toast-container']")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='toast-container']/div/div[2]")).getText()
							.equalsIgnoreCase("O preenchimento do campo indicador é obrigatório.")) {
				throw new TesteAutomatizadoException(EnumMensagens.INDICADOR_EM_BRANCO, this.getClass());
			} else if (driver.findElement(By.xpath("//*[@id='toast-container']")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='toast-container']/div/div[2]")).getText()
							.equalsIgnoreCase("O peso do indicador finalístico não pode ser menor que 0.")) {
				throw new TesteAutomatizadoException(EnumMensagens.INDICADOR_EM_BRANCO, this.getClass());
			}

		} catch (NoSuchElementException e) {
		}

		driver.findElement(By.id("btnProximo")).click();
	}

	protected void periodo() throws TesteAutomatizadoException {

		driver.findElement(By.xpath("//*[@class='panel-heading']/ul/li[3]/a")).click();

		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/ul/input")).sendKeys("1");

		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/ul/a")).click();

		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div/div[3]/div[2]/div/div/div/input"))
				.sendKeys("-19102015");

		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div/div[3]/div[3]/div/div/div/input"))
				.sendKeys("-22102015");

		driver.findElement(By.xpath("//*[@class='panel-heading']/ul/li[1]/a")).click();

		try {
			if (driver.findElement(By.xpath("//*[@id='toast-container']")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='toast-container']/div/div[2]")).getText()
							.equalsIgnoreCase("O início do período de monitoramento não pode ser vazio.")) {
				throw new TesteAutomatizadoException(EnumMensagens.INICIO_DO_PERIODO_EM_BRANCO, this.getClass());
			} else if (driver.findElement(By.xpath("//*[@id='toast-container']")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='toast-container']/div/div[2]")).getText()
							.equalsIgnoreCase("O fim do período de monitoramento não pode ser vazio.")) {
				throw new TesteAutomatizadoException(EnumMensagens.FIM_DO_PERIODO_EM_BRANCO, this.getClass());
			}

		} catch (NoSuchElementException e) {
		}
		driver.findElement(By.id("btnProximo")).click();
	}

	protected void cronograma() throws TesteAutomatizadoException {
		// clica em criar
		driver.findElement(By.id("criarCronograma")).click();

		// coloca o nome
		driver.findElement(By.xpath("//*[@id='accordion']/div/div[1]/span/input")).sendKeys("Teste");

		// defini numero de parcelas
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div[2]/input")).sendKeys("1");

		// clica em atualizar
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div[2]/a")).click();

		// colocar data
		driver.findElement(
				By.xpath("//*[@class='panel-collapse collapse in']/div/div[3]/div[2]/div[2]/div/div/div/input"))
				.sendKeys("-20112018");

		// valor padrão
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div[3]/div[2]/div[3]/input"))
				.sendKeys("200000");

		// percentual fixo
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div[3]/div[2]/div[4]/input"))
				.sendKeys("1000");

		// percentual de custeio
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div[3]/div[2]/div[5]/input"))
				.sendKeys("500");

		// formula de calculo para parcelar
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/ul/li[1]/a")).click();

		// adicionar
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']"
				+ "/div/div[1]/div/div[2]/div[2]/div/div[2]/div/div/div[1]/div[1]/div[2]/a")).click();

		// concluir
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div[1]/div/div[1]/ul/li[1]/a"))
				.click();

		// usar formula de calculo para bonus
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/ul/li[2]/a")).click();

		// adicionar
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']"
				+ "/div/div[1]/div[2]/div[2]/div[2]/div/div[2]/div/div/div[1]/div/div[2]/a")).click();

		// concluir

		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div[1]/div[2]/div[1]/ul/li[1]/a"))
				.click();

		// salvar
		driver.findElement(By.xpath("//*[@id='accordion']/div/div[1]/ul/li[1]/a")).click();

		try {
			if (driver.findElement(By.xpath("//*[@id='toast-container']")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='toast-container']/div/div[2]")).getText().equalsIgnoreCase(
							"Deve existir pelo menos uma parcela cadastrada para o modelo de pagamento.")) {
				throw new TesteAutomatizadoException(EnumMensagens.DEVE_EXISTIR_PARCELA_CADASTRADA, this.getClass());
			} else if (driver.findElement(By.xpath("//*[@id='toast-container']")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='toast-container']/div/div[2]")).getText()
							.equalsIgnoreCase("O nome do modelo não pode ser vazio.")) {
				throw new TesteAutomatizadoException(EnumMensagens.NOME_EM_BRANCO, this.getClass());
			} else if (driver.findElement(By.xpath("//*[@id='toast-container']")).isDisplayed()
					&& driver.findElement(By.xpath("//*[@id='toast-container']/div/div[2]")).getText()
							.equalsIgnoreCase("Existe fórmula em edição.")) {
				throw new TesteAutomatizadoException(EnumMensagens.EXISTE_FORMULA_EM_EDICAO, this.getClass());
			}

		} catch (NoSuchElementException e) {

		}

	}

}