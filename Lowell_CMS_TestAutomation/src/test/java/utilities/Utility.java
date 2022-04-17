package utilities;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.aventstack.extentreports.ExtentTest;

public class Utility {

	public WebDriver driver;
	public ExtentTest log;
	
	public Utility(WebDriver driver, ExtentTest child) {
		this.driver = driver;
		this.log = child;	
	}
	
		
	
//	public void highlightElement(Iterator<WebElement> it) throws InterruptedException {
//		for (int i = 0; i < 5; i++) {
//			JavascriptExecutor js = (JavascriptExecutor) driver;
//			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", it,
//					"color: green; border: 2px solid yellow;");
//			Thread.sleep(200);
//			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", it, "");
//		}
//	}

	public void highlightElement(WebElement element) throws InterruptedException {
		for (int i = 0; i < 5; i++) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: yellow; border: 2px solid green;");
			Thread.sleep(200);
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		}
	}
	public String[] splitString(String param) {
		String[] list = param.trim().split("\\s*,\\s*");
		// for(String name : list){
		// System.out.println(name);
		// }
		return list;
	}
	
	public String ignoreQuotes(String input) {
		String eVal = input.replaceAll("^\"|\"$", "");
		// }
		return eVal;
	}
	
	public String linkcheck(String homePage, String page) throws InterruptedException {	
		String status=null;
		driver.get(homePage+page);
		 try {
			 WebElement acceptCookies = driver.findElement(By.xpath("//*[@class='btn-accept-all-cookie cta cta--primary']"));
				if(acceptCookies.isDisplayed()){
					acceptCookies.click();
				}else {
					System.out.println("Cookiee pop-up not available!");
				}
			List<WebElement> links = driver.findElements(By.tagName("a"));
			int count = links.size();
			System.out.println("-------------------Page URL: "+homePage+page+"------------------------");
			log.info("Page URL: "+homePage+page);
			System.out.println("No of links are = "+count);
			log.info("No of links are = "+count);
			System.out.println("----------------------------------------------------------------------");
            System.out.println("Page Title: "+driver.getTitle());
            log.info("Page Title: "+driver.getTitle());
            System.out.println("----------------------------------------------------------------------");
			 
            if (count==0) {
            	status="skip";
            	System.out.println("No Image URL found !");
            	log.info("No Link URL found !");
            } else {
            for(int i=0;i<count;i++)
		        {
		            WebElement lnk = links.get(i);
		            String link_url= lnk.getAttribute("href");
		            //System.out.println("["+i+"]"+lnk.getText() + " >> " + link_url);
		            //System.out.println("["+i+"]" + " >> " + link_url);
		            
		            if(link_url.contains("mailto") ||link_url.contains("mind.org.uk") || link_url.contains("moneysavingexpert.com") 
		            		|| link_url.contains("ofgem.gov.uk")|| link_url.contains("csa-uk.com")  || link_url.contains("ofgem.gov.uk")
		            		|| link_url.contains("actionfraud.police.uk")|| link_url.contains("takefive-stopfraud.org.uk")
		            		|| link_url.contains("protect-eu.mimecast.com") || link_url.contains("google.co.uk") || link_url.contains("transunion.com")  ) {
		            	status="skip";
		            	System.out.println("["+i+"] "+link_url +" : "+"Status: "+status);
		            	log.info("["+i+"] "+link_url +" : "+"Status: "+status);
		            	} else{ 
		            		status=verifyLinks(i,link_url);
		            	} 
		        }
            }
		 }catch(Exception e) {
			 System.out.println(e);
			 status="fail";
            }
		System.out.println("**********************************************************************");
		return status;
		
	}
	
	public String imageCheck(String homePage, String page) throws InterruptedException {
		String result=null;
		driver.get(homePage+page);
		 try {
			 WebElement acceptCookies = driver.findElement(By.xpath("//*[@class='btn-accept-all-cookie cta cta--primary']"));
				if(acceptCookies.isDisplayed()){
					acceptCookies.click();
				}else {
					System.out.println("Cookiee pop-up not available!");
				}
		// Storing all elements with img tag in a list of WebElements
        List<WebElement> images = driver.findElements(By.tagName("img"));
        int cnt=images.size();
//        System.out.println("Total number of Images on the Page are " + images.size());
//        log.info("Total number of Images on the Page are " + images.size());
        
        System.out.println("-------------------Page URL: "+homePage+page+"------------------------");
		log.info("Page URL: "+homePage+page);
		System.out.println("No of images are = "+cnt);
		log.info("No of images are = "+cnt);
		System.out.println("----------------------------------------------------------------------");
        System.out.println("Page Title: "+driver.getTitle());
        log.info("Page Title: "+driver.getTitle());
        System.out.println("----------------------------------------------------------------------");  
            if (images.size()==0) {
            	result="skip";
            	System.out.println("No Image URL found !");
            	log.info("No Image URL found !");
            } else {
        //checking the links fetched.
        for(int index=0;index<images.size();index++)
        {
            WebElement image= images.get(index);
            String imageURL= image.getAttribute("src");
            //System.out.println("URL of Image " + (index+1) + " is: " + imageURL);
            result= verifyLinks(index, imageURL);
          
            //Validate image display using JavaScript executor
//            try {
//                boolean imageDisplayed = (Boolean) ((JavascriptExecutor) driver).executeScript("return (typeof arguments[0].naturalWidth !=\"undefined\" && arguments[0].naturalWidth > 0);", image);
//                if (imageDisplayed) {
//                    System.out.println("DISPLAY - OK");
//                    log.pass("DISPLAY - OK");
//                }else {
//                     System.out.println("DISPLAY - BROKEN");
//                     log.fail("DISPLAY - BROKEN");
//                }
//            } 
//            catch (Exception e) {
//            	System.out.println("Error Occured");
//            	log.fail("Error Occured");
//            }
        }
            }
		 }catch(Exception e) {
			 System.out.println(e);
			 result="fail";
            }
		System.out.println("**********************************************************************");
		return result;
	}
	
	public String verifyLinks(int no, String new_url) {
		String res=null;
		HttpURLConnection httpConnection=null;
		HttpsURLConnection httpsConnection=null;
		try
        {
			URL url = new URL(new_url);
			 if(url.toString().startsWith("https")){ 
				 HttpsURLConnection Connection = (HttpsURLConnection)url.openConnection();  
				 httpsConnection=Connection;
				 httpsConnection.setConnectTimeout(5000);
				 httpsConnection.connect();
			 }
			 
			 else if(url.toString().startsWith("http")){ 
				 HttpURLConnection Connection = (HttpURLConnection)url.openConnection();  
				 httpConnection=Connection;
				 httpConnection.setConnectTimeout(5000);
				 httpConnection.connect();
			 }
			 else {
				 System.out.println("URL ERROR: "+url.toString());
			 }
			 
			          
//           // Proxy proxy = new Proxy();
//           // proxy.setProxyAutoconfigUrl("http://webdefence.global.blackspider.com:8082/proxy.pac?p=9f9wb89p");
//           //System.setProperty("java.net.useSystemProxies", "true");
//            //Creating url connection and getting the response code
//           // HttpsURLConnection httpsURLConnect=(HttpsURLConnection)url.openConnection(Proxy.NO_PROXY);
//            //URL url = new URL(new_url);
//			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("webdefence.global.blackspider.com", 8082));
//	        URLConnection connection = new URL(new_url).openConnection(proxy);
//           // URL url=new URL(new_url);
//            HttpsURLConnection httpsConnection = (HttpsURLConnection) connection..openConnection();            
//            httpsConnection.setConnectTimeout(60000);
//            httpsConnection.setRequestMethod("HEAD");
//            httpsConnection.connect();
            if (httpConnection!=null) {            
	            if(httpConnection.getResponseCode()>=400)
	            {
	            	System.out.println("["+no+"] "+new_url+" - "+httpConnection.getResponseMessage()+"is a broken link");
	            	log.fail("["+no+"] "+new_url+" - "+httpConnection.getResponseCode()+" - "+httpConnection.getResponseMessage()+" - is a broken link");
	            	res="fail";
	            }   
	       
	            //Fetching and Printing the response code & message obtained
	            else{
	                System.out.println("["+no+"] "+new_url+" - "+httpConnection.getResponseCode()+" - "+httpConnection.getResponseMessage());
	                log.pass("["+no+"] "+new_url+" - "+httpConnection.getResponseCode()+" - "+httpConnection.getResponseMessage());
	                res="pass";
	            }
            }else if (httpsConnection!=null) {         
	            if(httpsConnection.getResponseCode()>=400)
	            {
	            	System.out.println("["+no+"] "+new_url+" - "+httpsConnection.getResponseMessage()+"is a broken link");
	            	log.fail("["+no+"] "+new_url+" - "+httpsConnection.getResponseCode()+" - "+httpsConnection.getResponseMessage()+" - is a broken link");
	            	res="fail";
	            }    	       
	            //Fetching and Printing the response code & message obtained
	            else{
	                System.out.println("["+no+"] "+new_url+" - "+httpsConnection.getResponseCode()+" - "+httpsConnection.getResponseMessage());
	                log.pass("["+no+"] "+new_url+" - "+httpsConnection.getResponseCode()+" - "+httpsConnection.getResponseMessage());
	                res="pass";
	            }        	
            }else {
            	System.out.println("Connection Error !!!");
            	res="fail";
            }
        }catch (MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            res="fail";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            res="fail";
        }
		return res;
	}
	
	public void createFile(String pathfile){     
		File file = new File(pathfile); //initialize File object and passing path as argument  
		boolean result;  
		try {  
			result = file.createNewFile();  //creates a new file  
			if(result) {      // test if successfully created a new file   
				System.out.println("file created "+file.getCanonicalPath()); //returns the path string  
			}  
			else {  
				System.out.println("File already exist at location: "+file.getCanonicalPath());  
			}  
		} catch (IOException e) {  
		e.printStackTrace();    //prints exception if any  
		}         
	}  
	
	
}
