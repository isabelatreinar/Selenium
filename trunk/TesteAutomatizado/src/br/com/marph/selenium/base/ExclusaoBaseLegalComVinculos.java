package br.com.marph.selenium.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;

public class ExclusaoBaseLegalComVinculos {
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	private List<String> erros = new ArrayList<>();

	
	@Before
	public void startBrowser(){
		driver = new FirefoxDriver();
		Conexao.ip(driver);  
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		}
		
	@Test
	public void testeExclusao() throws Exception {
		
		/** 
		 * Teste de Exclusão da Base Legal com vínculos
		 * A Base Legal utilizada no teste possui vínculo com resolução
		 * Dados de Teste
		 * Tipo de Base: Resolução
		 * Número: 159
		 */

		// Recolhe informações do log
		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessa o sistema
		AcessoSistema.perfilAdministrador(driver);
		
		// Acessa o menu
		MenuBaseLegalTemplate.menuBaseLegal(driver);
		
		// Exclusão de Base Legal com vínculos
		exclusaoComVinculos();
		
		// Fechar o browser
		driver.quit();
		
		// Recolhendo informações do teste
		float tempoGasto = (System.currentTimeMillis() - timestart);
		float tempoSegundos = tempoGasto / 1000;

		StringBuilder sb = new StringBuilder();
		sb.append("Entrada no sistema - ").append(tempoSegundos).append(" segundos - FINALIZADO COM SUCESSO\n");

		if (tempoSegundos > 5000) {
			log.warn(sb.toString() + "\n");
		} else {
			log.info(sb.toString() + "\n");
		}
	}
	
	public void exclusaoComVinculos() throws TesteAutomatizadoException{
		/**
		 * Método para teste de exclusão de base legal com vínculos
		 */
		// Pesquisa Base Legal (Base Legal vinculada a uma ou mais resoluções)
		EditarBaseLegal.pesquisarBaseLegal(driver, "Resolução", "159");

		// Click no botão "Excluir"
		driver.findElement(By.id("btnExcluir1")).click();
		
		// Modal de Confirmação de Exclusão
		// Verifica se o sistema exibiu o modal de confirmação (método getWindowHandle())
		if(driver.getWindowHandle().isEmpty()){
			erros.add(EnumMensagens.CONFIRMACAO_DESABILITADA.getMensagem());
		}
		WebElement divExterna = driver.findElement(By.className("jconfirm-box"));
		
		// Verifica mensagem de alerta
		if(!divExterna.findElement(By.className("content")).getText().equals("A base legal não pode ser excluída pois está vinculada a uma ou mais resoluções.")){
			erros.add(EnumMensagens.MENSAGEM_INCORRETA.getMensagem());
		}
		
		// Confirma mensagem de alerta
		WebElement divBotoes = divExterna.findElement(By.className("buttons"));
		divBotoes.findElement(By.xpath("button[1]")).click();
		
		//Thread.currentThread().getStackTrace()[1].getMethodName() -> Retorna o nome do Método em Execução
		if(erros.size() != 0)
			throw new TesteAutomatizadoException(erros, Thread.currentThread().getStackTrace()[1].getMethodName());
	}
}
