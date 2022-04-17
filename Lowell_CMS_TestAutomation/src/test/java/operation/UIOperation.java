package operation;

import java.io.Console;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;

import utilities.SikuliChecks;
import utilities.Utility;

public class UIOperation {

	public WebDriver driver;
	public ExtentTest logging;
	public boolean flag;
	public boolean flag1;
	public boolean flag2;
	public boolean flag3;
	public boolean flag4 = false;
	public String result = null;
	public int response;
	public WebElement wb;
	public int tabCnt;
	public String environment = null;
	public String testtype = null;
	public String reqUrl = null;
	public String issueID = null;
	public String defectSummary = null;
	public String defectDescription = null;
	String val1 = null;
	String val2 = null;

	Utility util;
	SikuliChecks skli;

	public UIOperation(WebDriver driver, ExtentTest child) {
		this.driver = driver;
		this.logging=child;
	}

	public String perform(String capture, String browser, String environment, String type, String url, Properties p,
			String operation, String objectName, String objectType, String value)
			throws Exception {
		util = new Utility(driver,logging);
		skli = new SikuliChecks(driver,logging);		
		switch (operation.toUpperCase()) {		

		case "NAVIGATE":
			try {
				url = url + value;
				System.out.println("URL found is: " + url);
				driver.navigate().to(url);
				driver.navigate().refresh();
				WebElement acceptCookies = driver.findElement(By.xpath("//*[@class='btn-accept-all-cookie cta cta--primary']"));
				if(acceptCookies.isDisplayed()){
					acceptCookies.click();
				}else {
					System.out.println("Cookiee pop-up not available!");
				}
				//Thread.sleep(5000);
				if (url.equals(driver.getCurrentUrl())) {
					result = "Pass";
				} else
					result = "Fail";
			} catch (Exception e) {
				System.out.println("Problem in Navigating to: " + url + e);
				result = "Fail";
			}
			break;

		case "SIKULI":
			try {
				// Perform click
				//WebElement eleClick = driver.findElement(this.getObject(p, objectName, objectType));
				//flag = eleClick.isDisplayed();
				//String value;
				if (value != null) {
					//util.highlightElement(eleClick);
					//eleClick.click();
					result = skli.CompareUI(value);
				} else
					result = "Fail";
			} catch (Exception e) {
				System.out.println("Problem in finding Sikuli Image: " + e);
				result = "Fail";
			}
			break;

		case "CLICK":
			try {
				// Perform click
				WebElement eleClick = driver.findElement(this.getObject(p, objectName, objectType));
				flag = eleClick.isDisplayed();
				if (flag = true) {
					util.highlightElement(eleClick);
					eleClick.click();
					result = "Pass";
				} else
					result = "Fail";
			} catch (Exception e) {
				System.out.println("Problem in Clicking: " + e);
				result = "Fail";
			}
			break;

		case "WAIT":
			try {
				Thread.sleep(3000);
				System.out.println("Waiting Success!!!");
				result = "Pass";
			} catch (Exception e) {
				System.out.println("Waiting Exception...: " + e.getStackTrace().toString());
				result = "Fail";
			}
			break;
			
		

		case "SETTEXT":
			try {
				// Set text on control
				WebElement eleSetval = driver.findElement(this.getObject(p, objectName, objectType));
				value = value.replaceAll("\"", "");
				flag = eleSetval.isDisplayed();
				if (flag = true) {
					util.highlightElement(eleSetval);
					eleSetval.clear();
					eleSetval.sendKeys(value);
					result = "Pass";
				} else
					result = "Fail";

			} catch (Exception e) {
				System.out.println("Problem in Setting Text: " + e);
				result = "Fail";
			}
			break;		

		case "GETTEXT":
			try {
				// Get text of an element
				WebElement eleGettxt = driver.findElement(this.getObject(p, objectName, objectType));
				flag = eleGettxt.isDisplayed();
				System.out.println(eleGettxt.getText());
				if (flag = true) {
					util.highlightElement(eleGettxt);
					if (eleGettxt.getText().contains(value)) {
						result = "Pass";
					} else
						result = "Fail";
				}
			} catch (Exception e) {
				System.out.println("Element not found ..." + e);
				result = "Fail";
			}
			break;
			
		case "GETCONTENT":
			try {
				// Get Content of a page or screen
				value =util.ignoreQuotes(value);
				WebElement content = driver.findElement(By.xpath("//*[contains(text(),'" + value + "')]"));
				//Assert.assertTrue("Text not found!", content.size() > 0);		
				//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", content);
				//get position
//				int x = content.getLocation().getX();
//				int y = content.getLocation().getY();
//
//				//scroll to x y 
//				JavascriptExecutor js = (JavascriptExecutor) driver;
//				js.executeScript("window.scrollBy(" +x +", " +y +")");
				
//				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", content);
//				WebDriverWait wait = new WebDriverWait(driver, 5);
//				wait.until(ExpectedConditions.visibilityOf(content));
				
				// Actions class with moveToElement()
//			      Actions a = new Actions(driver);
//			      a.moveToElement(content);
//			      a.perform();
				
				flag = content.isDisplayed();
				System.out.println(content.getText());
				if (flag = true) {
					util.highlightElement(content);
					if (content.getText().contains(value) || content.getText().contains(value.toUpperCase())) {
						result = "Pass";
					} else
						result = "Fail";
				}
			} catch (Exception e) {
				System.out.println("Content not found in the page or screen!" + e);
				result = "Fail";
			}
			break;
			
		case "CHKLINKS":
			try {
				// Check all the links available in the page
				result = util.linkcheck(url, value);
					
			} catch (Exception e) {
				System.out.println("Element - link not found ..." + e);
				result = "Fail";
			}
			break;
			
		case "CHKIMGS":
			try {
				// Check all the links available in the page
				result = util.imageCheck(url, value);
					
			} catch (Exception e) {
				System.out.println("Element - image not found ..." + e);
				result = "Fail";
			}
			break;
		case "LINKTEXT":
			try {
				// Get LINK Name from a page or screen
				WebElement linkName= driver.findElement(By.linkText(value));			
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", linkName);
				flag = linkName.isDisplayed();
				System.out.println(linkName.getText());
				if (flag = true) {
					util.highlightElement(linkName);
					if (linkName.getText().contains(value)) {
						result = "Pass";
					} else
						result = "Fail";
				}
			} catch (Exception e) {
				System.out.println("Content not found in the page or screen!" + e);
				result = "Fail";
			}
			break;
		case "SCROLL":
			try {
				WebElement element = driver.findElement(this.getObject(p, objectName, objectType));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].scrollIntoView();", element);
				result = "Pass";
			} catch (Exception e) {
				result = "Fail";
			}
			break;
		case "SCROLDOWN":
			try {
				//WebElement element = driver.findElement(this.getObject(p, objectName, objectType));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				//executor.executeScript("arguments[0].scrollIntoView();", element);
				executor.executeScript("window.scrollBy(0,420)","");
				result = "Pass";
			} catch (Exception e) {
				result = "Fail";
			}
			break;

		case "REFRESH":
			try {
				driver.navigate().refresh();
				result = "Pass";
			} catch (Exception e) {
				result = "Fail";
			}
			break;

		case "SWITCHWINDOW":
			try {
				Set<String> wndhdl = driver.getWindowHandles();
				Iterator<String> it = wndhdl.iterator();
				List<String> list = new ArrayList<String>();
				while (it.hasNext()) {
					list.add(it.next());
				}
				driver.switchTo().window((String) list.get(Integer.parseInt(value)));
				result = "Pass";
			} catch (Exception e) {
				result = "Fail";
				System.out.println(e);
			}
			break;

		default:
			break;
		}
		return result;
	}

	/**
	 * Find element BY using object type and value
	 * 
	 * @param p
	 * @param objectName
	 * @param objectType
	 * @return
	 * @throws Exception
	 */
	private By getObject(Properties p, String objectName, String objectType) throws Exception {
		// Find by xpath
		if (objectType.equalsIgnoreCase("XPATH")) {

			return By.xpath(p.getProperty(objectName));
		}
		// find by ID
		else if (objectType.equalsIgnoreCase("ID")) {

			return By.id(p.getProperty(objectName));

		}
		// find by class
		else if (objectType.equalsIgnoreCase("CLASSNAME")) {

			return By.className(p.getProperty(objectName));

		}
		// find by name
		else if (objectType.equalsIgnoreCase("NAME")) {

			return By.name(p.getProperty(objectName));

		}
		// Find by css
		else if (objectType.equalsIgnoreCase("CSS")) {

			return By.cssSelector(p.getProperty(objectName));

		}
		// find by link
		else if (objectType.equalsIgnoreCase("LINK")) {

			return By.linkText(p.getProperty(objectName));

		}
		// find by partial link
		else if (objectType.equalsIgnoreCase("PARTIALLINK")) {

			return By.partialLinkText(p.getProperty(objectName));

		} else {
			throw new Exception("Wrong object type");
		}
	}

	public void alertHandler() {
		try {
			// Switching to Alert
			Alert alert = driver.switchTo().alert();
			// Capturing alert message.
			String alertMessage = driver.switchTo().alert().getText();
			// Displaying alert message
			System.out.println(alertMessage);
			alert.dismiss();
		} catch (Exception e) {
			System.out.println("Alert error..." + e);
		}
	}

}
