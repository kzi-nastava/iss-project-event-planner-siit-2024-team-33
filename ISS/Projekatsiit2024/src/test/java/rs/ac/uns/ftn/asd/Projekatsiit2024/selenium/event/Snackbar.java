package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Snackbar {
	
	private WebDriver driver;
	private WebDriverWait wait;
	
	private By snackbarLabel = By.cssSelector("simple-snack-bar .mat-mdc-snack-bar-label");
    private By snackbarContainer = By.cssSelector("simple-snack-bar");
    
    public Snackbar(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }
    
    public String getMessage() {
        WebElement label = wait.until(
            ExpectedConditions.visibilityOfElementLocated(snackbarLabel)
        );
        return label.getText().trim();
    }
    
    public void waitUntilGone() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(snackbarContainer));
    }
}
