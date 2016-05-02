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
import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.javaScriptUtils.JavaScript;
import br.com.marph.selenium.utils.LogUtils;
import br.com.marph.selenium.utils.WaitUtils;
import br.marph.selenium.validacaoUtils.Validacoes;
import org.openqa.selenium.Keys;

public class CadastrarBeneficiarioValidacoes {
	/**
	 * Teste das validações sobre o campo Cnpj no cadastro de Beneficiário
	 * Validações Realizadas:
	 * CNPJ Duplicado ou já utilizado
	 * Pré-condicao: Existir beneficiário com o Cnpj cadastrado
	 * Dados de Teste
	 * CNPJ: 17217985003472
	 * 
	 * CNPJ Não Cadastrado no Cagec
	 * Pre-Condição: O beneficiário não pode estar cadastrado na base de dados do Cagec
	 * Dados de Teste
	 * CNPJ: 18862807000107
	 * 
	 * CNPJ Inválido
	 * Dados de Teste
	 * CNPJ: 18862807003456
	 */
	
	
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

		LogUtils.log(EnumMensagens.INICIO, this.getClass());
		long timestart = System.currentTimeMillis();
		
		//Acessar Sistema
		AcessoSistema.perfilAdministrador(driver);

		// Acessar menu
		MenuBeneficiarioTemplate.menuBeneficiario(driver);
		
		erros = new ArrayList<>();
		
		// Acessa a tela de cadastro de Beneficiário
		driver.findElement(By.id("btnNovoBeneficiario")).click();
		
		// Realiza teste de Cnpj duplicado
		CadastrarBeneficiario.cadastro(driver, "17217985003472", "");
		driver.findElement(By.id("modalNovoCnpj")).sendKeys(Keys.TAB); // retira o foco do campo para que o campo seja marcado
		validacaoCnpjDuplicado();
		
		
		// Realiza teste Cnpj não cadastrado no Cagec
		driver.findElement(By.id("modalNovoCnpj")).sendKeys(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE); //apaga as informações do campo CNPJ
		CadastrarBeneficiario.cadastro(driver, "18862807000107", "");
		driver.findElement(By.id("modalNovoCnpj")).sendKeys(Keys.TAB); // retira o foco do campo para que o toast seja exibido
		validacaoCnpjForaCagec();
		
		
		// Realiza teste Cnpj inválido
		driver.findElement(By.id("modalNovoCnpj")).sendKeys(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE);  //apaga as informações do campo CNPJ
		CadastrarBeneficiario.cadastro(driver, "18862807003456", "");
		driver.findElement(By.id("modalNovoCnpj")).sendKeys(Keys.TAB); // retira o foco do campo para que o campo seja marcado
		validacaoCnpjInvalido();
		

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

	private void validacaoCnpjDuplicado() {
		/* A variavel "marcacao" verifica se existe marcacao no campo, pois se não existir
		 * os elementos do tooltip não serão encontrados, ocasionando um erro no script de teste.
		 */
		boolean marcacao = true;
		JavaScript.removerFocoCampo(driver, "modalNovoCnpj");	// remove o foco do campo para ser marcado com erro
		JavaScript.getTooltip(driver, "modalNovoCnpj");			// coloca o foco no campo para exibir o tooltip
		
		// Verifica se o campo possui validação (está marcado em vermelho)
		if(Validacoes.verificaMarcacaoErroId(driver, "modalNovoCnpj_maindiv") == false){
			erros.add(EnumMensagens.REGISTRO_DUPLICADO.getMensagem() + " Beneficiario - CNPJ já utilizado");
			marcacao = false;
		}
		
		if(marcacao == true){			
			// Verifica se a validação realizada é a de CNPJ já utilizado
			if(Validacoes.verificaMensagemTooltip(driver, "CNPJ já utilizado."))
				erros.add(EnumMensagens.VALIDACAO_INCORRETA.getMensagem() + " Campo 'CNPJ'");
		}
		
	}
	
	private void validacaoCnpjForaCagec() {
		// Valida exibicao do toast apos inserir Cnpj
		if(!driver.findElement(By.id("toast-container")).isDisplayed()){
			erros.add(EnumMensagens.TOAST_DESABILITADO.getMensagem() + " Toast");
		} 
		// Verifica a mensagem de erro (mensagem de CNPJ não encontrado no CAGEC) 
		else if(!driver.findElement(By.xpath("//div[@class='toast-message']")).getText().equals("Não foi possível buscar as informações no CAGEC. [CNPJ-CPF não localizado]")){
			erros.add(EnumMensagens.MENSAGEM_INCORRETA.getMensagem() + " Toast");
		}
	}
	
	private void validacaoCnpjInvalido(){
		/* A variavel "marcacao" verifica se existe marcacao no campo, pois se não existir
		 * os elementos do tooltip não serão encontrados, ocasionando um erro no script de teste.
		 */
		boolean marcacao = true;	
		JavaScript.removerFocoCampo(driver, "modalNovoCnpj");	// remove o foco do campo para ser marcado com erro
		JavaScript.getTooltip(driver, "modalNovoCnpj");			// coloca o foco no campo para exibir o tooltip
		
		// Verifica se o campo possui validação (está marcado em vermelho)
		if(Validacoes.verificaMarcacaoErroId(driver, "modalNovoCnpj_maindiv") == false){
			erros.add(EnumMensagens.DADO_INVALIDO.getMensagem() + " CNPJ - CNPJ inválido");
			marcacao = false;
		}
		
		if(marcacao == true){
			// Aguarda 10 segundos até o elemento ser exibido
			WaitUtils.waitCondicionalXPath(driver, 20, "//*[contains(concat(' ', @class, ' '), ' tooltip-error ')]/div[2]");
			
			// Verifica se a validação realizada é a de CNPJ inválido
			if(Validacoes.verificaMensagemTooltip(driver, "CNPJ inválido!") == false)
				erros.add(EnumMensagens.VALIDACAO_INCORRETA.getMensagem() + " Campo 'CNPJ'");
		}
	}
	
	
}
