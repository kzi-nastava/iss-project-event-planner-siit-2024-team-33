package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SideProfilePage {

	private WebDriver driver;
	
	@FindBy(id = "login-btn")
	private WebElement loginButton;
	
	public SideProfilePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void clickLogin() {
		loginButton.click();
	}
}
