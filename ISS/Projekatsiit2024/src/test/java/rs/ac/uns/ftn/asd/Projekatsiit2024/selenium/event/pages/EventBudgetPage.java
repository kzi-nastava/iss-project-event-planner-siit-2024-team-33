package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EventBudgetPage {
	
	private WebDriver driver;
	
	@FindBy(css = "mat-select[formcontrolname='selectedEventType']")
	private WebElement eventTypeSelect;
	
	@FindBy(css = "div.containerBudget")
	private WebElement budgetFormContainer;
	
	public EventBudgetPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void chooseEventType(String eventTypeName) {
        eventTypeSelect.click();
        WebElement option = driver.findElement(
            By.xpath("//mat-option//span[contains(., '" + eventTypeName + "')]"));
        option.click();
    }
	
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
