package testrunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import operation.Config;
import operation.GetScreenShot;
import operation.ReadObject;
import operation.UIOperation;

public class ExecuteTests {
	ExtentHtmlReporter htmlReporter;
	ExtentReports report;
	ExtentTest parent;
	ExtentTest child;
	//for BrowserStack >>
	//RemoteWebDriver driver;
	//for local >> 
	WebDriver driver;
	WebElement wbe;
	String url = null;
	String box = null;
	String platform = null;
	String plat_version = null;
	String testtype = null;
	String buildNo = null;
	String br = null;
	String srcfilename = null;
	String srcfilepath = null;
	String resfilepath = null;
	String driverpath = null;
	String propertyFilePath = null;
	float passperc = 0;
	float failperc = 0;
	float skipperc = 0;
	float count = 0;
	boolean found = false;
	ArrayList<String> tempStatus = null;
	ArrayList<String> passCount = null;
	ArrayList<String> failCount = null;
	ArrayList<String> skipCount = null;
	PrintStream consoleOut;
	
	@BeforeClass
	@Parameters(value = { "browser","browser_version", "os", "os_version", "device","orientation","resolution" })
	public void setUp(String browser, String browser_version,String platform, String plat_version, String device, String orientation, String resolution) throws Exception {
		// ***************** CICD/ADO Execution Parameters/BrowserStack *****************
		//testtype = System.getProperty("testfilepath");
		//box = System.getProperty("env");
		//buildNo = System.getProperty("build");
		// ********* Local Desktop/BrowserStack Execution Parameters ********
			testtype = "SMOKE"; // SMOKE/REGRESSION
			//box = "PROD"; 		 // TEST/ DEV
			box ="REL";
			buildNo = testtype+"_BUILD";		
		
		br = browser.toLowerCase();
		srcfilepath = Config.SRC_PATH;
		resfilepath = Config.TEST_RES_PATH;
		System.out.println("File path: " + resfilepath);
		
		if (testtype.equalsIgnoreCase("smoke")) {
			srcfilename = Config.SMOKE+"_TestSuite" + ".csv";
			System.out.println( srcfilepath+ srcfilename);
		} else {
			srcfilename = "No parameters defined ...";
			System.out.println(srcfilepath + srcfilename);
			}	

		if (box.equalsIgnoreCase("prod")) {
			url = Config.PROD;
			System.out.println("URL:" + url);
		} 
		else if(box.equalsIgnoreCase("rel")){
			url=Config.RELEASE;
			System.out.println("URL:" + url);
		}else if(box.equalsIgnoreCase("test1")){
			url=Config.QA1;
			System.out.println("URL:" + url);
		}else if(box.equalsIgnoreCase("test2")){
			url=Config.QA2;
			System.out.println("URL:" + url);
		}else {
			url = "No URL found or value not set for this environment";
			System.out.println("URL:" + url);
		}
		
		DesiredCapabilities capability = new DesiredCapabilities();
		capability.setCapability("browserName", br);
		capability.setCapability("device", device);
		capability.setCapability("real_mobile", "true");
		capability.setCapability("os_version", plat_version);		
		
		capability.setCapability("browser_version",browser_version);
		capability.setCapability("os", platform);		
		capability.setCapability("browserstack.local", "false");	
		capability.setCapability("browserstack.debug", "true");
		capability.setCapability("browserstack.console", "verbose");
		capability.setCapability("browserstack.networkLogs", "true");		
		capability.setCapability("project", "Lowell CMS Test Automation");
		capability.setCapability("build", buildNo + " : " + box + " : " + testtype);
		
//		capability.setCapability("browserstack.appium_version", appium_version);
//		capability.setCapability("browserstack.selenium_version", selenium_version);

		System.out.println("Browser : " + br);
		String URL_BS = "https://" + Config.USERNAME_BS + ":" + Config.ACCESS_KEY_BS + Config.HUB_BS;
		System.out.println(URL_BS);
		
		//-----------------------------------BROWSERSTACK TEST EXECUTION---------------------------
//		driver = new RemoteWebDriver(new URL(URL_BS), capability);
//		System.out.println("BrowserStack Session: " + driver.getSessionId());
//		if(platform.equalsIgnoreCase("windows") || platform.equalsIgnoreCase("os x")) {
//		driver.manage().window().maximize();
//		}
		propertyFilePath = Config.PRPTY_PATH;
		//-----------------------------------------------------------------------------------------
		//-----------------------------------LOCAL CHROME TEST EXECUTION---------------------------
		driverpath=  Config.CHROMEDRIVER_PATH;				
		System.out.println(driverpath);
		System.setProperty("webdriver.chrome.driver", driverpath);
		WebDriver dr = new ChromeDriver(); 
		driver = dr;		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();	
		//driver.get(url);
		//driver.navigate().refresh();
		// driver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS);
//		//Thread.sleep(5000);
		//------------------------------------------------------------------------------------------
	}
	
