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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    private List<String> getEventTitles() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(driver -> {
            List<WebElement> elements = driver.findElements(By.cssSelector(".event-card .event-title"));
            if (elements.size() > 0) {
                try {
                    return elements.stream().map(WebElement::getText).collect(Collectors.toList());
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }
            return null;
        });
    }


    @Test
    public void testEventNameFilter() throws InterruptedException {
        loginAsOrganizer();
        driver.get("http://localhost:4200/events/all");

        WebElement searchInput = driver.findElement(By.cssSelector("input[placeholder='Search by name']"));
        searchInput.sendKeys("Alo");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.findElement(By.cssSelector(".more-filters-button")).click();

        WebElement confirmButton = driver.findElement(By.cssSelector("button.confirm"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", confirmButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);

        wait.until(driver -> {
            List<WebElement> elements = driver.findElements(By.cssSelector(".event-card .event-title"));
            return !elements.isEmpty() &&
                   elements.stream().allMatch(e -> e.getText().toLowerCase().contains("alo"));
        });
       
        Thread.sleep(200);
        List<String> titles = getEventTitles();
        Assertions.assertFalse(titles.isEmpty(), "Expected events after filter");
        Assertions.assertTrue(titles.stream().allMatch(t -> t.toLowerCase().contains("alo")),
                "All titles must contain 'Alo'. Found: " + titles);
        System.out.println("Filtered titles: " + titles);
    }



    @Test
    public void testLocationParis() throws InterruptedException {
        loginAsOrganizer();
        driver.get("http://localhost:4200/events/all");

        driver.findElement(By.cssSelector(".more-filters-button")).click();
        driver.findElement(By.cssSelector("input[placeholder='Input 1']")).sendKeys("Paris");
        WebElement confirmButton = driver.findElement(By.cssSelector("button.confirm"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", confirmButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".event-title")));
        Thread.sleep(200);
        List<String> titles = getEventTitles();

        List<String> expected = Arrays.asList(
        	    "Winter Wonderlandsdafasdfasdfsdafdsa",
        	    "Paris Electro Fest",
        	    "Paris Art Expo");
        Assertions.assertEquals(expected, titles, "Filtered titles should match Paris events");
    }

    @Test
    public void testBeforeDate() throws InterruptedException {
        loginAsOrganizer();
        driver.get("http://localhost:4200/events/all");

        driver.findElement(By.cssSelector(".more-filters-button")).click();
        driver.findElement(By.cssSelector("input[placeholder='Select date 1']")).sendKeys("12/31/2024");
        WebElement confirmButton = driver.findElement(By.cssSelector("button.confirm"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", confirmButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);
       
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".event-title")));
        
        Thread.sleep(200);
        List<String> titles = getEventTitles();

        List<String> expected = Arrays.asList("Wedding at jew york city", "Everything and all", "Winter Wonderland");
        Assertions.assertEquals(expected, titles, "Filtered titles should match events before cutoff date");
    }

    @Test
    public void testAfterDate() throws InterruptedException {
        loginAsOrganizer();
        driver.get("http://localhost:4200/events/all");

        driver.findElement(By.cssSelector(".more-filters-button")).click();
        driver.findElement(By.cssSelector("input[placeholder='Select date 2']")).sendKeys("12/01/2025");
        WebElement confirmButton = driver.findElement(By.cssSelector("button.confirm"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", confirmButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> {
            List<WebElement> elements = driver.findElements(By.cssSelector(".event-card .event-title"));
            return !elements.isEmpty();
        });
        Thread.sleep(200);
        List<String> titles = getEventTitles();

        Assertions.assertTrue(titles.stream().allMatch(t -> t.contains("Alo") || t.contains("Marketing")),
            "Filtered titles should be after Dec 2025. Found: " + titles);
    }

    @Test
    public void testSelectConcertAndWedding() throws InterruptedException {
        loginAsOrganizer();
        driver.get("http://localhost:4200/events/all");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".more-filters-button")));
        driver.findElement(By.cssSelector(".more-filters-button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.confirm")));
        WebElement concertDiv = driver.findElement(By.xpath("//label[contains(text(),'Concert')]/ancestor::mat-checkbox//div[@class='mat-mdc-checkbox-touch-target']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", concertDiv);

        WebElement weddingDiv = driver.findElement(By.xpath("//label[contains(text(),'Wedding')]/ancestor::mat-checkbox//div[@class='mat-mdc-checkbox-touch-target']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", weddingDiv);
        
        WebElement confirmButton = driver.findElement(By.cssSelector("button.confirm"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", confirmButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> {
            List<WebElement> elements = driver.findElements(By.cssSelector(".event-card .event-title"));
            return !elements.isEmpty();
        });
        
        Thread.sleep(200);
        List<String> titles = getEventTitles();
        List<String> expected = Arrays.asList(
            "Alo najjaca zureza ikadas dfdskfdsk",
            "Winter Wonderland",
            "Paris Electro Fest",
            "AI Future Conference",
            "Alo najjaca zureza ikadas dfdskfdsk"
        );

        Assertions.assertFalse(titles.isEmpty(), "Expected events after filter");
        Assertions.assertTrue(titles.stream().anyMatch(expected::contains),
            "Filtered titles should contain Concert or Wedding events. Found: " + titles);
    }
    //Paris, 200, All, AFTER 11.11.2025
    
    @Test
    public void testMultiple() throws InterruptedException {
        loginAsOrganizer();
        driver.get("http://localhost:4200/events/all");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //Click on more filters
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".more-filters-button")));
        driver.findElement(By.cssSelector(".more-filters-button")).click();
        //Wait until everything loads
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.confirm")));
        WebElement confirmButton = driver.findElement(By.cssSelector("button.confirm"));
        
        //Select 'All'
        WebElement concertDiv = driver.findElement(By.xpath("//label[contains(text(),'All')]/ancestor::mat-checkbox//div[@class='mat-mdc-checkbox-touch-target']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", concertDiv);
        //Select and input other fields
        driver.findElement(By.cssSelector("input[placeholder='Input 1']")).sendKeys("Paris");        
        driver.findElement(By.cssSelector("input[placeholder='Select date 2']")).sendKeys("11/11/2025");
        driver.findElement(By.cssSelector("input[placeholder='Input 2']")).sendKeys("200");
        
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", confirmButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> {
            List<WebElement> elements = driver.findElements(By.cssSelector(".event-card .event-title"));
            return !elements.isEmpty();
        });
        
        Thread.sleep(200);
        List<String> titles = getEventTitles();
        List<String> expected = Arrays.asList(
            "Paris Art Expo"
        );

        Assertions.assertFalse(titles.isEmpty(), "Expected events after filter");
        Assertions.assertTrue(titles.stream().anyMatch(expected::contains),
            "Filtered titles should contain Concert or Wedding events. Found: " + titles);
    }
}
