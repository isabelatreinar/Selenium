package br.marph.selenium.validacaoUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.marph.selenium.enums.EnumValidacao;

public class Validacoes {
	/**
	 * Verifica se o campo está marcado em vermelho. Se não estiver marcado em vermelho, significa que o campo está
	 * sem validação.
	 * Quando o campo está em vermelho é adicionado a classe "has-error" junto a div "idCampo_maindiv"
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
		return false;	// mensgaem diferente retorna false
	}
}
