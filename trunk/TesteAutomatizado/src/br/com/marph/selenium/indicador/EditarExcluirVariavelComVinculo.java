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

public class EditarExcluirVariavelComVinculo {
	/**
	 * Teste de edição/exclusão de variável utilizada na fórmula do indicador
	 * Pré-condição: - A variável deve estar sendo utilizada na fórmula do indicador
	 * - Ter executado o script de cadastro de indicador
	 * Dados de teste
	 * Informações do indicador
	 * Tipo de Indicador: Finalístico
	 * Nome: Indicador teste CRUD Indicador
	 * Tipo de Fonte: Declaratório
	 * Nome da Fonte: Beneficiário
	 * Polaridade: Quanto maior, melhor
	 * Programa: Programa Teste
	 * Meses da média Móvel: 12
	 * Meses de Defasagem: 13
	 * Descrição: Teste automatizado do cadastro de indicador
	 * 
	 * Informações da variável
	 * Nome: Variável Teste
	 * Descrição: Variável utilizada no teste automatizado.
	 * 
	 * Fórmula do Indicador
	 * Fórmula: VARIAVEL_TESTE_25
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
	public void testeEditarExcluirVariavelComVinculo() throws TesteAutomatizadoException {

		// Recolhendo informações de log
		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acesso ao sistema
		AcessoSistema.perfilAdministrador(driver);
		
		// Acesso ao menu
		MenuIndicadorTemplate.menuIndicador(driver);
		
		// Inicializa List de erros
		erros = new ArrayList<>();

		// Pesquisa e acessa a tela do registro
		EditarIndicadorSemVinculo.pesquisar(driver, "Finalístico", "Indicador teste CRUD Indicador", "Quanto maior, melhor", "Programa Teste");
		
		// Acessa a tela de cadastro de variável
		driver.findElement(By.id("btnVariavelIndicadores")).click();
		
		// Clica em "Editar" para abrir o collapse para edição
		driver.findElement(By.className("_editarVariavel")).click();
		
		// verifica as validações da edição
		verificaValidacoesEdicao();
		
		// Clica em "Excluir" para tentar excluir a variável do indicador
		driver.findElement(By.className("_excluirVariavel")).click();
		
		// verifica as validações da exclusão
		verificaValidacoesExclusao();
				
		// Verifica se não existem erros
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

	private void verificaValidacoesEdicao() {
		// verifica a exibição do toast
		if(Validacoes.verificaExibicaoToast(driver) == false){
			erros.add(EnumMensagensLog.TOAST_DESABILITADO.getMensagem() + " Edição da Variável.");
		}
		// Valida a mensagem exibida para o usuário
		if(Validacoes.verificaMensagemToast(driver, "A variável não pode ser editada pois está vinculada à fórmula do indicador.") == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Toast Edição");
		}		
	}
	
	private void verificaValidacoesExclusao() {
		// verifica a exibição do toast
		if(Validacoes.verificaExibicaoToast(driver) == false){
			erros.add(EnumMensagensLog.TOAST_DESABILITADO.getMensagem() + " Exclusão da Variável.");
		}
		// Valida a mensagem exibida para o usuário
		if(Validacoes.verificaMensagemToast(driver, "A variável não pode ser excluída pois está vinculada à fórmula do indicador.") == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Toast Exclusão");
		}		
	}

}
