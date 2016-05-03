package br.com.marph.selenium.utils;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {
	public static void waitCondicionalXPath(WebDriver driver, int tempo, String xPath){
		/**
		 * Aguarda por "tempo" segundos para o elemento "xPath" tentar ser localizado para exibir um exception na tela
		 */
		WebDriverWait wait = new WebDriverWait(driver, tempo);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
	}
	
	public static void waitCondicionalId(WebDriver driver, int tempo, String idElemento){
		/**
		 * Aguarda por "tempo" segundos para o elemento "idElemento" tentar ser localizado para exibir um exception na tela
		 */
		WebDriverWait wait = new WebDriverWait(driver, tempo);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(idElemento)));
	}
	
	public static void waitCondicionalClass(WebDriver driver, int tempo, String classElemento){
		/**
		 * Aguarda por "tempo" segundos para o elemento "classElemento" tentar ser localizado para exibir um exception na tela
		 */
		WebDriverWait wait = new WebDriverWait(driver, tempo);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(classElemento)));
	}
	
	public static void waitFluentClass(WebDriver driver, int tempoEspera, int tempoVerificacao, String classElemento){
		/**
		 * Aguarda "tempoEspera" segundos para o elemento "classElemento" tentar ser localizado para exibir um exception na tela,
		 * porém este método realiza verificações de a cada "tempoVerificacao" segundos para tentar localizar o elemento. Além disso,
		 * caso o selenium ignora as exceções "NoSuchElementException" lançadas na tela.
		 * 
		 * OBS: Esta exceção pode ser substituída por outras, dependendo da necessidade.
		 */
		new FluentWait<WebDriver>(driver)
				.withTimeout(tempoEspera, TimeUnit.SECONDS)		// tempo máximo de espera
				.pollingEvery(tempoVerificacao, TimeUnit.SECONDS)	// tempo de verificacao do elemento
				.ignoring(NoSuchElementException.class)				// ignora a exceção de elemento não encontrado
				.until(ExpectedConditions.visibilityOfElementLocated(By.className(classElemento)));	// procura o elemento com a classe passada como paramentro
	}
}
