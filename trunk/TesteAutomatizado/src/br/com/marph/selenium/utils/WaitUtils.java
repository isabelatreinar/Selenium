package br.com.marph.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {
	public static void waitCondicionalXPath(WebDriver driver, int tempo, String xPath){
		/**
		 * Aguarda por "tempo" segundos o elemento "xPath" ser exibido para exibir um exception na tela
		 */
		WebDriverWait wait = new WebDriverWait(driver, tempo);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
	}
	
	public static void waitCondicionalId(WebDriver driver, int tempo, String idElemento){
		/**
		 * Aguarda por "tempo" segundos o elemento "idElemento" ser exibido para exibir um exception na tela
		 */
		WebDriverWait wait = new WebDriverWait(driver, tempo);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(idElemento)));
	}
	
	public static void waitCondicionalClass(WebDriver driver, int tempo, String classElemento){
		/**
		 * Aguarda por "tempo" segundos o elemento "classElemento" ser exibido para exibir um exception na tela
		 */
		WebDriverWait wait = new WebDriverWait(driver, tempo);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(classElemento)));
	}
}
