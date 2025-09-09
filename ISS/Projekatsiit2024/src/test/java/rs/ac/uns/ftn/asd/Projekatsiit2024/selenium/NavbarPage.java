package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NavbarPage {
	
	private WebDriver driver;
	
	@FindBy(css = ".profile-icon img")
	private WebElement profileIcon;
	
	public NavbarPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void openSideProfile() {
		profileIcon.click();
	}
}
