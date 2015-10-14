package br.com.marph.selenium.base;

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
import org.openqa.selenium.support.ui.Select;

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class QtdeRegistrosBaseLegal {
	/*
	 * Esta classe valida a funcionalidade de seleção da quantidade de registros a serem
	 * exibidas no grid. A validação é feita para as opções '10', '50' e '100'
	 */
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
	public void qtdeRegistrosBaseLegal() throws TesteAutomatizadoException, InterruptedException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessar menu
		MenuBaseLegalTemplate.prepararAcessoBaseLegal(driver);

		/*
		 * validar quantidade selecionada --> 10
		 * A instrução implicitlyWait é necessária pois a rapidez da execução dos testes estava gerando uma
		 * falha, sem realmente existir. Dessa forma foi adotado um delay de 1 segundo para resolver esse problema.
		 */
		selecionarQuantidade(driver, "10");
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS); 
		if (contaRegistros(driver) != 10) {
			throw new TesteAutomatizadoException(EnumMensagens.QUANTIDADE_EXCEDIDA, this.getClass());
		}

		
		/*
		 * validar quantidade selecionada --> 50
		 * A instrução implicitlyWait é necessária pois a rapidez da execução dos testes estava gerando uma
		 * falha, sem realmente existir. Dessa forma foi adotado um delay de 1 segundo para resolver esse problema.
		 */
		selecionarQuantidade(driver, "50");
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS); 
		if (contaRegistros(driver) != 50) {
			throw new TesteAutomatizadoException(EnumMensagens.QUANTIDADE_EXCEDIDA, this.getClass());
		}

		/*
		 * validar quantidade selecionada --> 100
		 * A instrução implicitlyWait é necessária pois a rapidez da execução dos testes estava gerando uma
		 * falha, sem realmente existir. Dessa forma foi adotado um delay de 1 segundo para resolver esse problema.
		 */
		selecionarQuantidade(driver, "100");
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS); 
		if (contaRegistros(driver) != 100) {
			throw new TesteAutomatizadoException(EnumMensagens.QUANTIDADE_EXCEDIDA, this.getClass());
		}

		// se o campo estiver vazio o teste é finalizado com sucesso
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

	public void selecionarQuantidade(WebDriver driver, String valor) {
		Select selecionarValor = new Select(
				driver.findElement(By.xpath("//div[@id='baseLegalDataTable_length']/label/select")));
		selecionarValor.selectByVisibleText(valor);
	}

	public int contaRegistros(WebDriver driver) {
		/*
		 * Este método armazena as linhas da tablela (listagem) em uma lista, e retorna a quantidade
		 * de registro na lista, que será igual a quantidade de registros exibidos na tela
		 * O comando cssSelector("tbidy tr") desconsidera a linha de cabeçalho da listagem.
		 */
		List<WebElement> trColecao = driver.findElements(By.cssSelector("tbody tr"));
		return trColecao.size();
	}
}
