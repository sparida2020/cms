package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;
import org.sikuli.script.Pattern;
import com.aventstack.extentreports.ExtentTest;

import operation.Config;


public class SikuliChecks {
	public WebDriver driver;
	public ExtentTest log;
	public Screen s=null;
	public Pattern p=null;
	public String result=null;
	
	public SikuliChecks(WebDriver driver, ExtentTest child) {
		this.driver = driver;
		this.log = child;	
	}
	
	public String CompareUI(String imageName)
	{
		s = new Screen();		
		String path = Config.SKLI_PATH+imageName.toLowerCase()+".PNG";
		System.out.println(path);
		p = new Pattern(path);
		
		try {
			//s.wait(p);
			s.find(p);
			s.getLastMatch().highlight(3, "blue");
			//s.click(p);
			result="pass";
		} catch (Exception e) {
			e.printStackTrace();
			result="fail";
		}
		return result;		
	}
}
