package br.com.marph.selenium.indicador;

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

public class ExcluirIndicadorComVinculo {
	/**
	 * Teste exclusão de indicador com vínculos
	 * Pré-condição: O indicador deve ter vínculo com alguma entidade
	 * Dados de teste
	 * Tipo de Indicador: Finalístico
	 * Nome do Indicador: Adesão ao Programa Saúde na Escola
	 * Tipo de Fonte: Oficial
	 * Nome da Fonte: Portaria/MS
	 * Polaridade: Quanto maior, melhor
	 * Programa: SAPS-Saúde na Escola
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
	public void testeExclusaoComVinculo() throws TesteAutomatizadoException {

		// Recolhendo informações de log
		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acessar o sistema
		AcessoSistema.perfilAdministrador(driver);
		
		// Acessar o menu
		MenuIndicadorTemplate.menuIndicador(driver);
		
		// Inicializa List de erro
		erros = new ArrayList<String>();

		// Pesquisar registro
		EditarIndicadorSemVinculo.pesquisar(driver, "Finalístico", "Adesão ao Programa Saúde na Escola", "Quanto maior, melhor", "SAPS-Saúde na Escola");

		// Excluir Registro
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
		driver.findElement(By.id("btnExcluir1")).click();
		
		// Verifica a exibição do modal
		if(Validacoes.verificaModalAlerta(driver) == false){
			erros.add(EnumMensagensLog.MODAL_DESABILITADO.getMensagem());
		}
		
		// Verifica a mensagem do modal
		if(Validacoes.verificaMensagemModalAlerta(driver, "O indicador não pode ser excluído pois está vinculado a um ou mais registros de:\nResolução") == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Modal");
		}
		
		// Confirma exclusão
		driver.findElement(By.xpath("//*[@class='jconfirm-box']/div[4]/button[1]")).click();
	}
}
