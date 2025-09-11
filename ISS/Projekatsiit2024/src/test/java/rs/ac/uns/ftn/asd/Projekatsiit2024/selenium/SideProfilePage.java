package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SideProfilePage {

	private WebDriver driver;
	private WebDriverWait wait;
	
	public SideProfilePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}
	
	public void clickLogin() {
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn")));
        loginButton.click();
    }

    public void clickCreateEvent() {
        WebElement createEventButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.id("events-creation-btn")));
        createEventButton.click();
    }
}
