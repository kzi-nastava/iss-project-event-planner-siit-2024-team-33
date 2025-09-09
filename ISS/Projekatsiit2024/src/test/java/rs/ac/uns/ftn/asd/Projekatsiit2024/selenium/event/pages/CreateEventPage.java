package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateEventPage {

	private WebDriver driver;
	private WebDriverWait wait;

    @FindBy(id = "next-step")
    private WebElement nextButton;

    @FindBy(id = "previous-step")
    private WebElement previousButton;

    @FindBy(id = "confirm-event")
    private WebElement confirmButton;

    //child components are referenced by their page objects
    private EventMandatoryDataPage mandatoryDataPage;
    private EventBudgetPage budgetPage;
    private EventActivitiesCreationPage activitiesPage;

    public CreateEventPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);

        //initialize child pages
        mandatoryDataPage = new EventMandatoryDataPage(driver);
        budgetPage = new EventBudgetPage(driver);
        activitiesPage = new EventActivitiesCreationPage(driver);
    }

    public EventMandatoryDataPage mandatoryData() {
        return mandatoryDataPage;
    }

    public EventBudgetPage budgetData() {
        return budgetPage;
    }

    public EventActivitiesCreationPage activitiesData() {
        return activitiesPage;
    }

    public void goNext() {
    	wait.until(ExpectedConditions.elementToBeClickable(nextButton));
        nextButton.click();
    }

    public void goPrevious() {
    	wait.until(ExpectedConditions.elementToBeClickable(previousButton));
        previousButton.click();
    }

    public void confirmEvent() {
    	wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
        confirmButton.click();
    }
}
