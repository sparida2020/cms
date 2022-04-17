package operation;

public class Config {
	// Instantiate a Date object
		//public String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		//static String DATE_VALUE = new SimpleDateFormat("MM.dd.yyyy").format(new Date());

		// Grid Config
		public static final String GRID_HUB_IP = "localhost";
		public static final String GRID_HUB_PORT = "4444";


		// BrowserStack Lowell -----Access Key
//		 public static final String USERNAME_BS="siddharthasankar_T4LePu";
//		 public static final String ACCESS_KEY_BS="TEyU1KwUe9jfp4eHcwvp";
//		 public static final String HUB_BS = "@hub-cloud.browserstack.com/wd/hub";

			// BrowserStack Personal -----Access Key
		 public static final String USERNAME_BS="siddharthaparida_TPNUqI";
		 public static final String ACCESS_KEY_BS="e6CFyqhEp7CpnxVx8Ryq";
		 public static final String HUB_BS = "@hub-cloud.browserstack.com/wd/hub";
	
		// RemoteWebdriver Config
		public static final int PAGE_LOADWAIT_TIME = 120;
		public static final int XSMALL_PAUSE = 5;
		public static final int SMALL_PAUSE = 10;
		public static final int MEDIUM_PAUSE = 30;
		public static final int LARGE_PAUSE = 60;
		public static final int XLARGE_PAUSE = 500;
		public static final int POLLING_TIME = 500;

		// File Paths
		//EXE file path will be taken from Source Factory  ???
		public static final String SKLI_PATH 	 = System.getProperty("user.dir") + "/src/test/java/images/";
		public static final String SRC_PATH 	 = System.getProperty("user.dir") + "/src/test/java/testsuites/";
		public static final String SRC_PATH_RPT = System.getProperty("user.dir") + "/src/test/java/Reports/";
		public static final String TEST_RES_PATH = System.getProperty("user.dir") + "/src/test/java/Reports/";
		public static final String TEST_RES_LOG_PATH = System.getProperty("user.dir") + "/src/test/java/Reports/" + "ConsoleLog.txt";
		public static final String PRPTY_PATH = System.getProperty("user.dir") + "/src/test/java/objectrepository/locator.properties";
		public static final String EXTENT_REPORT_PATH = System.getProperty("user.dir") + "/src/test/java/Reports/";
		public static final String EXTENT_REPORT_IMG_PATH = System.getProperty("user.dir") + "/logo.png";
		public static final String EXTENT_CONFIG_XML_PATH = System.getProperty("user.dir") + "/extent-config.xml";
		public static final String CHROMEDRIVER_PATH = System.getProperty("user.dir") + "/src/test/java/utilities/" + "chromedriver.exe";
		
		//Application Config		
		public static final String  DOMAIN="@lowell.co.uk";
		public static final String  portal="lowell";
		public static final String	DEV="https://dev.lowell.co.uk/";
		public static final String	QA1="https://test1.lowell.co.uk/";  
		public static final String	QA2="https://test2.lowell.co.uk/";  
		public static final String  platform = "WINDOWS";
		public static final String	PROD="https://www.lowell.co.uk/";
		public static final String	RELEASE="https://release.lowell.co.uk/";
		public static final String  plat_version = "10";
		public static final String  BROWSER = "Chrome";
		public static final String  ENVIRONMENT = "QA";
		public static final String  SMOKE ="SMOKE";
		public static final String  BUILDNO ="BUILD-001";

		
		public static final String APITEST="https://test2.lowell.co.uk/";

}
