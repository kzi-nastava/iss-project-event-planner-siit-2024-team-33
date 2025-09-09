package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;

class AuthResponse {
    private String accessToken;
    private int expiresIn;

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public int getExpiresIn() { return expiresIn; }
    public void setExpiresIn(int expiresIn) { this.expiresIn = expiresIn; }
}

public class BaseSeleniumTest {
	
	protected WebDriver driver;
	protected String baseUrl = "http://localhost:4200";

    @BeforeEach
    void setUp() {
        //automatically downloads correct ChromeDriver version
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    protected void loginAsOrganizer() throws Exception {
        String loginUrl = "http://localhost:8080/api/auth/login";
        String email = "organizer@example.com";
        String password = "pass1234";

        String jsonBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);

        URL url = new URL(loginUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes());
            os.flush();
        }

        InputStream is = conn.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        AuthResponse response = mapper.readValue(is, AuthResponse.class);

        // Otvori baznu stranicu da bi window.localStorage bio dostupan
        driver.get(baseUrl);

        // Postavi JWT u localStorage
        ((JavascriptExecutor) driver).executeScript(
                String.format("window.localStorage.setItem('user', '%s');", response.getAccessToken())
        );
    }
}
