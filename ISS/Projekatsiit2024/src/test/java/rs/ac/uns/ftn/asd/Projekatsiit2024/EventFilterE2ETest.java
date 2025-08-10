package rs.ac.uns.ftn.asd.Projekatsiit2024;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

public class EventFilterE2ETest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testEventNameFilterWithConfirm() throws InterruptedException {
        driver.get("http://localhost:4200/events/all");

        WebElement searchInput = driver.findElement(By.cssSelector("input[placeholder='Search by name']"));
        searchInput.sendKeys("Alo");

        driver.findElement(By.cssSelector(".more-filters-button")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Advanced Offering Filters')]"))
        );

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.confirm")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", confirmButton);

        confirmButton.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        List<WebElement> eventTitles = driver.findElements(By.cssSelector(".event-card .event-title"));
        Assertions.assertFalse(eventTitles.isEmpty(), "There should be some events after filter");

        for (WebElement title : eventTitles) {
            String titleText = title.getText().toLowerCase();
            Assertions.assertTrue(titleText.contains("alo"),
                "Expected 'alo' but got:: " + titleText);
        }
    }

}
