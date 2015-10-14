package br.com.marph.selenium.usuario;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;

import br.com.marph.selenium.conexao.Conexao;


public class CadastroUsuarioIE {
	private final String LOG_NAME = System.getProperty("user.name");
	private Logger log = LogManager.getLogger(LOG_NAME);
	
		
		@Test
		public void startBrowser(){
			System.setProperty("webdriver.ie.driver", "C://IEDriverServer.exe");
			WebDriver driver=new InternetExplorerDriver();
			Conexao.ip(driver);  
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);

			log.info("Inicio do teste - cadastrar usuarios IE");
			
			long timestart = System.currentTimeMillis();		
			
			MenuUsuarioTemplate.prepararAcessoBaseLegal(driver);		
			
			WebElement botaoCadastrar = driver.findElement(By.id("btnNovoUsuario"));
			botaoCadastrar.click();
			
			WebElement nome = driver.findElement(By.id("usuarioNome"));
			nome.sendKeys("Ana Maria Oliveira");
			
			WebElement email = driver.findElement(By.id("usuarioEmail"));
			email.sendKeys("ana@gmail.com");
			
			WebElement cpf = driver.findElement(By.id("usuarioCpf"));
			cpf.sendKeys("-48121366283");
			
			WebElement cargo = driver.findElement(By.id("cargo_chosen"));
			cargo.click();
			
			WebElement selecionarCargo = driver.findElement(By.xpath("//li[@data-option-array-index='3']"));
			selecionarCargo.click();
			
			WebElement btnAvancar = driver.findElement(By.id("btnSalvar"));
			btnAvancar.click(); 		 
			
			WebElement perfil = driver.findElement(By.id("modalPerfil_chosen"));
			perfil.click();
			
			WebElement selecionarPerfil = driver.findElement(By.xpath("//li[@data-option-array-index='3']"));
			selecionarPerfil.click();				
			
			WebElement extensao = driver.findElement(By.id("modalExtensaoPerfilId"));
			extensao.sendKeys("uni");
			WebElement extensaoSeleciona = driver.findElement(By.id("ui-id-2"));
			extensaoSeleciona.click();
			
			WebElement salvar = driver.findElement(By.id("btnSalvar"));
			salvar.click();		
			
			//*[@id="modalPerfil_chosen"]/div/div/input
			//FIM
			
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
