package br.com.marph.selenium.municipio;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class ValidarCagecMunicipio {
	
		private final String LOG_NAME = System.getProperty("user.name");
		private WebDriver driver;
		private Logger log = LogManager.getLogger(LOG_NAME);	
		
		@Before
		public void startBrowser(){
			driver = new FirefoxDriver();
			Conexao.ip(driver);  
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
			}	

		@Test
		public void realizaBusca() throws TesteAutomatizadoException{			
			
			LogUtils.log(EnumMensagens.INICIO, this.getClass());
			
			long timestart = System.currentTimeMillis();		
			
			MenuMunicipioTemplate.prepararAcessoMunicipio(driver);
				
			PesquisarMunicipio.pesquisar(driver);
			
			WebElement entrar = driver.findElement(By.xpath("//td[@class='sorting_1']"));
			entrar.click();
			
			WebElement prefeito = driver.findElement(By.id("atualizarCagec"));
			prefeito.click();			
			
				boolean validar = driver.findElement(By.id("toast-container")).isDisplayed();

				if (validar == true) {
					LogUtils.log(EnumMensagens.CAGEC_VALIDADO, this.getClass());
				} else {
					throw new TesteAutomatizadoException(EnumMensagens.CAGEC_NAO_VALIDADO, this.getClass());
				}		
			 		
			
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
}
