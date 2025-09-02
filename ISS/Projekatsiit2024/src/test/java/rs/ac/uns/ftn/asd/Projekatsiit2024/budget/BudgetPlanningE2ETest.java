package rs.ac.uns.ftn.asd.Projekatsiit2024.budget;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import io.github.bonigarcia.wdm.WebDriverManager;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
public class BudgetPlanningE2ETest {

    private WebDriver driver;

    @Autowired
    private EventRepository eventRepository;
    
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
    
    @Test
    @Order(1)
    public void testAddBudgetItem_Success() throws Exception{
    	driver.get("http://localhost:4200/authentication/login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    	wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("email"))));
        
    	//enter login data
    	driver.findElement(By.id("email")).sendKeys("organizer2@example.com");
    	driver.findElement(By.id("password")).sendKeys("pass1234");
    	driver.findElement(By.id("submitButton")).click();
    	Thread.sleep(1000);
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("events")));
    	
    	//select event
    	driver.findElement(By.id("events")).click();
    	Thread.sleep(1000);
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("app-event-card[ng-reflect-title=\"Winter Wonderlandsdafasdfasdfs\"]")));
    	driver.findElement(By.cssSelector("app-event-card[ng-reflect-title=\"Winter Wonderlandsdafasdfasdfs\"]")).click();
    	Thread.sleep(1000);
    	
    	//go to budget page
    	driver.findElement(By.xpath("//button[.//span[text()='Event budget']]")).click();
    	Thread.sleep(1000);
    	
    	//add Catering to budget
    	driver.findElement(By.xpath("//span[text()='Catering']/../button")).click();
    	Thread.sleep(1000);
    	
    	//check if Catering moved
    	assertTrue(driver.findElement(By.xpath("//mat-tree-node/div/span[text()='Catering']")).isDisplayed());
    	
    	//change max budget
    	driver.findElement(By.xpath("//mat-tree-node[.//span[text()='Catering']]//input[@type='number']")).sendKeys("500");
    	Thread.sleep(1000);
    	
    	//check if max budget text changed on unfocus
    	driver.findElement(By.xpath("//body")).click();
    	Thread.sleep(500);
    	assertTrue(driver.findElement(By.xpath("//span[text()='Max Budget: 500€']")).isDisplayed());
    }
    
    @Test
    @Order(2)
    public void testBuyProducts_Success() throws Exception{
    	driver.get("http://localhost:4200/authentication/login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    	wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("email"))));

    	//enter login data
    	driver.findElement(By.id("email")).sendKeys("organizer2@example.com");
    	driver.findElement(By.id("password")).sendKeys("pass1234");
    	driver.findElement(By.id("submitButton")).click();
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("offerings")));
    	Thread.sleep(1000);
    	
    	//Select speakers
    	driver.findElement(By.id("offerings")).click();
    	Thread.sleep(1000);
    	driver.findElement(By.xpath("//app-offer-card[.//h4[text()='JBL Speaker']]")).click();
    	Thread.sleep(1000);
    	
    	//Open reservation dialogue
    	driver.findElement(By.xpath("//button[.//span[text()='Buy product']]")).click();
    	Thread.sleep(1000);
    	driver.findElement(By.id("eventSelect")).click();
    	Thread.sleep(1000);
    	
    	//Buy speakers
    	Select eventSelect = new Select(driver.findElement(By.id("eventSelect")));
    	eventSelect.selectByVisibleText("Winter Wonderlandsdafasdfasdfsdafdsa");
    	Thread.sleep(1000);
    	driver.findElement(By.xpath("//button[@type='submit']")).click();
    	Thread.sleep(1000);
    	
    	//Select silverware
    	driver.findElement(By.id("offerings")).click();
    	Thread.sleep(1000);
    	driver.findElement(By.xpath("//app-offer-card[.//h4[text()='Silver plates']]")).click();
    	Thread.sleep(1000);
    	
    	//Open reservation dialogue
    	driver.findElement(By.xpath("//button[.//span[text()='Buy product']]")).click();
    	Thread.sleep(1000);
    	driver.findElement(By.id("eventSelect")).click();
    	Thread.sleep(1000);
    	
    	//Buy speakers
    	eventSelect = new Select(driver.findElement(By.id("eventSelect")));
    	eventSelect.selectByVisibleText("Winter Wonderlandsdafasdfasdfsdafdsa");
    	Thread.sleep(1000);
    	driver.findElement(By.xpath("//button[@type='submit']")).click();
    	Thread.sleep(1000);
    	
    	// CHECK IF THE BUDGET PAGE CHANGED
    	//select event
    	driver.findElement(By.id("events")).click();
    	Thread.sleep(1000);
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("app-event-card[ng-reflect-title=\"Winter Wonderlandsdafasdfasdfs\"]")));
    	driver.findElement(By.cssSelector("app-event-card[ng-reflect-title=\"Winter Wonderlandsdafasdfasdfs\"]")).click();
    	Thread.sleep(1000);
    	
    	//go to budget page
    	driver.findElement(By.xpath("//button[.//span[text()='Event budget']]")).click();
    	Thread.sleep(1000);

    	driver.findElement(By.xpath("//div[./span[text()='Picnic']]//button")).click();
    	driver.findElement(By.xpath("//div[./span[text()='Catering']]//button")).click();
    	Thread.sleep(1000);
    }

    @Test
    @Order(3)
    public void testEditItems() throws Exception{
    	driver.get("http://localhost:4200/authentication/login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    	wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("email"))));
        
    	//enter login data
    	driver.findElement(By.id("email")).sendKeys("organizer2@example.com");
    	driver.findElement(By.id("password")).sendKeys("pass1234");
    	driver.findElement(By.id("submitButton")).click();
    	Thread.sleep(1000);
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("events")));
    	
    	//select event
    	driver.findElement(By.id("events")).click();
    	Thread.sleep(1000);
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("app-event-card[ng-reflect-title=\"Winter Wonderlandsdafasdfasdfs\"]")));
    	driver.findElement(By.cssSelector("app-event-card[ng-reflect-title=\"Winter Wonderlandsdafasdfasdfs\"]")).click();
    	Thread.sleep(1000);
    	
    	//go to budget page
    	driver.findElement(By.xpath("//button[.//span[text()='Event budget']]")).click();
    	Thread.sleep(1000);
    	
    	//Check max budget
    	assertTrue(driver.findElement(By.xpath("//span[text()='Max Budget: 500€']")).isDisplayed());

    	//Try setting negative price - shouldn't change
    	WebElement cateringInput = driver.findElement(By.xpath("//mat-tree-node[.//span[text()='Catering']]//input[@type='number']"));
    	cateringInput.sendKeys(Keys.CONTROL + "a");
    	cateringInput.sendKeys(Keys.DELETE);
    	cateringInput.sendKeys("-500");
    	driver.findElement(By.xpath("//body")).click();
    	Thread.sleep(500);
    	assertTrue(driver.findElement(By.xpath("//span[text()='Max Budget: 500€']")).isDisplayed());
    	
    	//Try setting price lower than spent money - shouldn't change
    	cateringInput = driver.findElement(By.xpath("//mat-tree-node[.//span[text()='Catering']]//input[@type='number']"));
    	cateringInput.sendKeys(Keys.CONTROL + "a");
    	cateringInput.sendKeys(Keys.DELETE);
    	cateringInput.sendKeys("10");
    	driver.findElement(By.xpath("//body")).click();
    	Thread.sleep(500);
    	assertTrue(driver.findElement(By.xpath("//span[text()='Max Budget: 500€']")).isDisplayed());
    	
    	//See if the max budget is the sum of all budgets
    	driver.findElement(By.xpath("//mat-tree-node[.//span[text()='Picnic']]//input[@type='number']")).clear();
    	Thread.sleep(500);
    	driver.findElement(By.xpath("//mat-tree-node[.//span[text()='Picnic']]//input[@type='number']")).sendKeys("300");
    	Thread.sleep(1000);
    	driver.findElement(By.xpath("//body")).click();
    	Thread.sleep(500);
    	assertTrue(driver.findElement(By.xpath("//span[contains(text(),'Max Budget: 800')]")).isDisplayed());
    }
    
    @Test
    @Order(4)
    public void testDeleteItems() throws Exception{
    	driver.get("http://localhost:4200/authentication/login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    	wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("email"))));
        
    	//enter login data
    	driver.findElement(By.id("email")).sendKeys("organizer2@example.com");
    	driver.findElement(By.id("password")).sendKeys("pass1234");
    	driver.findElement(By.id("submitButton")).click();
    	Thread.sleep(1000);
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("events")));
    	
    	//select event
    	driver.findElement(By.id("events")).click();
    	Thread.sleep(1000);
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("app-event-card[ng-reflect-title=\"Winter Wonderlandsdafasdfasdfs\"]")));
    	driver.findElement(By.cssSelector("app-event-card[ng-reflect-title=\"Winter Wonderlandsdafasdfasdfs\"]")).click();
    	Thread.sleep(1000);
    	
    	//go to budget page
    	driver.findElement(By.xpath("//button[.//span[text()='Event budget']]")).click();
    	Thread.sleep(1000);

    	driver.findElement(By.xpath("//div[./span[text()='Picnic']]//button")).click();
    	Thread.sleep(1000);
    	driver.findElement(By.xpath("//div[./span[text()='Catering']]//button")).click();
    	
    	//check used budget
    	assertTrue(driver.findElement(By.xpath("//span[contains(text(), 'Used Budget: 54')]")).isDisplayed());
    	//delete product and check again
    	driver.findElement(By.xpath("//div[./span[text()='JBL Speaker']]//button")).click();
    	Thread.sleep(1000);
    	
    	assertThrows(NoSuchElementException.class, () -> driver.findElement(By.xpath("//div[./span[text()='JBL Speaker']]//button")));
    	assertTrue(driver.findElement(By.xpath("//span[contains(text(), 'Used Budget: 14')]")).isDisplayed());
    	
    	//delete category and check again
    	driver.findElement(By.xpath("//div[./span[text()='Picnic']]//button")).click();
    	Thread.sleep(1000);
    	assertThrows(NoSuchElementException.class, () -> driver.findElement(By.xpath("//div[./span[text()='Taken offer categories']]//span[text()='Picnic']")));
    	Thread.sleep(1000);

    	//Check max budget
    	assertTrue(driver.findElement(By.xpath("//span[text()='Max Budget: 500€']")).isDisplayed());
    	Thread.sleep(1000);
    }
}