	@Test	
	@Parameters(value = { "browser", "os", "os_version", "device" })
	public void executeTests(String browser, String platform, String plat_version, String device) throws Exception {
		System.out.println();
		String consoleFilepath=Config.SRC_PATH_RPT + platform.replaceAll(" ", "_").toLowerCase() + "_" + plat_version.toLowerCase() + "_"+ br.toLowerCase() + "_TestResults/" + testtype + "_ConsoleLog.txt";
		String ExtentFilepath=Config.SRC_PATH_RPT+ platform.replaceAll(" ", "_").toLowerCase() + "_" + plat_version.toLowerCase() + "_"
				+ br.toLowerCase() + "_TestResults/" + testtype;
		String csvFilePath=Config.TEST_RES_PATH + platform.replaceAll(" ", "_").toLowerCase() + "_" + plat_version.toLowerCase()
		+ "_" + br.toLowerCase() + "_TestResults/" + testtype;

		final boolean append = true, autoflush = true;
		consoleOut = new PrintStream(new File(consoleFilepath));
		System.setOut(consoleOut);
		System.out.println(
				"**************************** Printing Console Logs Starts Here !!! **************************");
		htmlReporter = new ExtentHtmlReporter(ExtentFilepath + "_TestAutomation_Report.html");
		htmlReporter.loadXMLConfig(new File(Config.EXTENT_CONFIG_XML_PATH));
		report = new ExtentReports();
		report.attachReporter(htmlReporter);
		report.setSystemInfo("QA Build# (ADO) :", buildNo);
		report.setSystemInfo("Environment", box);
		report.setSystemInfo("Test Type", testtype);
		report.setSystemInfo("Browser", br);
		htmlReporter.config().setDocumentTitle("CMS: Lowell: Test Automation");
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setChartVisibilityOnOpen(false);
		htmlReporter.config().setTheme(Theme.DARK);

		ArrayList<String> tempStatus = new ArrayList<String>();
		passCount = new ArrayList<String>();
		failCount = new ArrayList<String>();
		skipCount = new ArrayList<String>();
		
		
		ReadObject object = new ReadObject();
		Properties allObjects = object.getObjectRepository(propertyFilePath);		
		GetScreenShot getScreen = new GetScreenShot(driver);
		
		try {
			CsvReader reader1 = new CsvReader(srcfilepath + srcfilename);
			reader1.readHeaders();
			// Write to CSV file which is open
			CsvWriter writer1 = new CsvWriter(new FileWriter(csvFilePath + "_TestCaseResults.csv",
					false), ',');
			// get total numbers of header column
			int numberOfHeaders = reader1.getHeaderCount();
			// print header column
			// writer1.write("Test Cases Executed on : ");
			for (int i = 0; i < numberOfHeaders; i++) {
				//System.out.print(reader1.getHeader(i) + ",  ");
				//System.out.print("\n  ");
				writer1.write(reader1.getHeader(i) + ",  ");
			}
			
			writer1.endRecord();
			while (reader1.readRecord()) {
				String testCaseId = reader1.get("Test Case");
				String testCaseName = reader1.get("Test Case Description");
				String feature = reader1.get("Feature Name");
				String execute = reader1.get("Execute(Yes/No)");
				String teststatus = reader1.get("Execution Status");
				// output file content
				System.out.println("############################################################################################################################################");
				System.out.println("Test Case: "+testCaseId+": "+testCaseName+ " >>> Start");
				//System.out.println("---------------------------TEST CASE INPUT------------------------------");
				System.out
						.println(testCaseId + " : " + testCaseName + ":" + feature + ":" + execute + ":" + teststatus);
				System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
				// String sheetname = feature.toLowerCase();
				String sheetname = feature;

				parent = report.createTest("<font size=\"2\" color=\"#22a1c4\" >" + testCaseId + " : "+ testCaseName + "</font>");
				// logger = report.startTest(testCaseId + " : " + testCaseName);
				String testSuiteFolder = Config.SRC_PATH + testtype.toLowerCase() + "/" + sheetname + ".csv";
				System.out.println("Test Suite Folder selected is : >>>>>" + testSuiteFolder);
				CsvReader reader2 = new CsvReader(testSuiteFolder);
				reader2.readHeaders();
				// Write to CSV file which is open
				CsvWriter writer2 = new CsvWriter(new FileWriter(csvFilePath + "_TestStepResults.csv",
						true), ',');

				// get total numbers of header column
				int noOfHeaders = reader2.getHeaderCount();
				// print header column
				// writer2.write("Test Steps Executed on : ");
				for (int j = 0; j < noOfHeaders; j++) {
					//System.out.print(reader2.getHeader(j) + ", ");
					//System.out.print("\n  ");
					writer2.write(reader2.getHeader(j) + ", ");
				}
				
				writer2.endRecord();
				while (reader2.readRecord()) {
					String testStepId = reader2.get("Test Step ID");
					String testStepName = reader2.get("Test Step Description");
					String keyword = reader2.get("Keyword");
					String param1 = reader2.get("Object");
					String param2 = reader2.get("Locator");
					String param5 = reader2.get("Test Data");
					String executestep = reader2.get("ExecuteStep(Yes/No)");
					String screenshot = reader2.get("CaptureScreen(Yes/No)");
					String stepstatus = reader2.get("Execution Status");
					// output file content
					System.out.println("========================================================================================================================================");
					System.out.println("Test Step: "+testStepId+": "+testStepName+ " >>> Start");
					//System.out.println("----------------------TEST STEP INPUT-------------------------");
					System.out.println(testStepId + "," + testStepName + "," + keyword + "," + param1 + "," + param2
							+ "," + param5 + "," + screenshot + "," + stepstatus);
					//System.out.println("--------------------------------------------------------------");
					child = parent.createNode(
							"<font size=\"2\" color=\"#22a1c4\" >" + testStepId + ": " + testStepName + "</font>");
					
					if (execute.equalsIgnoreCase("yes") && executestep.equalsIgnoreCase("yes")) {
						// Call perform function to perform operation on UI
						Thread.sleep(2000);
						UIOperation operation = new UIOperation(driver, child);
						String actionResult = operation.perform(screenshot, br, box, testtype, url, allObjects, keyword,
								param1, param2, param5);
						tempStatus.add(actionResult);
						
				
						if (actionResult.equalsIgnoreCase("pass")) {
							stepstatus = actionResult;
							teststatus = actionResult;
							passCount.add(actionResult);
							System.out.println("Action Performed: " + keyword + " : " + "Element Property: " + param1 + " : "
									+ "Find By: " + param2 + " : "+"Test Data/Value: " + param5);
							System.out.println("Results is: " + actionResult);
							
							//System.out.println("**************************************************************");
							//System.out.println(" ");
							child.log(Status.PASS,
									"<font size=\"2\" color=\"#82b74b\" >" + "<u>" + "Test Step Details:" + "</u>"
											+ "</font>" + "<br />" + "<font size=\"2\" >" + "Results: " + stepstatus
											+ "<br />" + "Object Name: " + param1 + "<br />" + "Object Value: "
											+ allObjects.getProperty(param1) + "<br />" + "Locator: " + param2
											+ "<br />" + "Test Data: " + param5 + "</font>" + "<br />");

							// *****************************************************
							// ********* Screenshot Capture for Pass ***************
							// *****************************************************
							//if (screenshot.equalsIgnoreCase("yes") && testtype.equalsIgnoreCase("smoke")) {
							if (screenshot.equalsIgnoreCase("yes")) {
								// Capture screenshots embeded with the html report
								// src=\"data:image/png;base64,"+utill.capture(driver)+"\"/>");
								String passtimeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
								String path1 = platform.replaceAll(" ", "_").toLowerCase() + "_" + plat_version.toLowerCase() + "_"
										+ br.toLowerCase() + "_TestResults/";
								String path2 = "screenshots/" + passtimeStamp + "_" + testtype + "_" + param2 + "_"
										+ param1 + keyword + ".jpg";
								//System.out.println("Image Location path : >>>" + path1 + path2);
								
								String imgLoc1 = getScreen.screencapture(path1, path2);
								child.addScreenCaptureFromPath((imgLoc1));
								//System.out.println("Test Step: "+testStepId+": "+testStepName+ " >>> End");
								//System.out.println(" ");
							}

						} else if (actionResult.equalsIgnoreCase("fail")) {
							stepstatus = actionResult;
							teststatus = actionResult;
							failCount.add(actionResult);
							System.out.println("Action Performed: " + keyword + " : " + "Element Property: " + param1 + " : "
									+ "Find By: " + param2 + " : "+"Test Data/Value: " + param5);
							System.out.println("Results is: " + actionResult);
							
							//System.out.println("**************************************************************");
							//System.out.println(" ");
							child.log(Status.FAIL,
									"<font size=\"2\" color=\"red\" >" + "<u>" + "Test Step Details:" + "</u>"
											+ "</font>" + "<br />" + "<font size=\"2\" >" + "Results: " + stepstatus
											+ "<br />" + "Object Name: " + param1 + "<br />" + "Object Value: "
											+ allObjects.getProperty(param1) + "<br />" + "Locator: " + param2
											+ "<br />" + "Test Data: " + param5 + "</font>" + "<br />");

							// *****************************************************
							// ********* Screenshot Capture for Fail ***************
							// *****************************************************
							if (screenshot.equalsIgnoreCase("yes")) {
								// Capture screenshots embeded with the html report
								// src=\"data:image/png;base64,"+utill.capture(driver)+"\"/>");
								String failtimeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
								String path1 = platform.replaceAll(" ", "_").toLowerCase() + "_" + plat_version.toLowerCase() + "_"
										+ br.toLowerCase() + "_TestResults/";
								String path2 = "screenshots/" + failtimeStamp + "_" + testtype + "_" + param2 + "_"
										+ param1 + keyword + ".jpg";
								//System.out.println("Image Location path : >>>>" + path1 + path2);
								
								String imgLoc2 = getScreen.screencapture(path1, path2);
								child.addScreenCaptureFromPath((imgLoc2));
								//System.out.println("Test Step: "+testStepId+": "+testStepName+ " >>> End");
								//System.out.println(" ");
							}
						} else if (actionResult.equalsIgnoreCase("skip")) {
							stepstatus = actionResult;
							teststatus = actionResult;
							child.log(Status.SKIP, "This Step is Skipped");
						}else {
							stepstatus = actionResult;
							teststatus = actionResult;
							child.log(Status.WARNING, "Some issues found in results");
							// parent.log(LogStatus.UNKNOWN,"Some issues found in results");
						}

					} else {
						stepstatus = "SKIP";
						teststatus = "SKIP";
						skipCount.add(stepstatus);
						child.log(Status.SKIP, "<font size=\"2\" color=\"darkorange\" >" + testStepId + ": "
								+ testStepName + "</font>");
					}

					writer2.write(testStepId);
					writer2.write(testStepName);
					writer2.write(keyword);
					writer2.write(param1);
					writer2.write(param2);
					writer2.write(param5);
					writer2.write(executestep);
					writer2.write(screenshot);
					writer2.write(stepstatus);
					writer2.endRecord();

				}
				consoleOut.println();
				writer2.flush();
				writer2.close();
				reader2.close();

				// ArrayList<String> thisIsAStringArray = tempStatus;
				String stringToSearch = "fail";
				for (String element : tempStatus) {
					if (element.equalsIgnoreCase(stringToSearch)) {
						found = true;
						teststatus = element;
						parent.log(Status.FAIL, "<font size=\"2\" color=\"red\" >" + testCaseName + "</font>");
						break;
					} else if (element.equalsIgnoreCase("pass")) {
						found = true;
						teststatus = element;
						parent.log(Status.PASS, "<font size=\"2\" color=\"#82b74b\" >" + testCaseName + "</font>");
						break;
					} else {
						parent.log(Status.INFO,
								"<font size=\"2\" color=\"#22a1c4\" >" + testCaseId + ": " + testCaseName + "</font>");
						break;
					}
				}

				writer1.write(testCaseId);
				writer1.write(testCaseName);
				writer1.write(feature);
				writer1.write(execute);
				writer1.write(teststatus);
				writer1.endRecord();
			}
			writer1.flush();
			writer1.close();
			reader1.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Pass Status found are: " + passCount + " : " + passCount.size());
		System.out.println("Fail Status found are: " + failCount + " : " + failCount.size());
		System.out.println("Skip Status found are: " + skipCount + " : " + skipCount.size());
		int totalCount = passCount.size() + failCount.size() + skipCount.size();
		if (passCount.size() != 0) {
			count = totalCount;
			passperc = (passCount.size() / count) * 100;
		} else {
			passperc = 0;
		}

		if (failCount.size() != 0) {
			count = totalCount;
			failperc = (failCount.size() / count) * 100;
		} else {
			failperc = 0;
		}

		if (skipCount.size() != 0) {
			count = totalCount;
			skipperc = (skipCount.size() / count) * 100;
		} else {
			skipperc = 0;
		}

		System.out.println("Total no of test cases executed are : " + totalCount);
		System.out.println("Percentage found : " + passperc + " : " + failperc + " : " + skipperc);
		System.out
				.println("**************************** Printing Console Logs Ends Here !!! **************************");
		if (failperc >= 50) {
			throw new RuntimeException(
					"50% or more no of test cases are failed so failing this build" + "\n Percentage found : "
							+ "PASS :" + passperc + " : " + "FAIL :" + failperc + " : " + "SKIP :" + skipperc);
		//}
		
		} else if (testtype.equalsIgnoreCase("smoke") && failperc > 0) {
			throw new RuntimeException(
					"Test Cases are not passed 100%; so failing this build" + "\n Percentage found : " + "PASS :"
							+ passperc + " : " + "FAIL :" + failperc + " : " + "SKIP :" + skipperc);
		}
	}

	@AfterClass
	public void closeApp() throws Exception {
		report.flush();
		driver.quit();
	}

}
