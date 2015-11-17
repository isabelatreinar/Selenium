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

public class CadastrarIndicador {
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
		
		cadastro();
		
		variavel();
		
		formula();
		
		float tempoGasto = (System.currentTimeMillis() - timestart);
		float tempoSegundos = tempoGasto / 1000;

		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos");

		if (tempoSegundos > 5000) {
			log.warn(sb.toString() + "\n");
		} else {
			log.info(sb.toString() + "\n");
		}
	}

	public void cadastro() {
		driver.findElement(By.id("btnNovoIndicador")).click();
		
		//tipo de indicador
		driver.findElement(By.id("tipoIndicador_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoIndicador_chosen']/div/div/input")).sendKeys("Finalístico");
		driver.findElement(By.xpath("//*[@id='tipoIndicador_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		//tipo de fonte
		driver.findElement(By.id("tipoFonte_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoFonte_chosen']/div/div/input")).sendKeys("Oficial");
		driver.findElement(By.xpath("//*[@id='tipoFonte_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		//polaridade
		driver.findElement(By.id("polaridade_chosen")).click();
		driver.findElement(By.xpath("//*[@id='polaridade_chosen']/div/div/input")).sendKeys("Quanto maior, melhor");
		driver.findElement(By.xpath("//*[@id='polaridade_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		//media movel
		driver.findElement(By.id("mesesDaMediaMovel")).sendKeys("12");		
		
		//meses de defasagem
		driver.findElement(By.id("mesesDeDefasagem")).sendKeys("21");
		
		//nome do indicador
		driver.findElement(By.id("nomeIndicador")).sendKeys("Testinhu");
		
		//nome da fonte
		driver.findElement(By.id("nomeFonte")).sendKeys("marph");
		
		//programa
		driver.findElement(By.id("programa_chosen")).click();
		driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input")).sendKeys("Farmácia de minas");
		driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		//descrição
		driver.findElement(By.id("descricao")).sendKeys("TESTEEE");
		
		//avançar
		driver.findElement(By.id("btnSalvar")).click();
	}
	
	public void variavel() {
		//clica no botão criar
		driver.findElement(By.id("criar")).click();
		
		//clica no botão criar
		driver.findElement(By.xpath("//*[@class='panel-heading']/span/input")).sendKeys("testa");
		
		//Preenche o campo descrição
		driver.findElement(By.xpath("//*[@class='panel-collapse collapse in']/div/div[2]/div/p/textarea")).sendKeys("huahauhau");
		
		//clica em salvar
		driver.findElement(By.xpath("//*[@class='panel-heading']/ul/li[1]/a")).click();
		
		//avança
		driver.findElement(By.id("btnSalvar")).click();
		
	}
	
	public void formula(){
		//clica para adicionar
		driver.findElement(By.xpath("//*[@id='heading0']/ul/li/a")).click();
		
		//clica para salvar
		driver.findElement(By.id("btnSalvar")).click();
		
	}
}
