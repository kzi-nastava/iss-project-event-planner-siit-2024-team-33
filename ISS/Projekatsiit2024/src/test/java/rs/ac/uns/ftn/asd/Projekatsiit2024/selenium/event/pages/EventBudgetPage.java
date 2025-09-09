package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EventBudgetPage {
	
	private WebDriver driver;
	
	@FindBy(css = "mat-select[formcontrolname='selectedEventType']")
	private WebElement eventTypeSelect;
	
	public EventBudgetPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void chooseEventType(String eventTypeName) {
        eventTypeSelect.click();
        WebElement option = driver.findElement(
            By.xpath("//mat-option//span[contains(text(),'" + eventTypeName + "')]"));
        option.click();
    }
}
