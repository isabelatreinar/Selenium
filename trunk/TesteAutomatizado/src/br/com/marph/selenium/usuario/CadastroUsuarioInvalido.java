package br.com.marph.selenium.usuario;

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

public class CadastroUsuarioInvalido {

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
		
		log.info("Inicio do teste - Cadastro usuarios invalidos");
		
		long timestart = System.currentTimeMillis();		
		
		MenuUsuarioTemplate.prepararAcessoBaseLegal(driver);		
		
		
		WebElement botaoCadastrar = driver.findElement(By.id("btnNovoUsuario"));
		botaoCadastrar.click();	
		
		WebElement nome = driver.findElement(By.id("usuarioNome"));
		nome.sendKeys("TESTE");
		
		WebElement cpf = driver.findElement(By.id("usuarioCpf"));
		cpf.sendKeys("-78899331189");
		
		WebElement cargo = driver.findElement(By.id("cargo_chosen"));
		cargo.click();
		
		WebElement selecionarCargo = driver.findElement(By.xpath("//li[@data-option-array-index='3']"));
		selecionarCargo.click();
		
		WebElement btnAvancar = driver.findElement(By.id("btnSalvar"));
		btnAvancar.click(); 		 
			
			//VALIDAÇÂO
			if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='usuarioNome_label']/label/span")).getText())){			
				log.info("Campo de nome estava em branco - Obrigatório");
			}
			
			if("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='usuarioCpf_label']/label/span")).getText())){				
				log.info("Campo CPF estava em branco - Obrigatório"); 
			}
			
			if("CPF inválido!".equals(driver.findElement(By.xpath("//*[@id='usuarioCpf_label']/label/span")).getText())){
				log.info("CPF inválido");
			}
			
			if("CPF já cadastrado.".equals(driver.findElement(By.xpath("//*[@id='usuarioCpf_label']/label/span")).getText())){
				log.info("CPF já cadastrado");
			}
			
			if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='cargo_label']/label/span")).getText())){			
				log.info("Campo de cargo estava em branco - Obrigatório");
			}
			
			//FIM VALIDAÇÃO	
			WebElement btnAvancar1 = driver.findElement(By.id("btnSalvar"));
			btnAvancar1.click();
			
			//VALIDAÇÂO
			if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='modalPerfil_label']/label/span")).getText())){			
				WebElement perfil = driver.findElement(By.id("modalPerfil_chosen"));
				perfil.click();				
				
				WebElement selecionarPerfil = driver.findElement(By.xpath("//li[@data-option-array-index='3']"));
				selecionarPerfil.click();				
			}
			//FIM
			
			WebElement extensao = driver.findElement(By.id("modalExtensaoPerfilId"));
			extensao.sendKeys("uni");
			WebElement extensaoSeleciona = driver.findElement(By.id("ui-id-2"));
			extensaoSeleciona.click();
			
			WebElement salvar = driver.findElement(By.id("btnSalvar"));
			salvar.click();
			
			WebElement voltar = driver.findElement(By.id("btnVoltar"));
			voltar.click();

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
