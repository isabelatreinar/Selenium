package br.com.marph.selenium.beneficiario;

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
import br.com.marph.selenium.javaScriptUtils.JavaScript;
import br.com.marph.selenium.utils.LogUtils;
import br.marph.selenium.validacaoUtils.Validacoes;

public class CadastroBeneficiarioCampoObrigatorio {
	private final String LOG_NAME = System.getProperty("user.name");
	private WebDriver driver;
	private Logger log = LogManager.getLogger(LOG_NAME);
	private List<String> erros;

	@Before
	public void startDriver(){
		driver = new FirefoxDriver();
		Conexao.ip(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@After
	public void driverClose(){
		driver.quit();
	}

	@Test
	public void testeCadastroBeneficiarioValidacoes() throws TesteAutomatizadoException {

		LogUtils.log(EnumMensagensLog.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		
		//Acessar Sistema
		AcessoSistema.perfilAdministrador(driver);

		// Acessar menu
		MenuBeneficiarioTemplate.menuBeneficiario(driver);
		
		erros = new ArrayList<>();
		
		// Acessa a tela de cadastro de Beneficiário
		driver.findElement(By.id("btnNovoBeneficiario")).click();
		
		// Realiza teste de campos obrigatórios (No cadastro)
		driver.findElement(By.id("btnAvancar1")).click();   // clica em salvar para marcar os campos
		verificaObrigatoriedadeCampos();
		
		
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
	
	private void verificaObrigatoriedadeCampos(){
		boolean marcacao = true;
		// abre tooltip
		JavaScript.getTooltipClear(driver, "modalNovoCnpj");	// remove o foco do campo "CNPJ" para ser marcado com erro
		
		// verifica se o campo possui marcacão de erro
		if(Validacoes.verificaMarcacaoErroId(driver, "modalNovoCnpj_maindiv") == false){
			erros.add(EnumMensagensLog.CAMPO_OBRIGATORIO.getMensagem() + " CNPJ");
			marcacao = false;
		}
		
		// Verifica a mensagem do tooltip no campo "CNPJ"
		if((marcacao == true) && (Validacoes.verificaMensagemTooltip(driver, "Preenchimento obrigatório!") == false)){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem());			
		}
		
		marcacao = true;	// seta marcacao como true para realizar a validação do campo "Tipo"
		JavaScript.getTooltipClear(driver, "modalNovoTipo_chosen");			// coloca o foco no campo "Tipo" para exibir o tooltip
		
		if(Validacoes.verificaMarcacaoErroId(driver, "modalNovoTipo_maindiv") == false){
			erros.add(EnumMensagensLog.CAMPO_OBRIGATORIO.getMensagem() + " Tipo");
		}
		
		// Verifica a mensagem do tooltip no campo "Tipo"
		if((marcacao == true) && (Validacoes.verificaMensagemTooltip(driver, "Preenchimento obrigatório!") == false)){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem());			
		}
	}
}
