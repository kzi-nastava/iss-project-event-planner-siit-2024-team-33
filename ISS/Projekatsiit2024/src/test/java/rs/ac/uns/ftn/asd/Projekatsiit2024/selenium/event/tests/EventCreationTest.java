package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.tests;

import org.junit.jupiter.api.Test;

import rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.BaseSeleniumTest;
import rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages.CreateEventPage;

public class EventCreationTest extends BaseSeleniumTest {
	
    private String baseUrl = "http://localhost:4200";
    
    @Test
    public void testPublicEventCreation() throws Exception {
    	loginAsOrganizer();

        driver.get(baseUrl + "/events/creation");

        CreateEventPage createEventPage = new CreateEventPage(driver);
        
        Thread.sleep(500);

        createEventPage.mandatoryData().fillMandatoryData(
        	    "Public Event",
        	    "This is a test public event",
        	    10,
        	    false,
        	    "2025-09-15",
        	    "13:00",
        	    "2025-09-15",
        	    "14:00",
        	    44.8176,
        	    20.4569
        );

        createEventPage.goNext();

        createEventPage.goNext();

        createEventPage.confirmEvent();
    }
}
