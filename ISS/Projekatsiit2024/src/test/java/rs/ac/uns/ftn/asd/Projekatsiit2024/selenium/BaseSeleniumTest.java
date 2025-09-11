package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
	protected Path downloadDir;

    @BeforeEach
    void setUp() throws IOException {
        //automatically downloads correct ChromeDriver version
        WebDriverManager.chromedriver().setup();
        
        downloadDir = Files.createTempDirectory("pdf-downloads");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadDir.toAbsolutePath().toString());
        prefs.put("plugins.always_open_pdf_externally", true);
        
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
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
