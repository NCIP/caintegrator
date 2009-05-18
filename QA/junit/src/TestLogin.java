import junit.framework.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.RenderedWebElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestLogin extends TestCase{

	public void testManagerUSIDManagerPW() {
	
		WebDriver driver = new FirefoxDriver();
		driver.get("http://cbvapp-q1015.nci.nih.gov:19080/caintegrator2/login.action");                
		
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
		String getCurrentUrl1 = driver.getCurrentUrl();
		assertEquals(getCurrentUrl1, "http://cbvapp-q1015.nci.nih.gov:19080/caintegrator2/workspace.action");
		
		//Test to see if right userid
		WebElement queryiduser = driver.findElement(By.id("user"));        
		System.out.println(queryiduser.getText());
		assertEquals(queryiduser.getText(), "Welcome, manager | Logout");
		driver.close();
	}
	
	
	
}
