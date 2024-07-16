package objects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
/**
 * Holds all the elements for boxillas login page
 * @author Boxilla
 *
 */
public class Loginpage {
	
	private static WebElement element = null;
	public static String userNameXpath = ".//*[@id='login_login']"; 
	private static String passwordXpath = ".//*[@id='login_password']";
	private static String loginBtnXpath = "//input[@value='Login']";
	
	public static String getLoginBtnXpath() {
		return loginBtnXpath;
	}
	public static String getPasswordXpath() {
		return passwordXpath;
	}
	
	public static String getUserNameXpath() {
		return userNameXpath;
	}
	
	public static WebElement username(WebDriver driver) {
		element = driver.findElement(By.xpath(userNameXpath));
		return element;
	}
	
	public static WebElement password(WebDriver driver) {
		element = driver.findElement(By.xpath(".//*[@id='login_password']"));
		return element;
	}
	
	public static WebElement loginbtn(WebDriver driver) {
		element = driver.findElement(By.xpath("//input[@value='Login']"));
		return element;
	}
	public static WebElement errorMsg(WebDriver driver) 
	{
		 element=driver.findElement(By.xpath(".//*[@class='foreman-alert alert alert-danger']"));
		 return element;
	}
	
}
