package br.com.marph.selenium.base;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class EditarBaseLegalMozilla {
	private final String LOG_NAME = "RAFAEL";
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);	
	
	@Before
	public void startBrowser(){
		driver = new FirefoxDriver();
		driver.get("http://172.16.10.115:8081");  
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		}	

	@Test
	public void realizaCadastro(){			
		
		log.info("Inicio do teste - editar base legal");
		
		long timestart = System.currentTimeMillis();		
		
		//ENTRADA
		WebElement fecharbtn = driver.findElement(By.id("closeModalHome"));
		fecharbtn.click();
		
		WebElement btnEntrar = driver.findElement(By.id("btnEntradaSistemaID"));
		btnEntrar.click();
		
		WebElement btnAcessar = driver.findElement(By.id("btnAcessar"));
		btnAcessar.click();
		
		WebElement btnConfirmar = driver.findElement(By.id("confirmarDados"));
		btnConfirmar.click();
		
		WebElement btnAcessarSist = driver.findElement(By.id("acessarSistema"));
		btnAcessarSist.click();			
		
		WebElement menuCadastrar = driver.findElement(By.xpath("//td[@onmouseup='cmItemMouseUp (this,2)']"));
		menuCadastrar.click(); 
		
		WebElement menuUsuario = driver.findElement(By.xpath("//*[@id='baseLegalMenu']"));
		menuUsuario.click();
		
		//EDIÇÂO
		String idBase = "rowId269";
		
		WebElement selecionarBase = driver.findElement(By.id(idBase));
		selecionarBase.click();
		
		WebElement Base = driver.findElement(By.xpath("//*[@id='"+idBase+"']/td[2]"));
		Base.click();		
		
		WebElement btnEditar = driver.findElement(By.id("btnEditar1"));
		btnEditar.click();	
		
		WebElement TipoBase = driver.findElement(By.id("tipoBaseLegal_chosen"));
		TipoBase.click();
		WebElement procuraTipoBase = driver.findElement(By.xpath("//li[@data-option-array-index='2']"));
		procuraTipoBase.click();
		
		WebElement numero = driver.findElement(By.id("numero"));
		numero.sendKeys("654458");
		
		WebElement data = driver.findElement(By.id("dataPublicacao"));
		data.click();
		WebElement dataSeleciona = driver.findElement(By.xpath("//td[@class='day']"));
		dataSeleciona.click();
		
		WebElement anoVigencia = driver.findElement(By.id("dataVigencia_chosen"));
		anoVigencia.click();
		
		WebElement anoVigenciaSeleciona = driver.findElement(By.xpath("//*[@id='dataVigencia_chosen']/div/ul/li[3]"));
		anoVigenciaSeleciona.click();
	
		//driver.findElement(By.id("textoPublicado")).sendKeys("C:\\Users\\rafael.sad\\Downloads\\TESTEEE2.pdf");
		
		WebElement salvar = driver.findElement(By.id("btnSalvar"));
		salvar.click();
		
		//FIM EDIÇÂO
		
		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='tipoBaseLegal_label']/label/span")).getText())){			
			log.info("Campo tipo estava em branco - Obrigatório");
		}
		
		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='numero_label']/label/span")).getText())){			
			log.info("Campo numero estava em branco - Obrigatório");
		}
		
		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='dataPublicacao_label']/label/span")).getText())){			
			log.info("Campo data estava em branco - Obrigatório");
		}
		
		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='dataVigencia_label']/label/span")).getText())){			
			log.info("Campo data estava em branco - Obrigatório");
		}

		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='textoPublicado_label']/label/span")).getText())){			
			log.info("Campo PDF estava em branco - Obrigatório");
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
