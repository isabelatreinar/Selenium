package br.com.marph.selenium.utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SelecionaPerfil {
	public static void administrador(WebDriver driver){
		driver.findElement(By.id("17069")).click();
	}
	
		public static void equipeGeicom(WebDriver driver){
			driver.findElement(By.id("17065")).click();
		}
}
