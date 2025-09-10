package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.BaseSeleniumTest;
import rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.NavbarPage;
import rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.SideProfilePage;
import rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.Snackbar;
import rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages.CreateEventPage;
import rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages.EventDetailsPage;
import rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages.EventsPage;
import rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages.PdfUtils;

public class EventCreationTest extends BaseSeleniumTest {
	
    private String baseUrl = "http://localhost:4200";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Test
    public void createPublicEventWithValidData() throws Exception {
    	loginAsOrganizer();

        driver.get(baseUrl + "/events/creation");

        CreateEventPage createEventPage = new CreateEventPage(driver);
        Thread.sleep(500);
        
        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDate endDate = startDate;

        createEventPage.mandatoryData().fillMandatoryData(
        	    "Public Event",
        	    "This is a test public event",
        	    10,
        	    false,
        	    startDate.format(DATE_FORMAT),
        	    "13:00",
        	    endDate.format(DATE_FORMAT),
        	    "14:00",
        	    44.8176,
        	    20.4569
        );

        createEventPage.goNext();
        createEventPage.goNext();
        createEventPage.confirmEvent();
        
        Snackbar snackbar = new Snackbar(driver);
        String actualMessage = snackbar.getMessage();
        assertEquals("The creation of an event succeeded.", actualMessage);
    }
    
    @Test
    void shouldCreateEventWithSecondEventTypeOptionAndThreeRecommendations() throws Exception {
        loginAsOrganizer();
        driver.get(baseUrl + "/events/creation");

        CreateEventPage createEventPage = new CreateEventPage(driver);
        Thread.sleep(500);

        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = startDate;
        
        createEventPage.mandatoryData().fillMandatoryData(
            "Public Event with Budget",
            "Test event to check budget recommendations",
            15,
            false,
            startDate.format(DATE_FORMAT),
            "13:00",
            endDate.format(DATE_FORMAT),
            "16:00",
            44.8176,
            20.4569
        );

        createEventPage.goNext();
        
        Thread.sleep(500);

        // Select second event type — recommendations auto-wait
        createEventPage.budgetData().chooseEventType("Conference", 3);

        // Assert recommendations
        List<WebElement> recommendations = createEventPage.budgetData().getRecommendations();
        assertEquals(3, recommendations.size(), "Expected exactly 3 recommendations to be displayed");

        createEventPage.goNext();
        createEventPage.confirmEvent();

        Snackbar snackbar = new Snackbar(driver);
        String actualMessage = snackbar.getMessage();
        assertEquals("The creation of an event succeeded.", actualMessage);
    }
    
    @Test
    void shouldCreateEventWithTwoActivities() throws Exception {
        loginAsOrganizer();
        driver.get(baseUrl + "/events/creation");

        CreateEventPage createEventPage = new CreateEventPage(driver);
        Thread.sleep(500);
        
        LocalDate eventDate = LocalDate.now().plusDays(15);

        createEventPage.mandatoryData().fillMandatoryData(
            "Event With Two Activities",
            "Testing two activities in event",
            20,
            false,
            eventDate.format(DATE_FORMAT),
            "13:00",
            eventDate.format(DATE_FORMAT),
            "18:00",
            44.8176,
            20.4569
        );

        createEventPage.goNext();
        
        createEventPage.goNext();

        createEventPage.activitiesData().addActivity(
            "Opening Ceremony",
            "Kick-off the event",
            "Main Hall",
            eventDate.format(DATE_FORMAT),
            "13:30",
            eventDate.format(DATE_FORMAT),
            "17:30"
        );

        createEventPage.activitiesData().addActivity(
            "Workshop",
            "Hands-on session",
            "Room 101",
            eventDate.format(DATE_FORMAT),
            "14:00",
            eventDate.format(DATE_FORMAT),
            "15:00"
        );
        
        createEventPage.activitiesData().waitForActivityCount(2);
        assertEquals(2, createEventPage.activitiesData().getActivityCount(), 
        		"Expected 2 activities to be displayed.");
        
        createEventPage.confirmEvent();

        Snackbar snackbar = new Snackbar(driver);
        String actualMessage = snackbar.getMessage();
        assertEquals("The creation of an event succeeded.", actualMessage);
    }
    
