package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EventMandatoryDataPage {
	
	private WebDriver driver;
	private WebDriverWait wait;
	
	@FindBy(id = "name")
    private WebElement nameInput;
    
    @FindBy(id = "description")
    private WebElement descriptionInput;
    
    @FindBy(id = "attendees")
    private WebElement attendeesInput;
    
    @FindBy(id = "location")
    private WebElement locationInput;
    
    @FindBy(id = "publicRadio")
    private WebElement publicRadioInput;

    @FindBy(id = "privateRadio")
    private WebElement privateRadioInput;
    
    @FindBy(css = "input[formcontrolname='startingDate']")
    private WebElement startDateInput;
    
    @FindBy(css = "input[formcontrolname='startingTime']")
    private WebElement startTimeInput;
    
    @FindBy(css = "input[formcontrolname='endingDate']")
    private WebElement endDateInput;
    
    @FindBy(css = "input[formcontrolname='endingTime']")
    private WebElement endTimeInput;
    
    @FindBy(id = "map-toggle")
    private WebElement openMapButton;
    
    @FindBy(css = ".map-actions button:nth-child(1)")
    private WebElement confirmMapButton;
	
	public EventMandatoryDataPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}
	
	public void fillMandatoryData(String name, String description, int attendees,
            boolean isPrivate,
            String startDate, String startTime,
            String endDate, String endTime,
            double mapLat, double mapLng) {

		wait.until(ExpectedConditions.visibilityOf(nameInput)).clear();
		nameInput.sendKeys(name);
		
		wait.until(ExpectedConditions.visibilityOf(descriptionInput)).clear();
		descriptionInput.sendKeys(description);
		
		wait.until(ExpectedConditions.visibilityOf(attendeesInput)).clear();
		attendeesInput.sendKeys(String.valueOf(attendees));
		
		if (isPrivate) {
		wait.until(ExpectedConditions.elementToBeClickable(privateRadioInput)).click();
		} else {
		wait.until(ExpectedConditions.elementToBeClickable(publicRadioInput)).click();
		}
		
		wait.until(ExpectedConditions.visibilityOf(startDateInput)).sendKeys(startDate);
		wait.until(ExpectedConditions.visibilityOf(startTimeInput)).sendKeys(startTime);
		wait.until(ExpectedConditions.visibilityOf(endDateInput)).sendKeys(endDate);
		wait.until(ExpectedConditions.visibilityOf(endTimeInput)).sendKeys(endTime);
		
		// Map
		wait.until(ExpectedConditions.elementToBeClickable(openMapButton)).click();
		
		WebElement mapCanvas = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#map")));
		int xOffset = (int)(mapLat * 2);
		int yOffset = (int)(mapLng * 2);
		Actions actions = new Actions(driver);
		actions.moveToElement(mapCanvas, xOffset, yOffset).click().perform();
		
		wait.until(ExpectedConditions.elementToBeClickable(confirmMapButton)).click();
	}
}
