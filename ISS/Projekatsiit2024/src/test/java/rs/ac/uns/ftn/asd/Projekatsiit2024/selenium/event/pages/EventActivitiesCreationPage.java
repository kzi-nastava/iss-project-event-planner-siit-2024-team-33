package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EventActivitiesCreationPage {

	private WebDriver driver;
	private WebDriverWait wait;
	
	@FindBy(id = "activity-name")
    private WebElement activityNameInput;

    @FindBy(id = "activity-description")
    private WebElement activityDescriptionInput;

    @FindBy(id = "activity-location")
    private WebElement activityLocationInput;

    @FindBy(id = "activity-startingDate")
    private WebElement startDateInput;

    @FindBy(id = "activity-startingTime")
    private WebElement startTimeInput;

    @FindBy(id = "activity-endingDate")
    private WebElement endDateInput;

    @FindBy(id = "activity-endingTime")
    private WebElement endTimeInput;

    @FindBy(css = "button.activityButton")
    private WebElement addActivityButton;
    
    public EventActivitiesCreationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
    
    public void addActivity(String name, String description, String location,
            String startDate, String startTime,
            String endDate, String endTime) {
    	WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(activityNameInput));
        nameInput.clear();
        nameInput.sendKeys(name);

        WebElement descInput = wait.until(ExpectedConditions.elementToBeClickable(activityDescriptionInput));
        descInput.clear();
        descInput.sendKeys(description);

        WebElement locationInput = wait.until(ExpectedConditions.elementToBeClickable(activityLocationInput));
        locationInput.clear();
        locationInput.sendKeys(location);

        WebElement startDateEl = wait.until(ExpectedConditions.elementToBeClickable(startDateInput));
        startDateEl.clear();
        startDateEl.sendKeys(startDate);

        WebElement startTimeEl = wait.until(ExpectedConditions.elementToBeClickable(startTimeInput));
        startTimeEl.clear();
        startTimeEl.sendKeys(startTime);

        WebElement endDateEl = wait.until(ExpectedConditions.elementToBeClickable(endDateInput));
        endDateEl.clear();
        endDateEl.sendKeys(endDate);

        WebElement endTimeEl = wait.until(ExpectedConditions.elementToBeClickable(endTimeInput));
        endTimeEl.clear();
        endTimeEl.sendKeys(endTime);

        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(addActivityButton));
        addBtn.click();
    }
    
    public List<WebElement> getActivityCards() {
    	List<WebElement> cards = driver.findElements(By.cssSelector(".eaCard"));
        return cards;
    }
    
    public int getActivityCount() {
        return getActivityCards().size();
    }
    
    public void waitForActivityCount(int expectedCount) {
        wait.until(driver -> getActivityCount() == expectedCount);
    }
    
    public void removeFirstActivity() {
        int currentCount = getActivityCount();

        WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".eaCard:first-of-type button")
            ));
        removeButton.click();

        wait.until(driver -> getActivityCount() == currentCount - 1);
    }
}
