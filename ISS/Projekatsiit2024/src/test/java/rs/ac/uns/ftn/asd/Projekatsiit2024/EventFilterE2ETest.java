package rs.ac.uns.ftn.asd.Projekatsiit2024;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;

public class EventFilterE2ETest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testEventFilter() {
        driver.get("http://localhost:4200"); // Angular dev URL

        
        driver.findElement(By.cssSelector(".open-filter-button")).click();

        
        WebElement categoryInput = driver.findElement(By.cssSelector("input[placeholder='Input Category']"));
        categoryInput.sendKeys("Music");

        
        driver.findElement(By.cssSelector("input[value='available']")).click();

        
        driver.findElement(By.cssSelector("button.confirm")).click();

        
        WebElement results = driver.findElement(By.cssSelector(".search-results"));
        Assertions.assertTrue(results.getText().contains("Music"));
    }
    
}
