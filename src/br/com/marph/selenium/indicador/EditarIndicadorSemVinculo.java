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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.marph.selenium.conexao.AcessoSistema;
import br.com.marph.selenium.conexao.Conexao;
import br.com.marph.selenium.enums.EnumMensagensLog;
import br.com.marph.selenium.exceptions.TesteAutomatizadoException;
import br.com.marph.selenium.utils.LogUtils;
import br.com.marph.selenium.utils.WaitUtils;
import br.marph.selenium.validacaoUtils.Validacoes;

public class EditarIndicadorSemVinculo {
	/**
	 * Teste de Edição de indicador sem vínculo (Caminho Feliz)
	 * Dados de Teste
	 * Informações do indicador
	 * Tipo de Indicador: Finalístico
	 * Nome: Indicador teste Edição de Indicador
	 * Tipo de Fonte: Declaratório
	 * Nome da Fonte: Beneficiário
	 * Polaridade: Quanto menor, melhor
	 * Programa: Programa Teste
	 * Meses da média Móvel: 12
	 * Meses de Defasagem: 12
	 * Descrição: Teste automatizado de edição do indicador.
	 * 
	 * Informações da variável
	 * Nome: Variável Edição
	 * Descrição: Variável de teste automatizado de edição de indicador.
	 * OBS:A variável não foi editada, pois variáveis que são utilizadas na fórmula do indicador não podem ser ediadas.
	 * 
	 * Fórmula do Indicador
	 * Fórmula: VARIAVEL_EDICAO_26
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
	public void testeEdicaoIndicadorSemVinculo() throws TesteAutomatizadoException {
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
		pesquisar(driver, "Finalístico", "Indicador teste Edição de Indicador", "Quanto menor, melhor", "Programa Teste");
		
		// Acessa a tela de edição do indicador
		driver.findElement(By.id("btnEditar1")).click();
		
		// Limpa os dados do formulário
		limparDados();
		
		// Edita os dados do indicador
		CadastrarIndicador.cadastroIndicador(driver, "Processual", "Indicador teste Edição de Indicador Automatizada", "Oficial", "DATASUS", "Quanto maior, melhor", "Programa Teste", "24", "15", "Edição automatizada de indicador");

		// Salva a edição das informações do indicador
		driver.findElement(By.id("btnSalvar1")).click();
		
		// Verifica as informações salvas
		verificaInformacoes(driver, "Processual", "Indicador teste Edição de Indicador Automatizada", "Oficial", "DATASUS", "Quanto maior, melhor", "Programa Teste", "24", "15", "Edição automatizada de indicador");
		
		// Acessa a tela de fórmula do indicador
		driver.findElement(By.id("btnFormulaIndicadores")).click();
		
		// Editar a fórmula do indicador
		editarFormula();
		
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
	
	public static void pesquisar(WebDriver driver, String tipo, String nome, String polaridade, String programa) {
		// Seleciona o filtro "Tipo"
		driver.findElement(By.id("tipoIndicador_chosen")).click();
		driver.findElement(By.xpath("//*[@id='tipoIndicador_chosen']/div/div/input")).sendKeys(tipo);
		driver.findElement(By.xpath("//*[@id='tipoIndicador_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		//Preenche o filtro "Nome"
		driver.findElement(By.id("nomeIndicador")).sendKeys(nome);
				
		//expandir pesquisa
		driver.findElement(By.id("btnExpandirPesquisaAvancada")).click();
		
		// Seleciona o filtro "Polaridade"
		driver.findElement(By.id("polaridadeIndicador_chosen")).click();
		driver.findElement(By.xpath("//*[@id='polaridadeIndicador_chosen']/div/div/input")).sendKeys(polaridade);
		driver.findElement(By.xpath("//*[@id='polaridadeIndicador_chosen']/div/div/input")).sendKeys(Keys.TAB);
		
		// Seleciona o filtro "Programa"
		driver.findElement(By.id("programa_chosen")).click();
		driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input")).sendKeys(programa);
		driver.findElement(By.xpath("//*[@id='programa_chosen']/div/div/input")).sendKeys(Keys.TAB);
				
		//clica no botao pesquisar.
		driver.findElement(By.id("btnPesquisar")).click();
		
		// Aguarda o registro ser exibido
		WaitUtils.waitFluentClass(driver, 30, 5, "sorting_1");
		
		// Acessa registro
		driver.findElement(By.className("sorting_1")).click();
	}

	public void limparDados() {
		/**
		 * Limpa somente os dados dos campos que não são combobox
		 */
		// Apaga os dados do campo "Nome do indicador"
		driver.findElement(By.id("nomeIndicador")).sendKeys(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE);
		
