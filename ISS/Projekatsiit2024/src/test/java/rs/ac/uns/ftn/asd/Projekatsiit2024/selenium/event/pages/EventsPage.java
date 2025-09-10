package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.event.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EventsPage {
	
	private WebDriver driver;
    private WebDriverWait wait;

    private final By eventCardsLocator = By.cssSelector("app-event-card");
    private final By nextButtonLocator = By.xpath("//div[@class='paginator']//button[contains(text(),'Next')]");
    private final By previousButtonLocator = By.xpath("//div[@class='paginator']//button[contains(text(),'Previous')]");
    private final By pageNumberButtonsLocator = By.cssSelector(".paginator button:not([disabled])");

    public EventsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /** Returns event cards currently visible on the page */
    public List<WebElement> getEventCards() {
        return driver.findElements(eventCardsLocator);
    }

    /** Returns the count of event cards on the current page */
    public int getEventCountOnCurrentPage() {
        return getEventCards().size();
    }

    /** Go to next page if available */
    public boolean goToNextPage() {
        List<WebElement> nextButtons = driver.findElements(nextButtonLocator);
        if (!nextButtons.isEmpty() && nextButtons.get(0).isEnabled()) {
            int currentPage = getCurrentPageNumber();
            nextButtons.get(0).click();
            waitUntilPageChanges(currentPage);
            waitForEventsToLoad();
            return true;
        }
        return false;
    }

    /** Go to previous page if available */
    public boolean goToPreviousPage() {
        List<WebElement> prevButtons = driver.findElements(previousButtonLocator);
        if (!prevButtons.isEmpty() && prevButtons.get(0).isEnabled()) {
            int currentPage = getCurrentPageNumber();
            prevButtons.get(0).click();
            waitUntilPageChanges(currentPage);
            waitForEventsToLoad();
            return true;
        }
        return false;
    }

    /** Go to the first page */
    public void goToFirstPage() {
        while (goToPreviousPage()) {
            // keep clicking until at first page
        }
    }

    /** Wait for event cards to be present */
    private void waitForEventsToLoad() {
        wait.until(driver -> driver.findElements(eventCardsLocator).size() >= 0);
    }

    /** Wait until the page number changes */
    private void waitUntilPageChanges(int oldPageNumber) {
        wait.until(driver -> getCurrentPageNumber() != oldPageNumber);
    }

    /** Get currently active page number */
    private int getCurrentPageNumber() {
        List<WebElement> pages = driver.findElements(pageNumberButtonsLocator);
        for (WebElement page : pages) {
            if (page.getAttribute("class").contains("active")) {
                try {
                    return Integer.parseInt(page.getText().trim());
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    /** Count all events across all pages */
    public int getTotalEventCount() {
        int total = 0;

        goToFirstPage();

        boolean hasNext = true;
        while (hasNext) {
            total += getEventCountOnCurrentPage();
            hasNext = goToNextPage();
        }

        return total;
    }
    
    public void clickEventByTitle(String title) {
        goToFirstPage(); // start from first page

        boolean hasNext = true;
        while (hasNext) {
            for (WebElement card : getEventCards()) {
                String cardTitle = card.findElement(By.cssSelector(".event-title")).getText().trim();
                if (cardTitle.equals(title)) {
                    card.click();
                    return;
                }
            }
            hasNext = goToNextPage();
        }

        throw new RuntimeException("Event with title '" + title + "' not found.");
    }
}
