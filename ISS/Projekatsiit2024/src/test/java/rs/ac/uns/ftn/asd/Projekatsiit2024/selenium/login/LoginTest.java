package rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.BaseSeleniumTest;
import rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.NavbarPage;
import rs.ac.uns.ftn.asd.Projekatsiit2024.selenium.SideProfilePage;

public class LoginTest extends BaseSeleniumTest {
	
	private String baseUrl = "http://localhost:4200";
	
	private NavbarPage navbar;
	private SideProfilePage sideProfile;
	private LoginPage loginPage;
	
	@Test
	void loginWithValidCredentials_ShouldRedirectToHome() throws InterruptedException {
		driver.get(baseUrl);
		
		navbar = new NavbarPage(driver);
		navbar.openSideProfile();
		
		sideProfile = new SideProfilePage(driver);
		sideProfile.clickLogin();
		
		loginPage = new LoginPage(driver);
		loginPage.login("organizer@example.com", "pass1234");
		
		Thread.sleep(2000); //ideally replace with explicit wait
		assertTrue(driver.getCurrentUrl().startsWith(baseUrl));
	}
	
	@Test
	void loginWithInvalidCredentials_ShouldShowErrorMessage() throws InterruptedException {
		driver.get(baseUrl);
		
		navbar = new NavbarPage(driver);
		navbar.openSideProfile();
		
		sideProfile = new SideProfilePage(driver);
		sideProfile.clickLogin();
		
		loginPage = new LoginPage(driver);
		loginPage.login("wrong@example.com", "wrongpassword");
		
		Thread.sleep(1000);
		assertEquals("Invalid email or password.", loginPage.getErrorMessage());
	}
}
