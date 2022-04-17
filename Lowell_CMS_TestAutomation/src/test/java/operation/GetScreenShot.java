package operation;

import java.io.File;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

public class GetScreenShot {
	public WebDriver driver;

	public GetScreenShot(WebDriver driver) {
		this.driver = driver;
	}

	public String screencapture(String filepath1, String filepath2) {
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		String dest = filepath2;

		String finalDest = filepath1 + filepath2;
		//System.out.println("ScreenShot file name: " + finalDest);

		File destination = new File(Config.SRC_PATH_RPT + finalDest);

		try {
			FileUtils.copyFile(srcFile, destination);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dest;
	}
}