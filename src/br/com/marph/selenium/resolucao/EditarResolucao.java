package br.com.marph.selenium.resolucao;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.utils.LogUtils;

public class EditarResolucao {
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
	public void realizaEdicao() {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuResolucaoTemplate.prepararAcessoResolucao(driver);

		PesquisarResolucao.pesquisar(driver);

		VisualizarResolucao.visualiza(driver);

		pegaAba(driver);

		editarResolucao(driver);

		editarBeneficiario(driver);

		editarIndicadores(driver);

		editarPeriodo(driver);

		WebElement voltar = driver.findElement(By.id("liIrParaListagem"));
		voltar.click();

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

	public static void pegaAba(WebDriver driver) {
		/**
		 * Pega em qual aba esta e volta para a primeira que é a aba de
		 * Resolução
		 */
		int valor = 0;
		List<WebElement> wizard = driver.findElements(By.xpath("//*[@id='wizard']/ul/li"));

		for (int i = 0; i < wizard.size(); i++) {
			if ("current".equals(wizard.get(i).getAttribute("class"))) {
				valor = i;
			}
		}

		if (valor == 1) {
			driver.findElement(By.id("btnAnterior")).click();
		} else if (valor == 2) {
			driver.findElement(By.id("btnAnterior")).click();
			driver.findElement(By.id("btnAnterior")).click();
		} else if (valor == 3) {
			driver.findElement(By.id("btnAnterior")).click();
			driver.findElement(By.id("btnAnterior")).click();
			driver.findElement(By.id("btnAnterior")).click();
		} else if (valor == 4) {
			driver.findElement(By.id("btnAnterior")).click();
			driver.findElement(By.id("btnAnterior")).click();
			driver.findElement(By.id("btnAnterior")).click();
			driver.findElement(By.id("btnAnterior")).click();
		}
	}
	
	public static void editarResolucao(WebDriver driver) {
		/**
		 * Edita aba de resolução
		 */
		//seleciona base
		driver.findElement(By.id("termosBaseLegal_chosen")).click();
		driver.findElement(By.xpath("//*[@id='termosBaseLegal_chosen']/div/ul/li[5]")).click();
		
		//descrição
		driver.findElement(By.id("descricao")).clear();
		driver.findElement(By.id("descricao")).sendKeys("Teste ");
		
		//tempo
		driver.findElement(By.id("tempoVigencia")).clear();
		driver.findElement(By.id("tempoVigencia")).sendKeys("10");
		
		//btn ok
		driver.findElement(By.xpath("/html/body/div[5]/div[2]/div/div/div/div/div[4]/button")).click();
		
		//salvar
		driver.findElement(By.id("btnSalvar1")).click();

		//proximo
		driver.findElement(By.id("btnProximo")).click();

	}
	
	public static void editarBeneficiario(WebDriver driver) {
		/**
		 * Importa planilha na aba de beneficiario fazendo edição
		 */
		//upload
		driver.findElement(By.id("buttonImportarPlanilha")).click();
		
		// arquivo
		driver.findElement(By.id("uploadBeneficiariosContemplados"))
				.sendKeys("C:\\Users\\rafael.sad\\Documents\\Export.xlsx");
		
		//importar
		driver.findElement(By.id("buttonImportar")).click();

		//btn proximo
		driver.findElement(By.id("btnProximo")).click();

	}
	
	public static void editarIndicadores(WebDriver driver) {
		/**
		 * Edita aba de Indicadores
		 */
		
		//TODO TERMINAR EDIÇÃO
		//editar
		driver.findElement(By.xpath("//*[@class='panel-heading']/ul/li[3]/a")).click();
		
		//ponto
		driver.findElement(By.xpath("//*[@class='tabelaIndicadores']/div[2]/div[3]/input")).clear();
		driver.findElement(By.xpath("//*[@class='tabelaIndicadores']/div[2]/div[3]/input")).sendKeys("6000");
		
		//peso
		driver.findElement(By.xpath("//*[@class='tabelaIndicadores']/div[2]/div[4]/input")).clear();
		driver.findElement(By.xpath("//*[@class='tabelaIndicadores']/div[2]/div[4]/input")).sendKeys("10000");

		//pre requisito  
		driver.findElement(By.xpath("//*[@class='tabelaIndicadores']/div[2]/div[5]/a")).click();

		//salvar
		driver.findElement(By.xpath("//*[@class='panel-heading']/ul/li[1]/a")).click();
		//proximo
		driver.findElement(By.id("btnProximo")).click();  
	} 

	public static void editarPeriodo(WebDriver driver) {
		/**
		 * Edita Aba de periodo de monitoramento
		 */
		//btn Editar
		driver.findElement(By.xpath("//*[@class='panel-heading']/ul/li[3]/a")).click();

		//data
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div/div[2]/div[2]/div/div/div/input")).clear();
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div/div[2]/div[2]/div/div/div/input")).sendKeys("-25102015");

		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div/div[2]/div[3]/div/div/div/input")).clear();
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div/div[2]/div[3]/div/div/div/input")).sendKeys("-31102015");
		
		//salvar
		driver.findElement(By.xpath("//*[@class='panel-heading']/ul/li[1]/a")).click();
	}
}
