
import java.util.concurrent.TimeUnit;

import org.junit.After;  
import org.junit.Before;  
import org.junit.Test;  
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
  
public class pesquisa_google2{
	
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
