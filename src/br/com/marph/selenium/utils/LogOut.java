package br.com.marph.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LogOut {
	public static void logOut(WebDriver driver){
		driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[2]/li/a")).click();
	}
}