    @Test
    void shouldAddAndRemoveActivity() throws Exception {
        loginAsOrganizer();
        driver.get(baseUrl + "/events/creation");

        CreateEventPage createEventPage = new CreateEventPage(driver);
        Thread.sleep(500);

        LocalDate eventDate = LocalDate.now().plusDays(10);

        createEventPage.mandatoryData().fillMandatoryData(
            "Event With Removable Activity",
            "Testing add/remove activity",
            10,
            false,
            eventDate.format(DATE_FORMAT),
            "13:00",
            eventDate.format(DATE_FORMAT),
            "16:00",
            44.8176,
            20.4569
        );

        createEventPage.goNext();
        createEventPage.goNext();

        createEventPage.activitiesData().addActivity(
            "Temporary Activity",
            "This will be removed",
            "Hall A",
            eventDate.format(DATE_FORMAT),
            "13:30",
            eventDate.format(DATE_FORMAT),
            "14:30"
        );

        createEventPage.activitiesData().waitForActivityCount(1);
        assertEquals(1, createEventPage.activitiesData().getActivityCount(), "Expected 1 activity to be displayed");

        createEventPage.activitiesData().removeFirstActivity();
        assertEquals(0, createEventPage.activitiesData().getActivityCount(), "Expected no activities after removal");
        
        createEventPage.confirmEvent();

        Snackbar snackbar = new Snackbar(driver);
        String actualMessage = snackbar.getMessage();
        assertEquals("The creation of an event succeeded.", actualMessage);
    }
    
    @Test
    void shouldIncreaseEventCountAfterCreation() throws Exception {
        loginAsOrganizer();
        driver.get(baseUrl + "/events/all");
        EventsPage eventsPage = new EventsPage(driver);
        //getting total event count across all pages
        int initialEventCount = eventsPage.getTotalEventCount();

        NavbarPage navbar = new NavbarPage(driver);
        navbar.openSideProfile();

        SideProfilePage sideProfile = new SideProfilePage(driver);
        sideProfile.clickCreateEvent();

        CreateEventPage createEventPage = new CreateEventPage(driver);
        LocalDate eventDate = LocalDate.now().plusDays(7);

        createEventPage.mandatoryData().fillMandatoryData(
            "Test Event for Count",
            "Event to verify count increase",
            10,
            false,
            eventDate.format(DATE_FORMAT),
            "13:00",
            eventDate.format(DATE_FORMAT),
            "18:00",
            44.8176,
            20.4569
        );

        createEventPage.goNext();
        createEventPage.goNext();
        createEventPage.confirmEvent();

        Snackbar snackbar = new Snackbar(driver);
        String message = snackbar.getMessage();
        assertEquals("The creation of an event succeeded.", message);

        
        driver.get(baseUrl + "/events/all");
        EventsPage refreshedEventsPage = new EventsPage(driver);
        int updatedEventCount = refreshedEventsPage.getTotalEventCount();

        assertEquals(initialEventCount + 1, updatedEventCount, "Event count should increase by 1 after creation");
    }
    
    @Test
    void shouldOpenEventDetailsAfterCreation() throws Exception {
        loginAsOrganizer();
        driver.get(baseUrl + "/events/all");

        EventsPage eventsPage = new EventsPage(driver);
        int initialEventCount = eventsPage.getTotalEventCount();

        NavbarPage navbar = new NavbarPage(driver);
        navbar.openSideProfile();

        SideProfilePage sideProfile = new SideProfilePage(driver);
        sideProfile.clickCreateEvent();

        CreateEventPage createEventPage = new CreateEventPage(driver);
        LocalDate eventDate = LocalDate.now().plusDays(7);

        String eventTitle = "Test Event " + System.currentTimeMillis();

        createEventPage.mandatoryData().fillMandatoryData(
            eventTitle,
            "Event to verify opening details page",
            10,
            false,
            eventDate.format(DATE_FORMAT),
            "13:00",
            eventDate.format(DATE_FORMAT),
            "18:00",
            44.8176,
            20.4569
        );

        createEventPage.goNext();
        createEventPage.goNext();
        createEventPage.confirmEvent();

        Snackbar snackbar = new Snackbar(driver);
        String message = snackbar.getMessage();
        assertEquals("The creation of an event succeeded.", message);

        // Refresh and go to events list
        driver.get(baseUrl + "/events/all");
        EventsPage refreshedEventsPage = new EventsPage(driver);

        // Click on the newly created event
        refreshedEventsPage.clickEventByTitle(eventTitle);

        // Now verify that EventDetailsPage is loaded
        EventDetailsPage detailsPage = new EventDetailsPage(driver);
        assertEquals(eventTitle, detailsPage.getEventTitle(), "Opened event details should match the created event title");
    }
    
