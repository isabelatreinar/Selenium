package br.com.marph.selenium.usuario;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
  
public class CadastrarUsuarioMozilla{

	private final String LOG_NAME = "ISABELA";
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	
	
	@Before
	public void startBrowser(){
		driver = new FirefoxDriver();
		driver.get("http://172.16.10.115:8081");  
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		}
	
/*	@After
	public void tearDown(){
		driver.quit();
		}*/
	
	@Test
	public void realizaBusca(){	
		
		
		log.info("Inicio do teste");
		
		long timestart = System.currentTimeMillis();		
		
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
		
		WebElement menuUsuario = driver.findElement(By.xpath("//*[@id='usuariosMenu']"));
		menuUsuario.click();		
		
	  WebElement botaoCadastrar = driver.findElement(By.id("btnNovoUsuario"));
		botaoCadastrar.click();
		
		WebElement nome = driver.findElement(By.id("usuarioNome"));
		nome.sendKeys("Ana Maria Oliveira");
		
		WebElement email = driver.findElement(By.id("usuarioEmail"));
		email.sendKeys("ana@gmail.com");
		
		WebElement cpf = driver.findElement(By.id("usuarioCpf"));
		cpf.sendKeys("788.993.311-89");
		
		WebElement telefone = driver.findElement(By.id("usuarioCpf"));
		telefone.sendKeys("788.993.311-89");	
		
		WebElement btnAvancar = driver.findElement(By.id("btnSalvar"));
		btnAvancar.click(); 		 
		
		WebElement perfil = driver.findElement(By.id("modalPerfil_chosen"));
		perfil.click();
		
		//TESTAR A BAIXO
		WebElement selecionarPerfil = driver.findElement(By.xpath("//li[@data-option-array-index='3']"));
		selecionarPerfil.click();				
		
		WebElement extensao = driver.findElement(By.id("modalExtensaoPerfilId"));
		extensao.sendKeys("uni");
		WebElement extensaoSeleciona = driver.findElement(By.id("ui-id-1"));
		extensaoSeleciona.click();
		
		WebElement salvar = driver.findElement(By.id("btnSalvar"));
		salvar.click();

		
		
		//*[@id="modalPerfil_chosen"]/div/div/input
		//FIM
		float tempoGasto = (System.currentTimeMillis() - timestart);
		
		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoGasto).append(" segundos");
		
/*		sb.append("Entrada no sistema - ");
		sb.append(tempoGasto);
		sb.append("segundos");*/
		
		if(tempoGasto>5000){
			log.warn(sb.toString());
		}else{
			log.info(sb.toString());
		}

		/*teste*/
		
		
	}
}

