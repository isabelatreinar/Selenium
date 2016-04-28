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
import br.com.marph.selenium.enums.EnumValidacao;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;
import org.openqa.selenium.JavascriptExecutor;

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
	 * 
	 *  CNPJ e Tipo de beneficiário em branco
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
		
		
		// Realiza teste de Cnpj duplicado
		driver.findElement(By.id("btnNovoBeneficiario")).click();
		CadastrarBeneficiario.cadastro(driver, "17217985003472", "");
		validacaoCnpjDuplicado();
		
		
		// Realiza teste Cnpj não cadastrado no Cagec
		driver.findElement(By.id("btnNovoBeneficiario")).click();
		CadastrarBeneficiario.cadastro(driver, "18862807000107", "");
		validacaoCnpjForaCagec();
		
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
		String script = "$('#modalNovoCnpj').focus();";
		((JavascriptExecutor)driver).executeScript(script);
		
		if(driver.findElement(By.id("modalNovoCnpj")).getAttribute("class").equals(EnumValidacao.MARCACAO_ERRO.getHtml())){
			erros.add(EnumMensagens.REGISTRO_DUPLICADO.getMensagem() + " Beneficiario - CNPJ já utilizado");
		}
		
		// Verifica primeiramente se o elemento Html está visível, pois existe tooltip somente se o campo tiver marcado em vermelho
		if(driver.findElement(By.xpath("//*[contains(concat(' ', @class, ' '), ' tooltip-error ')]")).isDisplayed()){
			if(!driver.findElement(By.xpath("//*[contains(concat(' ', @class, ' '), ' tooltip-error ')]/div[2]")).getText().equals("CNPJ já utilizado."))
				erros.add(EnumMensagens.VALIDACAO_INCORRETA.getMensagem());
		}
	}
	
	private void validacaoCnpjForaCagec() {
		// Valida exibicao do toast apos inserir Cnpj
		if(!driver.findElement(By.id("toast-container")).isDisplayed()){
			erros.add(EnumMensagens.TOAST_DESABILITADO.getMensagem());
		}
	}
}
