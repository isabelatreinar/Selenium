package br.com.marph.selenium.blocoDeFinanciamento;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagensLog;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;
import br.marph.selenium.validacaoUtils.Validacoes;

public class InativarBlocoComVinculo {
	/**
	 * Teste Inativar Bloco de Financiamento com vínculo com Programa
	 * Pré-Condicao: O bloco deve ter vínculo com algum programa
	 * Dados de Teste
	 * Nome: Teste Inativar Bloco com Programa Vinculado
	 * Programa Vinculado: Programa Teste Inativar Bloco com programa vinculado
	 * 
	 */
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	private List<String> erros;
	
	@Before
	public void startBrowser() {
		driver = new FirefoxDriver();
		Conexao.ip(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@After
	public void closeBrowser(){
		driver.quit();
	}
	
	@Test
	public void testeAtivarInativarBloco() throws TesteAutomatizadoException {
		// Recolhendo informacoes de log
		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acesso o sistema
		AcessoSistema.perfilAdministrador(driver);
		
		// Acesso menu
		MenuBlocoTemplate.menuBlocoFinanciamento(driver);
		
		// Inicializar List
		erros = new ArrayList<>();
		
		// Pesquisa e seleciona registro a ser editado
		EditarBlocoSemVinculo.pesquisar(driver, "Teste Inativar Bloco com Programa Vinculado", "Ativo");
		
		// Inativa o bloco e realiza a validação
		inativaBloco();
		
		
		// Verifica se existem erros
		if(erros.size() != 0){
			throw new TesteAutomatizadoException(erros, getClass());
		}
		
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
	
	private void inativaBloco(){
		// Clica no botão "Inativar"
		driver.findElement(By.id("btnInativar")).click();
		
		// Valida a exibição do toast
		if(Validacoes.verificaExibicaoToast(driver) == false){
			erros.add(EnumMensagensLog.TOAST_DESABILITADO.getMensagem());
		}
		// valida a mensagem exibida para o usuário
		else if(Validacoes.verificaMensagemToast(driver, "O bloco de financiamento não pode ser inativado pois está vinculado a um ou mais programas ativos.") == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Toast");
		}
	}	
}
