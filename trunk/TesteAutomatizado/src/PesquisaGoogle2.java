
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
  
public class PesquisaGoogle2{

	private WebDriver driver;
	private Logger log = null;
	
	@Before
	public void setUp(){
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		PropertyConfigurator.configure("log4j.properties");
		log = Logger.getLogger(PesquisaGoogle2.class);
	}
	
	@After
	public void tearDown(){
		driver.quit();
		}
	
	@Test
	public void realizaBusca(){
		log.debug("teste de mensagem");
		
		long timestart = System.currentTimeMillis();
		
		driver.get("http://172.16.10.115:8080/public/index");
		
		driver.findElement(By.id("closeModalHome")).click();
		driver.findElement(By.cssSelector("input[type=\"button\"]")).click();
		WebElement botaoBuscar = driver.findElement(By.id("btnAcessar"));
		botaoBuscar.click();
		
		WebElement botaoAcessar = driver.findElement(By.name("formSubmit"));
		botaoAcessar.click();
		System.out.println("Entrada no sistema - "+ (System.currentTimeMillis() - timestart) + "segundos");
		
		}
}
