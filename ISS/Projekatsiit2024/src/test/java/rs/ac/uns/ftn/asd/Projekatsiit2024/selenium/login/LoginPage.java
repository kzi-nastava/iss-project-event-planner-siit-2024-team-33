package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.login;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
	
	private WebDriver driver;
	
	@FindBy(id = "email")
	private WebElement emailInput;
	
	@FindBy(id = "password")
	private WebElement passwordInput;
	
	@FindBy(id = "submitButton")
	private WebElement submitButton;
	
	@FindBy(css = ".error-message")
	private WebElement errorMessage;
	
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void enterEmail(String email) {
		emailInput.clear();
		emailInput.sendKeys(email);
	}
	
	public void enterPassword(String password) {
		passwordInput.clear();
		passwordInput.sendKeys(password);
	}
	
	public void clickSubmit() {
		submitButton.click();
	}
	
	public String getErrorMessage() {
		return errorMessage.getText();
	}
	
	public void login(String email, String password) {
		enterEmail(email);
		enterPassword(password);
		clickSubmit();
	}
}
