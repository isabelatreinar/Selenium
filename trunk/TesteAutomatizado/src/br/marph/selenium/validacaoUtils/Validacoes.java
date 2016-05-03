package br.marph.selenium.validacaoUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.enums.EnumValidacao;

public class Validacoes {
	/**
	 * Esta classe possui vários métodos para realizar validações de diversos elementos presentes no Geicom
	 * 
	 */
	public static boolean verificaMarcacaoErroId(WebDriver driver, String idElemento){
		/**
		 * Realiza a validação da marcação de erro em campos que são identificados através do Id
		 * Se o campo estiver com a marcação em vermelho retorna true e se estiver sem a marcação retorna false
		 */
		if(driver.findElement(By.id(idElemento)).getAttribute("class").contains(EnumValidacao.MARCACAO_ERRO.getHtml()))
			return true;		// campo com a marcação
		return false;	// campo sem a marcação
	}
	
	public static boolean verificaMarcacaoErroxPath(WebDriver driver, String xpath){
		/**
		 * Realiza a validação da marcação de erro em campos que são identificados através do xPath
		 * Se o campo estiver com a marcação em vermelho retorna true e se estiver sem a marcação retorna false
		 */
		if(driver.findElement(By.xpath(xpath)).getAttribute("class").contains(EnumValidacao.MARCACAO_ERRO.getHtml()))
			return true;		// campo com a marcação
		return false;	// campo sem a marcação
	}
	
	public static boolean verificaMensagemTooltip(WebDriver driver, String mensagem){
		/**
		 * Verifica se a mensagem/validação no campo está correta
		 * Se for igual a mensagem passada como paramento o método retorna true
		 * Caso contrario retorna false
		 */
		if(driver.findElement(By.xpath("//*[contains(concat(' ', @class, ' '), ' tooltip-error ')]/div[2]")).getText().equals(mensagem))
			return true;	// mensagem desejada retorna true
		return false;	// mensagem diferente retorna false
	}
	
	public static boolean verificaExibicaoToast(WebDriver driver){
		/**
		 * Verifica se o toast foi exibido para o usuario
		 * Se for exibido retorna true
		 * Caso contrário retorna false
		 */
		if(driver.findElement(By.id("toast-container")).isDisplayed())
			return true;	// toast exibido
		return false;		// toast não exibido
	}
	
	public static boolean verificaMensagemToast(WebDriver driver, String mensagem){
		/**
		 * Valida a mensagem exibida no toast
		 * Se for igual retorna true
		 * Caso contrário retorna false
		 * 
		 */
		
		//driver.findElement(By.xpath("//*[@id='toast-container']/div[2]")).getText() retorna a mensagem exibida no toast
		if(driver.findElement(By.xpath("//*[@id='toast-container']/div/div[2]")).getText().equals(mensagem))
			return true;	// mensgaem exibida está correta
		return false;		// mensgaem exibida não está correta
	}
	
	public static boolean verificaBreadCrumb(WebDriver driver, String breadCrumb){
		/**
		 * Valida o breadCrumb da página
		 * o parâmentro "breadCrumb" deve ser passado conforme está apresentado na tela após 
		 * a string "Você está em: "
		 */
		if(driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/ol")).getText().equals("Você está em: "+ breadCrumb))
				return true;	
			return false;		
	}
	
	public static boolean verificaModalAlerta(WebDriver driver){
		/**
		 * Verifica se o modal de alerta foi exibido para o usuário
		 * Se foi exibido retorna true
		 * Caso contrário false
		 * 
		 */
		if(driver.findElement(By.className("jconfirm-box")).isDisplayed())
			return true;	// modal está sendo exibido
		return false;	// modal não está sendo exibido
	}
	
	public static boolean verificaMensagemModalAlerta(WebDriver driver, String mensagem){
		/**
		 * Verifica a mensagem exibida no modal de alerta
		 * Se foi a mesma do parâmetro retorna true
		 * Caso contrário false
		 * 
		 */
		if(driver.findElement(By.xpath("//*[@class='jconfirm-box']/div[3]")).getText().equals(mensagem))
			return true;	
		return false;	
	}
}
