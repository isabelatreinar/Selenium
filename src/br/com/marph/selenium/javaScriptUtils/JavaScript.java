package br.com.marph.selenium.javaScriptUtils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class JavaScript {
	public static void getTooltip(WebDriver driver, String idElemento){
		/**
		 * Coloca o foco no campo fazendo com que o tooltip apareça
		 * Este método funciona somente se o campo estiver preenchido
		 */
		
		String script = "$('#" + idElemento + "').focus();";
		((JavascriptExecutor)driver).executeScript(script);
	}
	
	public static void getTooltipClear(WebDriver driver, String idElemento){
		/**
		 * Coloca o foco no campo fazendo com que o tooltip apareça
		 * Este método é para campos sem dados (Ex. Teste de campos obrigatórios)
		 */
		
		String script = "$('#" + idElemento + "').focusin();";
		((JavascriptExecutor)driver).executeScript(script);
	}
	
	public static void removerFocoCampo(WebDriver driver, String idElemento){
		/**
		 * Remove o foco do campo
		 */
		String script = "$('#" + idElemento + "').focusout();";
		((JavascriptExecutor)driver).executeScript(script);
	}
	
	public static void updateDataPicker(WebDriver driver, String data){
		/**
		 * Insere data nos elementos datepiker 
		 * Formato da String data "dd-mm-aaaa"
		 */
		String script = "$('.input-group.date').datepicker('update', '" + data +"');";
		((JavascriptExecutor)driver).executeScript(script);
	}
}
