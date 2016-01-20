package br.com.marph.selenium.resolucao;

import java.io.IOException;
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
import br.com.marph.selenium.utils.AcessoUtils;
import br.com.marph.selenium.utils.LogUtils;

public class CadastroResolucao {
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	JavascriptExecutor js;
	String nomeIndicador = "teste";
	
	@Before
	public void startBrowser() {
		driver = new FirefoxDriver();
		Conexao.ip(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		js = (JavascriptExecutor) driver;
	}

	@Test
	public void realizaBusca() throws InterruptedException, TesteAutomatizadoException, IOException {

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
		
		indicadoresXCronograma();
		
		modelosXBeneficiarios();
		
		importacaoDeMetas();
		
		importacaoDeParcelas();

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
		
		AcessoUtils.idClick(driver, "btnNovaResolucao","programa_chosen");
		
		AcessoUtils.xpathChoosenSend(driver, "//*[@id='programa_chosen']/div/div/input", "Dell",Keys.TAB);
		
		// valida programa

		if (driver.findElement(By.id("programa_chosen")).isDisplayed() && driver
				.findElement(By.xpath("//*[@id='programa_chosen']/a/span")).getText().equalsIgnoreCase("Programa")) {
			throw new TesteAutomatizadoException(EnumMensagens.PROGRAMA_EM_BRANCO, this.getClass());
		}

		// fim
		// numero resolucao
		driver.findElement(By.id("baseLegal")).sendKeys("407");
		driver.findElement(By.xpath("//li[@id='ui-id-3']"))
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
		
		AcessoUtils.xpathClick(driver, "//*[@id='termosBaseLegal_chosen']","//*[@id='termosBaseLegal_chosen']/div/ul/li[2]");

		// tempo
		driver.findElement(By.id("tempoVigencia")).sendKeys("25");

		// tempo
		driver.findElement(By.id("descricao")).sendKeys("Teste TESTE");
		
		AcessoUtils.idClick(driver, "btnSalvar1","btnProximo");
		
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

		driver.findElement(By.id("buttonImportarPlanilha")).click();
		
		// arquivo
		driver.findElement(By.id("uploadBeneficiariosContemplados"))
				.sendKeys("C:\\Users\\rafael.sad\\Downloads\\Geicom\\Export.xlsx");// Export.xlsx

		// importa
		driver.findElement(By.id("buttonImportar")).click();

		 Thread.sleep(5000);
			
		if (driver.findElement(By.id("toast-container")).getText().equalsIgnoreCase("Existem erros no formulário.")) {
			driver.findElement(By.id("uploadBeneficiariosContemplados")).click();
			if (driver.findElement(By.xpath("//*[@id='divImportarPlanilha']/div/div/div[2]/div/div")).getText()
					.equalsIgnoreCase(
							"Formato de arquivo inválido. Por favor selecione um arquivo no formato XLS ou XLSX.")) {
				throw new TesteAutomatizadoException(EnumMensagens.FORMATO_DE_ARQUIVO_INVALIDO, this.getClass());
			} else if (driver.findElement(By.xpath("//*[@id='divImportarPlanilha']/div/div/div[2]/div/div")).getText()
					.equalsIgnoreCase("Tamanho de arquivo não suportado. Selecione um arquivo com até 5 MB.")) {
				throw new TesteAutomatizadoException(EnumMensagens.TAMANHO_NAO_SUPORTADO, this.getClass());
			}else
				throw new TesteAutomatizadoException(EnumMensagens.ARQUIVO_EM_BRANCO, this.getClass());
		} else {
			// avancar
			driver.findElement(By.id("btnProximo")).click();
		}	
	}

 	protected void validarBeneficiarios() throws TesteAutomatizadoException {

 		if (driver.findElement(By.xpath("//*[@class='toast-message']")).getText().equalsIgnoreCase("Corrija os erros do log e importe o arquivo novamente para prosseguir.")) {
			if (driver.findElement(By.id("buttonDownloadLogErros")).isDisplayed()) {
				throw new TesteAutomatizadoException(EnumMensagens.ARQUIVO_COM_ERRO_DE_LOG, this.getClass());
			}
 		}
	}

	protected void indicadores() throws TesteAutomatizadoException {
		
		try {
			if(driver.findElement(By.id("mensagemNaoPrestacaoMetas")).getText().equalsIgnoreCase("Não é necessário inserir informações nesta aba.")){
				throw new TesteAutomatizadoException(EnumMensagens.INDICADOR_FIM, this.getClass());
			}
		} catch (NoSuchElementException e) {
			
		}
		
		// criar
		driver.findElement(By.id("criar")).click();

		// nome
		driver.findElement(By.id("nome")).sendKeys(nomeIndicador);

		// INDICADOR
		driver.findElement(By.xpath("//*[@id='collapseNovo']/div/ul/li[1]/a")).click();
		driver.findElement(By.xpath("//*[@data-label-field='nomeIndicador']")).sendKeys("teste");
		driver.findElement(By.id("ui-id-2")).click();

		// ponto
		driver.findElement(By.xpath("//*[@id='tabelaIndicadoresNovo']/div[2]/div[3]/input")).sendKeys("5000");

		// peso
		driver.findElement(By.xpath("//*[@id='tabelaIndicadoresNovo']/div[2]/div[4]/input")).sendKeys("10000");
		
		
		AcessoUtils.xpathClick(driver, "//*[@id='tabelaIndicadoresNovo']/div[2]/div[5]/a","//*[@id='headingNovo']/ul/li[1]/a");


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

		/*try {
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
		}*/
		
		driver.findElement(By.id("btnProximo")).click();
	} 

	protected void cronograma() throws TesteAutomatizadoException, InterruptedException {
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
				.sendKeys("-29112018");

		// valor padrão
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div[3]/div[2]/div[3]/input"))
				.sendKeys("200000");

		// percentual fixo
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div[3]/div[2]/div[4]/input"))
				.sendKeys("1000");

		// percentual de custeio
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div[3]/div[2]/div[5]/input"))
				.sendKeys("500");
		
		AcessoUtils.xpathClick(driver, "//*[@class='panel-collapse collapse in']/div/ul/li[1]/a","//*[@class='panel-collapse collapse in']"
				+ "/div/div[1]/div/div[2]/div[2]/div/div[2]/div/div/div[1]/div[1]/div[2]/a",
				"//*[@class='panel-collapse collapse in']/div/div[1]/div/div[1]/ul/li[1]/a",
				"//*[@id='accordion']/div/div[1]/ul/li[1]/a");
		

		/*try {
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

		}*/
		
		driver.findElement(By.id("btnProximo")).click();
	}

	protected void indicadoresXCronograma(){
		
		AcessoUtils.xpathClick(driver, "//*[@class='panel-heading']/ul/li[3]/a","//*[@class='chosen-container chosen-container-single']/a/div/b");
		
		AcessoUtils.xpathChoosenSend(driver, "//*[@class='chosen-container chosen-container-single chosen-with-drop chosen-container-active']/div/div/input", "teste",Keys.TAB);
		
		AcessoUtils.xpathClick(driver, "//*[@class='panel-collapse collapse in']/div/div[2]/ul/li/a","//*[@class='panel-heading']/ul/li[1]/a");
		
		driver.findElement(By.id("btnProximo")).click();
	}
	
	protected void modelosXBeneficiarios() throws InterruptedException{
		
		driver.findElement(By.id("buttonImportarPlanilha")).click();
		
		driver.findElement(By.id("modeloIndicadorImportar_chosen")).click();
		
		AcessoUtils.xpathChoosenSend(driver, "//*[@id='modeloIndicadorImportar_chosen']/div/div/input", "teste",Keys.TAB);
		
		driver.findElement(By.id("uploadBeneficiariosContemplados")).sendKeys("C:\\Users\\rafael.sad\\Downloads\\Geicom\\modeloBeneficiarioExport.xlsx");
		
		driver.findElement(By.id("buttonImportar")).click();
		
		Thread.sleep(8000);
		
		driver.findElement(By.id("btnProximoBottom")).click();
		
	}	
		
	public void importacaoDeMetas() throws IOException, InterruptedException{		
		
		driver.findElement(By.id("buttonImportarPlanilha")).click();
		
		driver.findElement(By.id("uploadMetasPactuadas")).sendKeys("C:\\Users\\rafael.sad\\Downloads\\Geicom\\importacaoMetasExport.xlsx");
		
		driver.findElement(By.id("buttonImportar")).click();
		
		Thread.sleep(20000);
		
		driver.findElement(By.id("btnProximo")).click();
		
		
/*		XSSFWorkbook workbook = null;		
		File arquivo = new File("C:\\Users\\rafael.sad\\Downloads\\importacaoMetasExport.xlsx");		
		FileOutputStream outPut = null;		
		
		try {			
			workbook = new XSSFWorkbook(arquivo);			
			XSSFSheet planilha = workbook.getSheetAt(0);			
			outPut = new FileOutputStream(arquivo);						
//			XSSFRow cabecalho = planilha.getRow(0);
			Random gerador = new Random();			
			for (int i = 1; i < planilha.getLastRowNum() ; i++) {
				XSSFRow linha = planilha.getRow(i);
				Cell celula = linha.createCell(3);
				celula.setCellValue( String.valueOf(gerador.nextInt(100)));
			}
			workbook.write(outPut);			
		} catch (InvalidFormatException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
		finally {
			if(outPut != null){
				outPut.close();
			}
			if(workbook != null){
				workbook.close();
			}						
		}*/		
	}	
	
	protected void importacaoDeParcelas() throws InterruptedException{
		
		driver.findElement(By.id("buttonImportarPlanilha")).click();
		
		driver.findElement(By.id("uploadValorParcelas")).sendKeys("C:\\Users\\rafael.sad\\Downloads\\Geicom\\importacaoParcelasExport.xlsx");
		
		driver.findElement(By.id("buttonImportar")).click();
		
		Thread.sleep(8000);
		
		driver.findElement(By.id("btnFinalizar")).click();
		
	}
}