    @Test
    void shouldGeneratePdfWithCorrectEventDetails() throws Exception {
        // 1️⃣ Login
        loginAsOrganizer();
        driver.get(baseUrl + "/events/creation");

        // 2️⃣ Create event with random title
        String eventTitle = "Test Event " + System.currentTimeMillis();

        CreateEventPage createEventPage = new CreateEventPage(driver);
        LocalDate eventDate = LocalDate.now().plusDays(7);

        createEventPage.mandatoryData().fillMandatoryData(
            eventTitle,
            "This is a PDF test description",
            10,
            false,
            eventDate.format(DATE_FORMAT),
            "13:00",
            eventDate.format(DATE_FORMAT),
            "18:00",
            44.8176,
            20.4569
        );

        createEventPage.goNext();
        createEventPage.goNext();
        createEventPage.confirmEvent();

        // 3️⃣ Open details page
        driver.get(baseUrl + "/events/all");
        EventsPage eventsPage = new EventsPage(driver);
        eventsPage.clickEventByTitle(eventTitle);
        EventDetailsPage detailsPage = new EventDetailsPage(driver);

        // 4️⃣ Click PDF button
        detailsPage.clickGeneratePdfButton();

        // 5️⃣ Wait for PDF download dynamically
        Path downloadedPdf = null;
        int attempts = 0;
        while (attempts < 20) { // wait up to 10 seconds
            List<Path> pdfFiles = Files.list(downloadDir)
                                       .filter(p -> p.toString().endsWith(".pdf"))
                                       .toList();
            if (!pdfFiles.isEmpty()) {
                downloadedPdf = pdfFiles.get(0); // take the first PDF found
                break;
            }
            Thread.sleep(500);
            attempts++;
        }
        assertTrue(downloadedPdf != null, "PDF should be downloaded");

        // 6️⃣ Wait until file is fully written (size stabilizes)
        long previousSize = -1;
        while (true) {
            long size = Files.size(downloadedPdf);
            if (size == previousSize && size > 0) break;
            previousSize = size;
            Thread.sleep(200);
        }

        // 7️⃣ Read PDF
        String pdfText = PdfUtils.getPdfText(downloadedPdf.toFile());

        // 8️⃣ Normalize text and assert content
        String normalizedText = pdfText.replaceAll("\\s+", " ").trim();
        assertTrue(normalizedText.contains(eventTitle), "PDF should contain event title");
        assertTrue(normalizedText.contains("This is a PDF test description"), "PDF should contain description");
        
        // Optional: print PDF content for debugging
        System.out.println("PDF Content: " + normalizedText);
    }
    
    @Test
    void shouldNotAllowNextWhenMandatoryFieldsInvalid() throws Exception {
        // 1️⃣ Login
        loginAsOrganizer();
        driver.get(baseUrl + "/events/creation");

        CreateEventPage createEventPage = new CreateEventPage(driver);

        // 2️⃣ Fill invalid or incomplete data (leave mandatory fields empty or invalid)
        LocalDate eventDate = LocalDate.now().plusDays(7);
        createEventPage.mandatoryData().fillMandatoryData(
            "",                        // empty name (invalid)
            "Short",                   // description too short or invalid
            -5,                        // invalid number of attendees
            false,
            eventDate.format(DATE_FORMAT),
            "13:00",
            eventDate.format(DATE_FORMAT),
            "18:00",
            44.8176,
            20.4569
        );

        createEventPage.goNext();
        
        // 3️⃣ Try to click next
        // 4️⃣ Assert that Budget page elements are NOT present
        // Assuming your Budget page has a unique element, e.g., an element with id="budget-form"
        boolean isBudgetVisible = createEventPage.budgetData().isBudgetFormVisible();
        assertFalse(isBudgetVisible, "Budget page should not be visible if mandatory fields are invalid");

        // 5️⃣ Optional: check validation messages
        List<String> validationMessages = createEventPage.mandatoryData().getValidationMessages();
        assertTrue(validationMessages.stream().anyMatch(msg -> msg.contains("Name has to between 5-50 characters.")), "Should show validation for name");
        assertTrue(validationMessages.stream().anyMatch(msg -> msg.contains("Number of attendees can't be less than 0.")), "Should show validation for attendees");
    }
}
