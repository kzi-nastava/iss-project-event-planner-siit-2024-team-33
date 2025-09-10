package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EventBudgetPage {
	
	private WebDriver driver;
	private WebDriverWait wait;
	
	@FindBy(css = "mat-select[formcontrolname='selectedEventType']")
	private WebElement eventTypeSelect;
	
	@FindBy(css = "div.containerBudget")
	private WebElement budgetFormContainer;
	
	public EventBudgetPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // wait up to 5s
		PageFactory.initElements(driver, this);
	}
	
	// Select an event type from the dropdown
    public void chooseEventType(String eventTypeName, int expectedCount) {
    	int oldCount = driver.findElements(By.cssSelector(".scroller .ocCard")).size();
    	
        driver.findElement(By.cssSelector("mat-select[formcontrolname='selectedEventType']")).click();
        driver.findElement(By.xpath("//mat-option//span[contains(., '" + eventTypeName + "')]")).click();

        // Wait until the recommendations update to expected count
        wait.until(driver -> {
            List<WebElement> cards = driver.findElements(By.cssSelector(".scroller .ocCard"));
            return cards.size() == expectedCount;
        });
    }

    // Get all displayed recommendations
    public List<WebElement> getRecommendations() {
        return driver.findElements(By.cssSelector(".scroller .ocCard"));
    }
	
	public boolean isBudgetFormVisible() {
	    try {
	        return budgetFormContainer.isDisplayed();
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	}
}
