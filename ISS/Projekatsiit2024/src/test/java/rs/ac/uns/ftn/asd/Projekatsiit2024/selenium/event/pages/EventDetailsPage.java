package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EventDetailsPage {

	private WebDriver driver;
    private WebDriverWait wait;

    private final By titleLocator = By.cssSelector(".eventTitle h2");
    private final By descriptionLocator = By.cssSelector(".description p");
    private final By pdfButtonLocator = By.xpath("//button[span[text()='Event Details PDF']]");
    
    public EventDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
    
    public String getEventTitle() {
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(titleLocator));
        return title.getText();
    }

    public String getEventDescription() {
        WebElement description = wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionLocator));
        return description.getText();
    }

    public boolean isEventDetailsLoaded() {
        return !getEventTitle().isEmpty();
    }
    
    public void clickGeneratePdfButton() {
        WebElement pdfButton = wait.until(ExpectedConditions.visibilityOfElementLocated(pdfButtonLocator));
        
        // Scroll into view so sticky navbar does not cover it
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", pdfButton);
        
        // Click via JS to bypass native Selenium click issues
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", pdfButton);
    }
}
