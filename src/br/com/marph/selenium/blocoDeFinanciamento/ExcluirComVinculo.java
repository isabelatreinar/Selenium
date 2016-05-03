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

public class ExcluirComVinculo {
	/**
	 * Teste de Exclusão de Bloco de Financiamento com vínculo com programa
	 * Pré-condicao: Ter o bloco cadastrado na base de dados com vínculo com programa
	 * Dados de Teste
	 * Nome: Bloco de teste exclusao com vinculo
	 * Descrição: Teste de exclusão com vínculo com o programa "Programa Teste"
	 * Status: Ativo
	 * Vínculo: Programa Teste
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
		//driver.quit();
	}
	
	@Test
	public void testeExcluirSemVinculo() throws TesteAutomatizadoException {
		// Recolhendo informacoes de log
		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		
		// Acesso ao sistema
		AcessoSistema.perfilAdministrador(driver);

		// Acesso ao menu
		MenuBlocoTemplate.menuBlocoFinanciamento(driver);
		
		// Inicializa list
		erros = new ArrayList<String>();
		
		// Pesquisa e seleciona registro a ser editado
		EditarBlocoSemVinculo.pesquisar(driver, "Bloco de teste exclusao com vinculo", "Ativo");
		
		// Exclui o registro
		excluirComVinculo();
		
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

	private void excluirComVinculo() {
		//clica no botão excluir.
		driver.findElement(By.id("btnExcluir1")).click();
		
		// Valida a exibição do modal de alerta
		if(Validacoes.verificaModalAlerta(driver) == false){
			erros.add(EnumMensagensLog.MODAL_DESABILITADO.getMensagem());
		}
		// Valida a mensagem exibida para o usuário
		else if(Validacoes.verificaMensagemModalAlerta(driver, "O bloco de financiamento não pode ser excluído pois está vinculado a um ou mais programas.") == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Modal");
		}
		else{
			// Fecha o modal
			driver.findElement(By.xpath("//*[@class='jconfirm-box']/div[4]/button")).click();
			
			// Verifica se o sistema mantém o usuario na tela de visualizar bloco de Financiamento
			if(!driver.findElement(By.id("gridSystemModalLabel")).getText().equals("VISUALIZAR BLOCO DE FINANCIAMENTO")){
				erros.add(EnumMensagensLog.OPERACAO_INVALIDA.getMensagem());
			}
		}
	}
	
}
