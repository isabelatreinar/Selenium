package br.com.marph.selenium.municipio;

import java.util.concurrent.TimeUnit;

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
import br.com.marph.selenium.utils.LogUtils;

public class PesquisarHistoricoMunicipio {
	private final String LOG_NAME = System.getProperty("user.name");
	private static WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);	
	
	@Before
	public void startBrowser(){
		driver = new FirefoxDriver();
		Conexao.ip(driver);  
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		}	

	@Test
	public void pesquisarHistoricoMunicipio() throws TesteAutomatizadoException{			
		
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();		
		
		// Acessar menu Cadastro > Município
		MenuMunicipioTemplate.prepararAcessoMunicipio(driver);
		
		// Pesquisar um município
		PesquisarMunicipio.pesquisar(driver);
		
		// Acessar o município pesquisado
		VisualizarMunicipio.visualizar(driver);
		
		// Acessar a tela de histórico
		VisualizarHistoricoMunicipio.visualizar(driver);
		
		// Pesquisar
		pesquisar(driver);
		
		float tempoGasto = (System.currentTimeMillis() - timestart );
		float tempoSegundos = tempoGasto/1000;
		
		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos - FINALIZADO COM SUCESSO\n");
	
		if(tempoSegundos>5000){
			log.warn(sb.toString()+"\n");
		}else{
			log.info(sb.toString()+"\n");
		}		
	}
	
	public static void pesquisar(WebDriver driver) throws TesteAutomatizadoException{
		WebElement exibirPesquisa = driver.findElement(By.id("btnExpandirPesquisaAvancada"));
		exibirPesquisa.click();
		
		/*
		 * O sinal de menos é colocado antes da data para a máscara do campo seja considerada.
		 */
		//data inicial
		driver.findElement(By.id("dataInicialHistorico")).sendKeys("-14012015");
		driver.findElement(By.id("dataInicialHistorico")).sendKeys(Keys.TAB);
		
		//data final
		driver.findElement(By.id("dataFinalHistorico")).sendKeys("-14012015");
		driver.findElement(By.id("dataFinalHistorico")).sendKeys(Keys.TAB);
		
		//municipio
		driver.findElement(By.xpath("//div[@id='camposMunicipio_chosen']/ul/li/input")).click();
		driver.findElement(By.xpath("//div[@id='camposMunicipio_chosen']/ul/li/input")).sendKeys("Nome Prefeito");
		driver.findElement(By.xpath("//div[@id='camposMunicipio_chosen']/ul/li/input")).sendKeys(Keys.ENTER);

		/* 1º caso: se possui a mensagem "Resultado não encontrado" -> não preenche o campo 'Modificado por'
		 * 2º caso: se não possui a mensagem preenche o campo 'Modificado por'
		 * 3º caso: verifica se não possui a mensagem e não possui usuário -> erro na exibição
		 */
		// verifica se possui a mensagem "Resultado não encontrado"
		if(!driver.findElement(By.xpath("/html/body/div[2]/div[5]/div[2]")).getText().contains("Resultado não encontrado.")){
		
			// verifica se possui usuários, se não possui a mensagem nem usuários -> erro
			if(driver.findElements(By.cssSelector(".chosen-results li")).size() == 0){
				throw new TesteAutomatizadoException(EnumMensagens.ERRO_HISTORICO, PesquisarHistoricoMunicipio.class);
			}
			
			driver.findElement(By.id("usuariosAlteracao_chosen")).click();
			//modificado por
			driver.findElement(By.xpath("//div[@id='usuariosAlteracao_chosen']/div/div/input")).click();
			driver.findElement(By.xpath("//div[@id='usuariosAlteracao_chosen']/div/div/input")).sendKeys("Usuário Marph");
			driver.findElement(By.xpath("//div[@id='usuariosAlteracao_chosen']/div/div/input")).sendKeys(Keys.ENTER);
		}
		
		//pesquisar
		driver.findElement(By.id("btnPesquisar")).click();
	}
}
