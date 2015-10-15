package br.com.marph.selenium.municipio;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;

import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.utils.LogUtils;

public class ExportarMunicipio {
	private final String LOG_NAME = System.getProperty("user.name");
	private Logger log = LogManager.getLogger(LOG_NAME);
	
	
	@Test
	public void startBrowser(){
		System.setProperty("webdriver.ie.driver", "C://IEDriverServer.exe");
		WebDriver driver=new InternetExplorerDriver();
		Conexao.ip(driver);  
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		
		MenuMunicipioTemplate.prepararAcessoMunicipio(driver);
		
		long timestart = System.currentTimeMillis();
		
		WebElement btnPerfil = driver.findElement(By.xpath("//button[@data-id-datatable='municipioDataTable']"));
		btnPerfil.click();		
		
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