		// Apaga os dados do campo "Nome da fonte"
		driver.findElement(By.id("nomeFonte")).sendKeys(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE);
		
		// Apaga os dados do campo "Meses da média Móvel"
		driver.findElement(By.id("mesesDaMediaMovel")).sendKeys(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE);
		
		// Apaga os dados do campo "Meses de Defasagem"
		driver.findElement(By.id("mesesDeDefasagem")).sendKeys(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE);
		
		// Apaga os dados do campo "Descrição"
		driver.findElement(By.id("descricao")).sendKeys(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE);

	}
	
	public void verificaInformacoes(WebDriver driver, String tipoIndicador, String nomeIndicador, String tipoFonte, String nomeFonte, String polaridade, String programa, 
			String mesesMediaMovel, String mesesDefasagem, String descricao){
		// Valida a exibição do toast
		if(Validacoes.verificaExibicaoToast(driver) == false){
			erros.add(EnumMensagensLog.TOAST_DESABILITADO.getMensagem());
		}
		// Valida a mensagem do toast
		else if(Validacoes.verificaMensagemToast(driver, "Indicador salvo com sucesso") == false){
			erros.add(EnumMensagensLog.MENSAGEM_INCORRETA.getMensagem() + "Toast - Informações Indicador");
		}
		// Valida o campo "Tipo de Indicador"
		if(!driver.findElement(By.id("tipoIndicador")).getText().equals(tipoIndicador))
			erros.add(EnumMensagensLog.ERRO_SALVAR.getMensagem() + "Tipo Indicador");
		
		// Valida o campo "Nome do Indicador"
		if(!driver.findElement(By.id("nomeIndicador")).getText().equals(nomeIndicador))
			erros.add(EnumMensagensLog.ERRO_SALVAR.getMensagem() + "Nome Indicador");
		
		// Valida o campo "Tipo da Fonte"
		if(!driver.findElement(By.id("tipoFonte")).getText().equals(tipoFonte))
			erros.add(EnumMensagensLog.ERRO_SALVAR.getMensagem() + "Tipo da Fonte");
		
		// Valida o campo "Nome da Fonte"
		if(!driver.findElement(By.id("nomeFonte")).getText().equals(nomeFonte))
			erros.add(EnumMensagensLog.ERRO_SALVAR.getMensagem() + "Nome da Fonte");
		
		// Valida o campo "Polaridade"
		if(!driver.findElement(By.id("polaridade")).getText().equals(polaridade))
			erros.add(EnumMensagensLog.ERRO_SALVAR.getMensagem() + "Polaridade");
		
		// Valida o campo "Programa"
		if(!driver.findElement(By.id("programa")).getText().equals(programa))
			erros.add(EnumMensagensLog.ERRO_SALVAR.getMensagem() + "Programa");
		
		// Valida o campo "Meses da média móvel"
		if(!driver.findElement(By.id("mesesMediaMovel")).getText().equals(mesesMediaMovel))
			erros.add(EnumMensagensLog.ERRO_SALVAR.getMensagem() + "Meses da média móvel");
		
		// Valida o campo "Meses de Defasagem"
		if(!driver.findElement(By.id("mesesDefasagem")).getText().equals(mesesDefasagem))
			erros.add(EnumMensagensLog.ERRO_SALVAR.getMensagem() + "Meses de Defasagem");
		
		// Valida o campo "Descricao"
		if(!driver.findElement(By.id("descricao")).getText().equals(descricao))
			erros.add(EnumMensagensLog.ERRO_SALVAR.getMensagem() + "Descrição");
	}

	private void editarFormula(){
		// Habilitar campo para edição
		driver.findElement(By.id("btnEditar1")).click();
		
		// Inserir componentes
		driver.findElement(By.id("formula")).sendKeys(Keys.END);
		driver.findElement(By.id("formula")).sendKeys("*2");
		
		// Salvar fórmula
		driver.findElement(By.id("btnSalvar1")).click();
	}
}