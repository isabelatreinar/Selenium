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

public class EditarIndicadorComVinculo {
	/**
	 * Teste de Edição de indicador com vínculo 
	 * 
	 * Pré-condição: Ter vínculo com uma ou mais Resoluções
	 * 
	 * Dados de Teste
	 * Informações do indicador
	 * Tipo de Indicador: Finalístico
	 * Nome: % de resposta às demandas relacionadas aos demais municípios da região dentro do prazo pactuado
	 * Tipo de Fonte: Declaratório
	 * Nome da Fonte: Ministério da Saúde
	 * Polaridade: Quanto maior, melhor
	 * Programa: Ouvidoria Regional
	 * Meses da média Móvel: 
	 * Meses de Defasagem: 
	 * Descrição: O indicador será calculado considerando o número de demandas dos demais municípios respondidas 
	 * dentro do prazo, dividido pelo número total de demandas recebidas pelos demais municípios no período.
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
	public void testeEdicaoIndicadorComVinculo() throws TesteAutomatizadoException {
		// Recolhendo informações de log
		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acesso ao sistema
		AcessoSistema.perfilAdministrador(driver);
		
		// Acesso ao menu
		MenuIndicadorTemplate.menuIndicador(driver);
		
		// Inicializa list
		erros = new ArrayList<>();
		
		// Pesquisa e acessa a tela do registro
		EditarIndicadorSemVinculo.pesquisar(driver, "", "% de resposta às demandas relacionadas aos demais municípios da região dentro do prazo pactuado", "", "Ouvidoria Regional");
		
		// Acessa a tela de edição do indicador
		driver.findElement(By.id("btnEditar1")).click();
		
		// Verifica validacoes		
		verificaValidacoes();
		
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
	
	private void verificaValidacoes(){
		// Verifica a exibição do modal
		if(Validacoes.verificaModalAlerta(driver) == false){
			erros.add(EnumMensagensLog.MODAL_DESABILITADO.getMensagem());
		}
		// Valida a mensagem exibida para usuário
		else if(Validacoes.verificaMensagemModalAlerta(driver, "O indicador não pode ser editado pois está vinculado a um ou mais registros de:\nResolução") == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Modal");
		}
		else{
			// Fecha o modal
			driver.findElement(By.xpath("//*[@class='jconfirm-box']/div[4]/button")).click();
			
			// Verifica se o sistema mantém o usuario na tela de visualizar indicador
			if(!driver.findElement(By.id("gridSystemModalLabel")).getText().equals("VISUALIZAR INDICADOR")){
				erros.add(EnumMensagensLog.OPERACAO_INVALIDA.getMensagem());
			}
		}
	}
}
