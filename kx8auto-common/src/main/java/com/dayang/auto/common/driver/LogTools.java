
package com.dayang.auto.common.driver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

public class LogTools {

	public static void log(String logText) {
		System.out.println("[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())) + "] " + logText);
	}

	public static String screenShot(NewBrowserEmulator be) {
		//String dir = "./screenshot"; // TODO
		String time = new SimpleDateFormat("MMdd-HHmmssSSS").format(new Date());
		//String screenShotPath = dir + File.separator + time + ".png";
        
		/*WebDriver augmentedDriver = null;
		if (GlobalSettings.BrowserCoreType == 1 || GlobalSettings.BrowserCoreType == 3) {
			augmentedDriver = be.getBrowserCore();
			//augmentedDriver.manage().window().setPosition(new Point(0, 0));
			//augmentedDriver.manage().window().setSize(new Dimension(9999, 9999));
		} else if (GlobalSettings.BrowserCoreType == 2) {
			augmentedDriver = new Augmenter().augment(be.getBrowserCore());
		} else {
			return "Incorrect browser type";
		}
		*/
		String screenShotPath = System.getProperty("user.dir")+"\\screenshot\\" + time +".png";
		System.out.println(screenShotPath);
		WebDriver driver = be.getBrowserCore();
		try {
			File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(sourceFile, new File(screenShotPath));
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to screenshot";
		}

		return screenShotPath;
	}
	public static void delScreenShot(){
		String screenShotPath = System.getProperty("user.dir")+"\\screenshot\\";
		File file = new File(screenShotPath);
		if (!file.exists() || !file.isDirectory()) {
	         return ;
	    }
		String[] tempList = file.list();
	    File temp = null;
		for (int i = 0; i < tempList.length; i++) {
	         if (screenShotPath.endsWith(File.separator)) {
	            temp = new File(screenShotPath + tempList[i]);
	         } else {
	             temp = new File(screenShotPath + File.separator + tempList[i]);
	         }
	         if (temp.isFile()) {
	            temp.delete();
	         }
		}
	}
}
