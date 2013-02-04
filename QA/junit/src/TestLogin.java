/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
import junit.framework.*;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestLogin extends TestCase{

	public void testManagerUSIDManagerPW() {
		System.out.println("Date = " + new Date());
		WebDriver driver = new FirefoxDriver();
		String urlTested;
		urlTested = "http://cbvapp-q1015.nci.nih.gov:19080/caintegrator2/";
		
		System.out.println("\nTest Case = Testlogin");
		System.out.println("Test = testManagerUSIDManagerPW");
		System.out.println("Summary = log in using 'manager/manager'");
		System.out.println("Passing behavior = User successfully logs in, and is taken to the next page.");
		System.out.println("urlTested = " + urlTested);
		driver.get(urlTested);                
		WebElement queryFindj_username = driver.findElement(By.name("j_username"));        
		queryFindj_username.sendKeys("manager");        
		
		WebElement queryFindj_password = driver.findElement(By.name("j_password"));        
		queryFindj_password.sendKeys("manager");   
		
		//Successful login redirects to /caintegrator2/workspace.action
		List<WebElement> allInputs = driver.findElements(By.tagName("input"));
		for (WebElement line : allInputs) {
			if (line.getValue().equals("Login")){
				line.click();
			}
		}
		
		//Test to see if URL is correct.
		String getCurrentUrl = driver.getCurrentUrl();
		driver.close();
		assertEquals(getCurrentUrl, "http://cbvapp-q1015.nci.nih.gov:19080/caintegrator2/workspace.action");
	} // End of testManagerUSIDManagerPW()

	public void testManagerUSIDDisplayed() {
		System.out.println("Date = " + new Date());
		WebDriver driver = new FirefoxDriver();
		String urlTested;
		urlTested = "http://cbvapp-q1015.nci.nih.gov:19080/caintegrator2/";
		
		System.out.println("\nTest Case = Testlogin");
		System.out.println("Test = testManagerUSIDDisplayed");
		System.out.println("Summary = log in using 'manager': ");
		System.out.println("Passing behavior = User successfully logs in, and is taken to the next page, where user is recognized by name.");
		System.out.println("urlTested = " + urlTested);
		driver.get(urlTested);                
		WebElement queryFindj_username = driver.findElement(By.name("j_username"));        
		queryFindj_username.sendKeys("manager");        
		
		WebElement queryFindj_password = driver.findElement(By.name("j_password"));        
		queryFindj_password.sendKeys("manager");   
		
		//Successful login redirects to /caintegrator2/workspace.action
		List<WebElement> allInputs = driver.findElements(By.tagName("input"));
		for (WebElement line : allInputs) {
			if (line.getValue().equals("Login")){
				line.click();
			}
		} // End of testManagerUSIDDisplayed()
		
		//Test to see if app recognizes the right userid
		WebElement queryiduser = driver.findElement(By.id("user"));        
		String queryiduserText = queryiduser.getText();
		assertEquals(queryiduserText, "Welcome, manager | Logout");
		driver.close();
	} // End of testManagerUSIDDisplayed()
	
	public void testBlankUSID() {
		
		WebDriver driver = new FirefoxDriver();
		String urlTested;
		urlTested = "http://cbvapp-q1015.nci.nih.gov:19080/caintegrator2/";
		System.out.println("\nTest Case = Testlogin");
		System.out.println("Test = testBlankUSID");
		System.out.println("Summary = log in with blank ID");
		System.out.println("Passing behavior = if user tries to log in with a blank ID, " +
				"he should be taken to a new page. If the login page simply reloads again, with no message that the login has failed, then the Test fails.");
		System.out.println("urlTested = " + urlTested);
		driver.get(urlTested);                
		//WebElement queryFindj_username = driver.findElement(By.name("j_username"));        
		//Don't enter a USID
		//queryFindj_username.sendKeys("manager");        
		
		WebElement queryFindj_password = driver.findElement(By.name("j_password"));        
		queryFindj_password.sendKeys("manager");   
		
		//Click Login
		List<WebElement> allInputs = driver.findElements(By.tagName("input"));
		for (WebElement line : allInputs) {
			if (line.getValue().equals("Login")){
				line.click();
			}
		}
		
		//Test to see if URL has changed
		String getCurrentUrl = driver.getCurrentUrl();
		// I need to close the browser BEFORE I run the assert, because if it FAILS, the method halts.
		driver.close();
		assertFalse(getCurrentUrl.equals("http://cbvapp-q1015.nci.nih.gov:19080/caintegrator2/login.action"));
	} // End of testBlankUSID	

	public void testWrongPassword() {
		
		WebDriver driver = new FirefoxDriver();
		String urlTested;
		urlTested = "http://cbvapp-q1015.nci.nih.gov:19080/caintegrator2/";
		System.out.println("\nTest Case = Testlogin");
		System.out.println("Test = testWrongPassword");
		System.out.println("Summary = log in with wong password");
		System.out.println("Passing behavior = if user tries to log in with the wrong, " +
				"he should be taken to a new page. If the login page simply reloads again, with no message that the login has failed, then the Test fails.");
		System.out.println("urlTested = " + urlTested);
		driver.get(urlTested);                
		WebElement queryFindj_username = driver.findElement(By.name("j_username"));        
		//Enter correct USID
		queryFindj_username.sendKeys("manager");        
		
		//Enter incorrect USID
		WebElement queryFindj_password = driver.findElement(By.name("j_password"));        
		queryFindj_password.sendKeys("managggg");   
		
		//Click Login
		List<WebElement> allInputs = driver.findElements(By.tagName("input"));
		for (WebElement line : allInputs) {
			if (line.getValue().equals("Login")){
				line.click();
			}
		}
		
		//Test to see if URL has changed
		String getCurrentUrl = driver.getCurrentUrl();
		// I need to close the browser BEFORE I run the assert, because if it FAILS, the method halts.
		driver.close();
		assertFalse(getCurrentUrl.equals("http://cbvapp-q1015.nci.nih.gov:19080/caintegrator2/login.action"));
	} // End of testBlankUSID	

	public void testLogout() {
		WebDriver driver = new FirefoxDriver();
		String urlTested;
		urlTested = "http://cbvapp-q1015.nci.nih.gov:19080/caintegrator2/";
		
		System.out.println("\nTest Case = Testlogin");
		System.out.println("Test = testLogout");
		System.out.println("Summary = logout after logging in");
		System.out.println("Passing behavior = User can log out by clicking on 'Logout', and the browser is taken to the login screen.");
		System.out.println("urlTested = " + urlTested);
		driver.get(urlTested);                
		WebElement queryFindj_username = driver.findElement(By.name("j_username"));        
		queryFindj_username.sendKeys("manager");        
		
		WebElement queryFindj_password = driver.findElement(By.name("j_password"));        
		queryFindj_password.sendKeys("manager");   
		
		//User logins, and browser redirects to /caintegrator2/workspace.action
		List<WebElement> allInputs = driver.findElements(By.tagName("input"));
		for (WebElement line : allInputs) {
			if (line.getValue().equals("Login")){
				line.click();
			}
		}
			
		//Find and click the logout link
		List<WebElement> alla = driver.findElements(By.tagName("a"));
		for (int i = 1; i <= alla.size(); i++ ){
			//System.out.println("i = "  +  i);
			WebElement myWebElement = alla.get(i);
			if (myWebElement.getText().equals("Logout")){
			//if (alla.get(i).getText().equals("Logout")){
				myWebElement.click();
			//	alla.get(i).click();
				break;
			}	
		}
		
		//Test to see if URL is correct.
		String getCurrentUrl = driver.getCurrentUrl();
		driver.close();
		assertEquals(getCurrentUrl, "http://cbvapp-q1015.nci.nih.gov:19080/caintegrator2/login.action");
		
	} // End of testLogout()	

}
