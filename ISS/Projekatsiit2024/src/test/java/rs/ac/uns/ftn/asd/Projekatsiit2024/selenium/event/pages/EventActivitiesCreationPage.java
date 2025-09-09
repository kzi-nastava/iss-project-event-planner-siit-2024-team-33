package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EventActivitiesCreationPage {

	private WebDriver driver;
	
	@FindBy(id = "name")
    private WebElement activityNameInput;

    @FindBy(id = "description")
    private WebElement activityDescriptionInput;

    @FindBy(id = "location")
    private WebElement activityLocationInput;

    @FindBy(css = "input[formcontrolname='startingDate']")
    private WebElement startDateInput;

    @FindBy(css = "input[formcontrolname='startingTime']")
    private WebElement startTimeInput;

    @FindBy(css = "input[formcontrolname='endingDate']")
    private WebElement endDateInput;

    @FindBy(css = "input[formcontrolname='endingTime']")
    private WebElement endTimeInput;

    @FindBy(css = "button.activityButton")
    private WebElement addActivityButton;
    
    public EventActivitiesCreationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    public void addActivity(String name, String description, String location,
            String startDate, String startTime,
            String endDate, String endTime) {
		activityNameInput.clear();
		activityNameInput.sendKeys(name);
		
		activityDescriptionInput.clear();
		activityDescriptionInput.sendKeys(description);
		
		activityLocationInput.clear();
		activityLocationInput.sendKeys(location);
		
		startDateInput.sendKeys(startDate);
		startTimeInput.sendKeys(startTime);
		endDateInput.sendKeys(endDate);
		endTimeInput.sendKeys(endTime);
		
		addActivityButton.click();
    }
}
