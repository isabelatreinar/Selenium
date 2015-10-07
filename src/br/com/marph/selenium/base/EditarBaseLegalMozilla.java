package br.com.marph.selenium.base;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.utils.LogUtils;

public class EditarBaseLegalMozilla {
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
	public void realizaCadastro(){			
		
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		
		long timestart = System.currentTimeMillis();		
		
		MenuBaseLegalTemplate.prepararAcessoBaseLegal(driver);
		
		
		edicaoCampos();

		
		validacao();
		
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

	private void validacao() {
		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='tipoBaseLegal_label']/label/span")).getText())){			
			log.info("Campo tipo estava em branco - Obrigatório");
		}
		
		if ("Obrigatório!".equals(driver.findElement(By.xpath("//*[@id='numero_label']/label/span")).getText())){			
			log.info("Campo numero estava em branco - Obrigatório");
		}
		
		if ("Existe uma Deliberação cadastrada com este número.".equals(driver.findElement(By.xpath("//*[@id='numero_label']/label/span")).getText())){			
			log.info("Existe uma Deliberação cadastrada com este número.");
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
		
		if ("Tamanho de arquivo não suportado. Selecione um arquivo com até 5 MB.".equals(driver.findElement(By.xpath("//*[@id='textoPublicado_label']/label/span")).getText())){			
			log.info("PDF maior que 5mb.");
		}
	}

	private void edicaoCampos() {
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
	
		driver.findElement(By.id("textoPublicado")).sendKeys("C:\\Users\\rafael.sad\\Downloads\\TESTEEE2.pdf");
		
		WebElement salvar = driver.findElement(By.id("btnSalvar"));
		salvar.click();
	}
}
