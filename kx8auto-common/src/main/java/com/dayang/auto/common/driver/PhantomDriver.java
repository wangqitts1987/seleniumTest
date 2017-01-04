package com.dayang.auto.common.driver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class PhantomDriver {
	    WebDriver driver;
	
		public PhantomDriver(){
			String ph = System.getProperty("user.dir")+"/res/phantomjs1_9";
			// prepare capabilities
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setJavascriptEnabled(true); // < not really needed: JS enabled by
												// default
			caps.setCapability("takesScreenshot", true); // < yeah, GhostDriver haz
															// screenshotz!
			if (isWindowsOS()) {
				ph =  System.getProperty("user.dir")+"/res/phantomjs1_9.exe";
			} else
				ph =  System.getProperty("user.dir")+"/res/phantomjs1_9";
			caps.setCapability(
					PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, ph);

			// Launch driver (will take care and ownership of the phantomjs process)
			driver = new PhantomJSDriver(caps);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		}
		
		public WebDriver getDriver(){
			return this.driver;
		}

		private boolean isWindowsOS() {
			boolean isWindowsOS = false;
			String osName = System.getProperty("os.name");
			if (osName.toLowerCase().indexOf("windows") > -1) {
				isWindowsOS = true;
			}
			return isWindowsOS;
		}
}
