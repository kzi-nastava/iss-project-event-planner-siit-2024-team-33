package rs.ac.uns.ftn.asd.Projekatsiit2024.offerReservationTests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class EventFilterE2ETest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    private void loginAsOrganizer() {
        driver.get("http://localhost:4200/authentication/login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));

        driver.findElement(By.id("email")).sendKeys("organizer2@example.com");
        driver.findElement(By.id("password")).sendKeys("pass1234");
        driver.findElement(By.id("submitButton")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("events")));
    }

    private void selectCheckboxByLabel(WebDriver driver, String labelText) {
        WebElement label = driver.findElement(By.xpath("//label[contains(text(),'" + labelText + "')]"));
        String forAttribute = label.getAttribute("for");
        WebElement checkbox = driver.findElement(By.id(forAttribute));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);

        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    private int parseAttendees(String text) {
        return Integer.parseInt(text.replaceAll("\\D+", ""));
    }

    private LocalDate parseDate(String text) {
        return LocalDate.parse(text.trim(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    @Test
    public void testEventNameFilter() {
        loginAsOrganizer();
        driver.get("http://localhost:4200/events/all");

        WebElement searchInput = driver.findElement(By.cssSelector("input[placeholder='Search by name']"));
        searchInput.sendKeys("Alo");

        driver.findElement(By.cssSelector(".more-filters-button")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Advanced Offering Filters')]"))
        );

        WebElement confirmButton = new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.confirm")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", confirmButton);
        confirmButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".event-card"), 0)
        );

        List<WebElement> eventTitles = driver.findElements(By.cssSelector(".event-card .event-title"));
        Assertions.assertFalse(eventTitles.isEmpty(), "There should be some events after filter");

        for (WebElement title : eventTitles) {
            String titleText = title.getText().toLowerCase();
            Assertions.assertTrue(titleText.contains("alo"),
                "Expected event title to contain 'alo' but got: " + titleText);
        }
    }

    @Test
    public void testLocationEvent() {
        loginAsOrganizer();
        driver.get("http://localhost:4200/events/all");

        driver.findElement(By.cssSelector(".more-filters-button")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Advanced Offering Filters')]"))
        );

        WebElement locationElement = driver.findElement(By.cssSelector("input[placeholder='Input 1']"));
        locationElement.clear();
        locationElement.sendKeys("Paris, France");

        WebElement confirmButton = new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.confirm")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", confirmButton);
        confirmButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".event-card"), 0)
        );

        List<WebElement> events = driver.findElements(By.cssSelector(".event-card"));
        Assertions.assertFalse(events.isEmpty(), "There should be some events after filter");

        for (WebElement event : events) {
            String locationText = event.findElement(By.cssSelector(".event-location")).getText();
            Assertions.assertEquals("Paris, France", locationText);
        }
    }

    @Test
    public void testAttendees() {
        loginAsOrganizer();
        driver.get("http://localhost:4200/events/all");

        driver.findElement(By.cssSelector(".more-filters-button")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Advanced Offering Filters')]"))
        );

        WebElement attendeesElement = driver.findElement(By.cssSelector("input[placeholder='Input 2']"));
        attendeesElement.clear();
        attendeesElement.sendKeys("170");

        WebElement confirmButton = new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.confirm")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", confirmButton);
        confirmButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".event-card"), 0)
        );

        List<WebElement> events = driver.findElements(By.cssSelector(".event-card"));
        Assertions.assertFalse(events.isEmpty(), "There should be some events after filter");

        for (WebElement event : events) {
            String attendeesText = event.findElement(By.cssSelector(".event-attendees")).getText();
            int attendees = parseAttendees(attendeesText);
            Assertions.assertTrue(attendees >= 150,
                "Expected >=150 attendees but got: " + attendees);
        }
    }

    @Test
    public void testBeforeDate() {
        loginAsOrganizer();
        driver.get("http://localhost:4200/events/all");

        driver.findElement(By.cssSelector(".more-filters-button")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Advanced Offering Filters')]"))
        );

        WebElement dateElement = driver.findElement(By.cssSelector("input[placeholder='Select date 1']"));
        dateElement.clear();
        dateElement.sendKeys("12/31/2024");

        WebElement confirmButton = new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.confirm")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", confirmButton);
        confirmButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".event-card"), 0)
        );

        List<WebElement> events = driver.findElements(By.cssSelector(".event-card"));
        Assertions.assertFalse(events.isEmpty(), "There should be some events after filter");

        LocalDate cutoffDate = LocalDate.of(2024, 12, 31);

        for (WebElement event : events) {
            String dateText = event.findElement(By.cssSelector(".event-date")).getText();
            LocalDate eventDate = parseDate(dateText);
            Assertions.assertTrue(eventDate.isBefore(cutoffDate),
                "Event date " + eventDate + " is not before cutoff " + cutoffDate);
        }
    }

    @Test
    public void testAfterDate() {
        loginAsOrganizer();
        driver.get("http://localhost:4200/events/all");

        driver.findElement(By.cssSelector(".more-filters-button")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Advanced Offering Filters')]"))
        );

        WebElement dateElement = driver.findElement(By.cssSelector("input[placeholder='Select date 2']"));
        dateElement.clear();
        dateElement.sendKeys("12/01/2025");

        WebElement confirmButton = new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.confirm")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", confirmButton);
        confirmButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".event-card"), 0)
        );

        List<WebElement> events = driver.findElements(By.cssSelector(".event-card"));
        Assertions.assertFalse(events.isEmpty(), "There should be some events after filter");

        LocalDate cutoffDate = LocalDate.of(2025, 12, 1);

        for (WebElement event : events) {
            String dateText = event.findElement(By.cssSelector(".event-date")).getText();
            LocalDate eventDate = parseDate(dateText);
            Assertions.assertTrue(eventDate.isAfter(cutoffDate),
                "Event date " + eventDate + " is not after cutoff " + cutoffDate);
        }
    }

    @Test
    public void testSelectConcertAndWedding() {
        loginAsOrganizer();
        driver.get("http://localhost:4200/events/all");

        driver.findElement(By.cssSelector(".more-filters-button")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Advanced Offering Filters')]"))
        );

        selectCheckboxByLabel(driver, "Concert");
        selectCheckboxByLabel(driver, "Wedding");

        WebElement confirmButton = new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.confirm")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", confirmButton);
        confirmButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".event-card"), 0)
        );

        List<WebElement> events = driver.findElements(By.cssSelector(".event-card"));
        Assertions.assertFalse(events.isEmpty(),
            "There should be some events after filtering by Concert and Wedding");

        for (WebElement event : events) {
            String typeText = event.findElement(By.cssSelector(".event-type")).getText();
            Assertions.assertTrue(
                typeText.equalsIgnoreCase("Wedding") || typeText.equalsIgnoreCase("Concert"),
                "Event type must be Wedding or Concert but got: " + typeText
            );
        }
    }
}
