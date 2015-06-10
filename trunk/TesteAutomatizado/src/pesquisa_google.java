
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
  
public class pesquisa_google{
	
	WebDriver driver;
	
	@Before
	public void setUp(){
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		
	}
	
	@After
	public void tearDown(){
		driver.quit();
	}
	
	@Test
	public void realizaBusca(){
		driver.get("http://google.com");
		WebElement campoBusca = driver.findElement(By.id("gs_htif0"));
		campoBusca.sendKeys("Pão de Queijo Mineiro");
		WebElement botaoBuscar = driver.findElement(By.name("btnK"));
		botaoBuscar.click();
		WebElement resposta = driver.findElement(By.linkText("Pão de Queijo Mineiro"));
		assertThat(resposta.getText(),equalTo("Pão de Queijo Mineiro"));
		
	}
}