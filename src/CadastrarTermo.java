
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
import org.openqa.selenium.support.ui.Select;
  
public class CadastrarTermo{
	
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
	public void preencheformulario(){
		String texto="Sua resposta foi registrada.";
	driver.get("http://tinyurl.com/twseleniumworkshop");
	
	WebElement nome = driver.findElement(By.id("entry_1050252143"));
	nome.sendKeys("Isabela de Oliveira");
	
	Select linguagem = new Select(driver.findElement(By.id("entry_2043435478")));
	linguagem.selectByValue("Java");
	
	WebElement radio = driver.findElement(By.id("group_1094861216_2"));
	radio.click();
	
	WebElement checkbox = driver.findElement(By.id("group_1880671481_2"));
	checkbox.click();
	
	WebElement botaoEnviar = driver.findElement(By.id("ss-submit"));
	botaoEnviar.click();
	
	WebElement resposta = driver.findElement(By.className("ss-resp-message"));
	assertThat(resposta.getText(),equalTo(texto));
	
	}
	}
