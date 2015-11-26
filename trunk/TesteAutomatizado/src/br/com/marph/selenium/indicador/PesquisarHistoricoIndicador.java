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

public class PesquisarHistoricoIndicador {
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
		
		VisualizarHistoricoIndicador.historico(driver);
		
		pesquisar();
		
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
	
	protected void pesquisar(){
		//expandir pesquisa
		driver.findElement(By.id("btnExpandirPesquisaAvancada")).click();
		
		//intervalo de alteração
		driver.findElement(By.id("dataInicialHistorico")).sendKeys("-11112015");
		
		//data final
		driver.findElement(By.id("dataFinalHistorico")).sendKeys("-13112015");
		
		//Campo alterado
		driver.findElement(By.xpath("//div[@id='camposIndicadores_chosen']/ul/li/input")).click();
		driver.findElement(By.xpath("//div[@id='camposIndicadores_chosen']/ul/li/input")).sendKeys("Nome do Indicador");
		driver.findElement(By.xpath("//div[@id='camposIndicadores_chosen']/ul/li/input")).sendKeys(Keys.ENTER);
		
		//modificado por
		driver.findElement(By.id("usuariosAlteracao_chosen")).click();
		driver.findElement(By.xpath("//div[@id='usuariosAlteracao_chosen']/div/div/input")).sendKeys("Usuário MARPH");
		driver.findElement(By.xpath("//div[@id='usuariosAlteracao_chosen']/div/div/input")).sendKeys(Keys.ENTER);
		
		//pesquisar
		driver.findElement(By.id("btnPesquisar")).click();
	}
}
