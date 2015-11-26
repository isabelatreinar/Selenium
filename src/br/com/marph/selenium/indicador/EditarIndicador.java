package br.com.marph.selenium.indicador;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class EditarIndicador {
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
	public void realizaBusca() throws InterruptedException, TesteAutomatizadoException {

		LogUtils.log(EnumMensagens.INICIO, this.getClass());

		long timestart = System.currentTimeMillis();

		MenuIndicadorTemplate.prepararAcessoIndicador(driver);
		
		PesquisarIndicador.pesquisar(driver);
		
		VisualizarIndicador.visualizar(driver);

		editar();

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

	public void editar() {
		// acessa botao de editar
		driver.findElement(By.id("btnEditar1")).click();

		// tipo de indicador
		driver.findElement(By.id("tipoIndicador_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoIndicador_chosen']/div/div/input")).sendKeys("Finalístico");
		driver.findElement(By.xpath("//*[@id='tipoIndicador_chosen']/div/div/input")).sendKeys(Keys.TAB);

		// tipo de fonte
		driver.findElement(By.id("tipoFonte_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoFonte_chosen']/div/div/input")).sendKeys("Oficial");
		driver.findElement(By.xpath("//*[@id='tipoFonte_chosen']/div/div/input")).sendKeys(Keys.TAB);

		// polaridade
		driver.findElement(By.id("polaridade_chosen")).click();
		driver.findElement(By.xpath("//*[@id='polaridade_chosen']/div/div/input")).sendKeys("Quanto maior, melhor");
		driver.findElement(By.xpath("//*[@id='polaridade_chosen']/div/div/input")).sendKeys(Keys.TAB);

		// media movel
		driver.findElement(By.id("mesesDaMediaMovel")).clear();
		driver.findElement(By.id("mesesDaMediaMovel")).sendKeys("4");

		// meses de defasagem
		driver.findElement(By.id("mesesDeDefasagem")).clear();
		driver.findElement(By.id("mesesDeDefasagem")).sendKeys("0");

		// nome do indicador
		driver.findElement(By.id("nomeIndicador")).clear();
		driver.findElement(By.id("nomeIndicador")).sendKeys("Excluir1");

		// nome da fonte
		driver.findElement(By.id("nomeFonte")).clear();
		driver.findElement(By.id("nomeFonte")).sendKeys("SIGAF");

		// programa
		driver.findElement(By.id("programa_chosen")).click();
		driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input")).sendKeys("Farmácia de minas");
		driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input")).sendKeys(Keys.TAB);

		// descrição
		driver.findElement(By.id("descricao")).clear();
		driver.findElement(By.id("descricao"))
				.sendKeys("Este indicador expressa o percentual de itens da relação"
						+ " de plantas medicinais, fitoterápicos e homeopáticos, "
						+ "dispensados na unidade do componente verde da Rede Farmácia de Minas. "
						+ "O Indicador será obtido através do SIGAF");
	}
}
