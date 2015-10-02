package br.com.marph.selenium.resolucao;

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

public class CadastrarBeneficiarios {
	private final String LOG_NAME = "RAFAEL";
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
	public void realizaBusca(){			
		
		log.info("Inicio do teste - Cadastrar resolução");
		
		long timestart = System.currentTimeMillis();		
		
		MenuResolucaoTemplate.prepararAcessoBaseLegal(driver);
		
	/*	WebElement numero = driver.findElement(By.id("numero"));
		numero.sendKeys("456");
		
		WebElement programa = driver.findElement(By.id("programa_chosen"));
		programa.click();
		
		WebElement programaSeleciona = driver.findElement(By.xpath("//li[@data-option-array-index='3']"));
		programaSeleciona.click();
		
		WebElement perquisar = driver.findElement(By.id("btnPesquisar"));
		perquisar.click();*/
		
		String idUsuario = "rowId394";
		
		WebElement selecionarResolucao = driver.findElement(By.id(idUsuario));
		selecionarResolucao.click();
		
		WebElement resolucao = driver.findElement(By.xpath("//*[@id='"+idUsuario+"']/td[2]"));
		resolucao.click();
		
		if("1.Resolução".equals(driver.findElement(By.xpath("//li[@class='current']")).getText())){
			WebElement avancar = driver.findElement(By.id("btnProximoBottom"));
			avancar.click();
		}
		
		if("2.Beneficiários Contemplados".equals(driver.findElement(By.xpath("//li[@class='current']")).getText())){
			WebElement carregar = driver.findElement(By.id("uploadBeneficiariosContemplados-txt"));
			carregar.sendKeys("C:\\Users\\rafael.sad\\Downloads\\beneficiarioExport (3).xls");
		}
		
		float tempoGasto = (System.currentTimeMillis() - timestart );
		float tempoSegundos = tempoGasto/1000;
		
		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos");
	
		if(tempoSegundos>5000){
			log.warn(sb.toString()+"\n");
		}else{
			log.info(sb.toString()+"\n");
		}		
	}
}


/*if(driver.findElement(By.id("btnSalvar1")).isDisplayed() == true){
			WebElement avancar = driver.findElement(By.id("btnProximoBottomLabel"));
			avancar.click();
		}
		else {
			WebElement upload = driver.findElement(By.id("uploadBeneficiariosContemplados"));
			upload.click();
		}*/
