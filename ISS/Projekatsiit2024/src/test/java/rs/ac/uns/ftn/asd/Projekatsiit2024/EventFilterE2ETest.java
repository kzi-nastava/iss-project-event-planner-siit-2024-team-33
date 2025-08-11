package rs.ac.uns.ftn.asd.Projekatsiit2024;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import io.github.bonigarcia.wdm.WebDriverManager;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EventFilterE2ETest {

    private WebDriver driver;

    @Autowired
    private EventRepository eventRepository;
    
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


    public void selectCheckboxByLabel(WebDriver driver, String labelText) {
        WebElement label = driver.findElement(By.xpath("//label[contains(text(),'" + labelText + "')]"));
        String forAttribute = label.getAttribute("for");
        WebElement checkbox = driver.findElement(By.id(forAttribute));
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);
        
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    
    @Test
    public void testEventNameFilter() throws InterruptedException {
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

        try { Thread.sleep(150); } catch (InterruptedException ignored) {}

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
    
    @Test
    public void testLocationEvent() {
        driver.get("http://localhost:4200/events/all");

        driver.findElement(By.cssSelector(".more-filters-button")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'Advanced Offering Filters')]")
            )
        );

        WebElement locationElement = driver.findElement(By.cssSelector("input[placeholder='Input 1']"));
        locationElement.clear();
        locationElement.sendKeys("Paris, France");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement confirmButton = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.confirm"))
        );
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", confirmButton);
       
        try { Thread.sleep(150); } catch (InterruptedException ignored) {}


        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        List<WebElement> events = driver.findElements(By.cssSelector(".event-card"));
        Assertions.assertFalse(events.isEmpty(), "There should be some events after filter");

        for (WebElement event : events) {
            String eventId = event.getAttribute("data-id");
            Assertions.assertNotNull(eventId, "Event card is missing data-id attribute");

            Optional<Event> e = eventRepository.findById(Integer.parseInt(eventId));

            if(e.isEmpty()) {
            	new AssertionError("Event not found in repository for id: " + eventId);
            }
            Event realEvent = e.get();
            
            Assertions.assertEquals("Paris, France", realEvent.getPlace(), 
                "Event location does not match filter");
        }
    }
    
    @Test
    public void testAttendees() {
    	driver.get("http://localhost:4200/events/all");
    	
    	driver.findElement(By.cssSelector(".more-filters-button")).click();
    	
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h2[contains(text(),'Advanced Offering Filters')]")
                )
            );
    
        WebElement attendeesElement = driver.findElement(By.cssSelector("input[placeholder='Input 2']"));
        attendeesElement.clear();
        attendeesElement.sendKeys("170");
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement confirmButton = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.confirm"))
        );
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", confirmButton);
       
        try { Thread.sleep(150); } catch (InterruptedException ignored) {}

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        List<WebElement> events = driver.findElements(By.cssSelector(".event-card"));
        Assertions.assertFalse(events.isEmpty(), "There should be some events after filter");

        for (WebElement event : events) {
            String eventId = event.getAttribute("data-id");
            Assertions.assertNotNull(eventId, "Event card is missing data-id attribute");

            Optional<Event> e = eventRepository.findById(Integer.parseInt(eventId));

            if(e.isEmpty()) {
            	new AssertionError("Event not found in repository for id: " + eventId);
            }
            Event realEvent = e.get();
            
            Assertions.assertTrue(realEvent.getNumOfAttendees()>=150, "Expected event to have 150 or more attendees, but got: " + realEvent.getNumOfAttendees());
        }
    }
    
    @Test
    public void testBeforeDate() {
    	driver.get("http://localhost:4200/events/all");
    	
    	driver.findElement(By.cssSelector(".more-filters-button")).click();
    	
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h2[contains(text(),'Advanced Offering Filters')]")
                )
            );
    
        WebElement attendeesElement = driver.findElement(By.cssSelector("input[placeholder='Select date 1']"));
        attendeesElement.clear();
        attendeesElement.sendKeys("12/31/2024");
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement confirmButton = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.confirm"))
        );
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", confirmButton);
       
        try { Thread.sleep(150); } catch (InterruptedException ignored) {}

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        List<WebElement> events = driver.findElements(By.cssSelector(".event-card"));
        Assertions.assertFalse(events.isEmpty(), "There should be some events after filter");
        LocalDate cutoffDate = LocalDate.of(2024, 12, 31);

        for (WebElement event : events) {
            String eventId = event.getAttribute("data-id");
            Assertions.assertNotNull(eventId, "Event card is missing data-id attribute");

            Optional<Event> e = eventRepository.findById(Integer.parseInt(eventId));

            if(e.isEmpty()) {
            	new AssertionError("Event not found in repository for id: " + eventId);
            }
            Event realEvent = e.get();
            LocalDateTime eventDateTime = realEvent.getDateOfEvent();
            LocalDate eventDate = eventDateTime.toLocalDate();

            Assertions.assertTrue(eventDate.isBefore(cutoffDate), "Event date " + eventDateTime + " is not before cutoff date " + cutoffDate);
        }
    }

    @Test
    public void testAfterDate() {
    	driver.get("http://localhost:4200/events/all");
    	
    	driver.findElement(By.cssSelector(".more-filters-button")).click();
    	
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h2[contains(text(),'Advanced Offering Filters')]")
                )
            );
    
        WebElement attendeesElement = driver.findElement(By.cssSelector("input[placeholder='Select date 2']"));
        attendeesElement.clear();
        attendeesElement.sendKeys("12/01/2025");
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement confirmButton = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.confirm"))
        );
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", confirmButton);
       
        try { Thread.sleep(150); } catch (InterruptedException ignored) {}

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        List<WebElement> events = driver.findElements(By.cssSelector(".event-card"));
        Assertions.assertFalse(events.isEmpty(), "There should be some events after filter");
        LocalDate cutoffDate = LocalDate.of(2025, 12, 01);

        for (WebElement event : events) {
            String eventId = event.getAttribute("data-id");
            Assertions.assertNotNull(eventId, "Event card is missing data-id attribute");

            Optional<Event> e = eventRepository.findById(Integer.parseInt(eventId));

            if(e.isEmpty()) {
            	new AssertionError("Event not found in repository for id: " + eventId);
            }
            Event realEvent = e.get();
            LocalDateTime eventDateTime = realEvent.getDateOfEvent();
            LocalDate eventDate = eventDateTime.toLocalDate();

            Assertions.assertTrue(eventDate.isAfter(cutoffDate), "Event date " + eventDateTime + " is not after cutoff date " + cutoffDate);
        }
    }
    
    @Test
    public void testSelectConcertAndWedding() {
        driver.get("http://localhost:4200/events/all");

        driver.findElement(By.cssSelector(".more-filters-button")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Advanced Offering Filters')]"))
        );

        try { Thread.sleep(150); } catch (InterruptedException ignored) {}

        selectCheckboxByLabel(driver, "Concert");
        selectCheckboxByLabel(driver, "Wedding");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.confirm")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", confirmButton);
        try { Thread.sleep(150); } catch (InterruptedException ignored) {}

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));


        List<WebElement> events = driver.findElements(By.cssSelector(".event-card"));
        Assertions.assertFalse(events.isEmpty(), "There should be some events after filtering by Concert and Wedding");
        
        for (WebElement event : events) {
            String eventId = event.getAttribute("data-id");
            Assertions.assertNotNull(eventId, "Event card is missing data-id attribute");

            Optional<Event> e = eventRepository.findById(Integer.parseInt(eventId));

            if(e.isEmpty()) {
            	new AssertionError("Event not found in repository for id: " + eventId);
            }
            
            Event realEvent = e.get();
            EventType eventType = realEvent.getEventType(); 
            String eventTypeName = eventType.getName();

            Assertions.assertTrue(
                    eventTypeName.equalsIgnoreCase("Wedding") || eventTypeName.equalsIgnoreCase("Concert"),
                    "Event type must be either 'Wedding' or 'Concert', but was: " + eventTypeName + " for event id: " + eventId
                );
        }


    }

}
