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
import br.com.marph.selenium.enums.EnumMensagensSistema;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.javaScriptUtils.JavaScript;
import br.com.marph.selenium.utils.LogUtils;
import br.marph.selenium.validacaoUtils.Validacoes;

public class CadastroIndicadorValidacoes {
	/** 
	 * Teste de Cadastro de Indicador (Validações)
	 * Dados de Teste
	 * Informações do indicador
	 * Tipo de Indicador: Finalístico
	 * Nome: Indicador teste CRUD Indicador
	 * Programa: Programa Teste
	 * 
	 * Validações Realizadas
	 * Indicador Duplicado (para o mesmo programa)
	 * Pré-condição: Ter executado o script de cadastro de indicador
	 * 
	 * Campo Obrigatório
	 * 
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
		//driver.quit();
	}
	
	@Test
	public void testeCadastroIndicador() throws TesteAutomatizadoException {

		// Recolhendo informações de log
		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();

		// Acesso ao sistema
		AcessoSistema.perfilAdministrador(driver);
		
		// Acesso ao menu
		MenuIndicadorTemplate.menuIndicador(driver);
		
		// Inicializa List de erros
		erros = new ArrayList<>();

		// Acessar o formulário de cadastro de indicador
		driver.findElement(By.id("btnNovoIndicador")).click();
		
		// Clica em "Avançar" para marcar os campos obrigatórios
		driver.findElement(By.id("btnSalvar1")).click();
		
		// Realiza as verificações dos campos obrigatórios
		verificaCamposObrigatorios();
		
		// Preencher as informações da primeira etapa do cadastro
		CadastrarIndicador.cadastroIndicador(driver, "", "Indicador teste CRUD Indicador", "", "", "", "Programa Teste", "", "", "");
		
		// Realizar verificação das validações de cadastro duplicado
		verificaCadastroDuplicado();
		
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

	private void verificaCamposObrigatorios() {
		// Verifica a exibição do toast
		if(Validacoes.verificaExibicaoToast(driver) == false){
			erros.add(EnumMensagensLog.TOAST_DESABILITADO.getMensagem() + " Validação Campo Obrigatório");
		}
		// Valida a mensagem exibida para o usuário
		else if(Validacoes.verificaMensagemToast(driver, EnumMensagensSistema.ERRO.getMensagem()) == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Toast Campo Obrigatório");
		}
		
		// Verifica as validações do campo "Tipo de indicador"
		if(Validacoes.verificaMarcacaoErroId(driver, "tipoIndicador_maindiv") == false)
			erros.add(EnumMensagensLog.CAMPO_OBRIGATORIO.getMensagem() + "Tipo de indicador");
		else{
			JavaScript.getTooltipClear(driver, "tipoIndicador_chosen");
			if(Validacoes.verificaMensagemTooltip(driver, EnumMensagensSistema.PREENCHIMENTO_OBRIGATORIO.getMensagem()) == false)
				erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Tipo de indicador");
		}
		
		// Verifica as validações do campo "Nome do indicador"
		if(Validacoes.verificaMarcacaoErroId(driver, "nomeIndicador_maindiv") == false)
			erros.add(EnumMensagensLog.CAMPO_OBRIGATORIO.getMensagem() + "Nome do indicador");
		else{
			JavaScript.getTooltipClear(driver, "nomeIndicador");
			if(Validacoes.verificaMensagemTooltip(driver, EnumMensagensSistema.PREENCHIMENTO_OBRIGATORIO.getMensagem()) == false)
				erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Nome do indicador");
		}
		
		// Verifica as validações do campo "Tipo de Fonte"
		if(Validacoes.verificaMarcacaoErroId(driver, "tipoFonte_maindiv") == false)
			erros.add(EnumMensagensLog.CAMPO_OBRIGATORIO.getMensagem() + "Tipo de Fonte");
		else{
			JavaScript.getTooltipClear(driver, "tipoFonte_chosen");
			if(Validacoes.verificaMensagemTooltip(driver, EnumMensagensSistema.PREENCHIMENTO_OBRIGATORIO.getMensagem()) == false)
				erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Tipo de Fonte");
		}
		
		// Verifica as validações do campo "Nome da Fonte"
		if(Validacoes.verificaMarcacaoErroId(driver, "nomeFonte_maindiv") == false)
			erros.add(EnumMensagensLog.CAMPO_OBRIGATORIO.getMensagem() + "Nome da Fonte");
		else{
			JavaScript.getTooltipClear(driver, "nomeFonte");
			if(Validacoes.verificaMensagemTooltip(driver, EnumMensagensSistema.PREENCHIMENTO_OBRIGATORIO.getMensagem()) == false)
				erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Nome da Fonte");
		}
	}

	private void verificaCadastroDuplicado() {
		// Verifica se o campo possui validação de registro duplicado
		if(Validacoes.verificaMarcacaoErroId(driver, "nomeIndicador_maindiv") == false){
			erros.add(EnumMensagensLog.REGISTRO_DUPLICADO.getMensagem() + "Indicador");
		}
		// Verifica a mensagem do tooltip
		else{
			JavaScript.getTooltip(driver, "nomeIndicador");
			if(Validacoes.verificaMensagemTooltip(driver, "Indicador já cadastrado.") == false){
				erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Tooltip - Nome do Indicador");
			}
		}
	}
}
