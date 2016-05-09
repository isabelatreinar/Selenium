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

public class ExcluirIndicadorSemVinculo {
	/**
	 * Teste exclusão de indicador sem vínculos
	 * Pré-condição: O registro deve constar na base de dados
	 * Dados de teste
	 * Tipo de Indicador: Finalístico
	 * Nome do Indicador: Teste exclusão sem vínculo
	 * Tipo de Fonte: Declaratório
	 * Nome da Fonte: Beneficiário
	 * Polaridade: Quanto maior, melhor
	 * Programa: Programa Teste
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
	public void testeExclusaoSemVinculo() throws TesteAutomatizadoException {

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
		EditarIndicadorSemVinculo.pesquisar(driver, "Finalístico", "Teste exclusão sem vínculo", "Quanto maior, melhor", "Programa Teste");

		// Excluir Registro
		excluirSemVinculo();
		
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

	private void excluirSemVinculo() {
		driver.findElement(By.id("btnExcluir1")).click();
		
		// Verifica a exibição do modal
		if(Validacoes.verificaModalAlerta(driver) == false){
			erros.add(EnumMensagensLog.MODAL_DESABILITADO.getMensagem());
		}
		
		// Verifica a mensagem do modal
		if(Validacoes.verificaMensagemModalAlerta(driver, "Tem certeza que deseja excluir o indicador?") == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Modal");
		}
		
		// Confirma exclusão
		driver.findElement(By.xpath("//*[@class='jconfirm-box']/div[4]/button[1]")).click();
		
		// Verifica a exibição do toast
		if(Validacoes.verificaExibicaoToast(driver) == false){
			erros.add(EnumMensagensLog.TOAST_DESABILITADO.getMensagem());
		}
		// verifica mensagem do toast
		if(Validacoes.verificaMensagemToast(driver, "Indicador excluido com sucesso.") == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Toast");
		}
		
	}
}
