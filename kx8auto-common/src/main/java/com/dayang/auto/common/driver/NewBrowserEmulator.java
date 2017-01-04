package com.dayang.auto.common.driver;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.dayang.auto.common.manager.ExtentManager;
import com.dayang.auto.common.util.Kx8AutoCommonUtil;
import com.dayang.auto.conf.ConfDriverUtils;
import com.dayang.auto.conf.ConfGlobalUtils;
/**
 * 类描述：BrowserEmulator is based on Selenium2 and adds some enhancements
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年11月10日               张才胜               V01.00.001		      新增内容   
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */
public class NewBrowserEmulator {

	WebDriver driver;
	ChromeDriverService chromeServer;
	JavascriptExecutor javaScriptExecutor;
	String currenWindowHanler;

	int stepInterval = Integer.parseInt(ConfGlobalUtils.get("StepInterval"));

	int timeout = Integer.parseInt(ConfGlobalUtils.get("Timeout"));

	private static final Logger LOGGER = Logger.getLogger(Kx8AutoCommonUtil.class);
 
	public NewBrowserEmulator() {
		try {
			setupdriverType(Integer.parseInt(ConfGlobalUtils.get("BrowserCoreType")));
		} catch (MalformedURLException e) {
			Assert.fail("Incorrect GridHubUrl value!");
		}

		javaScriptExecutor = (JavascriptExecutor) driver;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		LOGGER.info("Started BrowserEmulator");
	}
	
	private void setupdriverType(int type) throws MalformedURLException {
		DesiredCapabilities capabilities = null;
		if (type == 1) {
			LOGGER.info("Using Firefox");
			if (ConfGlobalUtils.get("Parallel").equalsIgnoreCase("false")) {
				driver = new FirefoxDriver();
			} else {
				capabilities = DesiredCapabilities.firefox();
				capabilities.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
				capabilities.setPlatform(org.openqa.selenium.Platform.ANY);
				driver = new RemoteWebDriver(new URL("http://" + ConfGlobalUtils.get("GridHubUrl") + "/wd/hub"),
						capabilities);
			}

		} else if (type == 2) {
			LOGGER.info("Using Chrome");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized");
			capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			if (ConfGlobalUtils.get("Parallel").equalsIgnoreCase("false")) {
				System.out.println(ConfDriverUtils.getChromeDriver());
				chromeServer = new ChromeDriverService.Builder()
						.usingDriverExecutable(new File(ConfDriverUtils.getChromeDriver())).usingAnyFreePort().build();

				try {
					chromeServer.start();
				} catch (IOException e) {
					LOGGER.info("Timed out waiting for driver server to start.");
				}

				driver = new RemoteWebDriver(chromeServer.getUrl(), capabilities);
			} else {
				driver = new RemoteWebDriver(new URL("http://" + ConfGlobalUtils.get("GridHubUrl") + "/wd/hub"),
						capabilities);
			}
		} else if (type == 3) {
			LOGGER.info("Using IE");
			System.setProperty("webdriver.ie.driver", ConfDriverUtils.get("driver.ie"));
			capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

			if (ConfGlobalUtils.get("Parallel").equalsIgnoreCase("false")) {
				driver = new InternetExplorerDriver(capabilities);
			} else {
				driver = new RemoteWebDriver(new URL("http://" + ConfGlobalUtils.get("GridHubUrl") + "/wd/hub"),
						capabilities);
			}

		} else if (type == 4) {

			// "/path/phantomjs.exe
			// --webdriver=5567--webdriver-selenium-grid-hub=http://127.0.0.1:4444"
			LOGGER.info("Using PhantomDriver");
			capabilities = DesiredCapabilities.phantomjs();

			if (ConfGlobalUtils.get("Parallel").equalsIgnoreCase("false")) {
				driver = new PhantomDriver().getDriver();
			} else {
				driver = new RemoteWebDriver(new URL("http://" + ConfGlobalUtils.get("GridHubUrl") + "/wd/hub"),
						capabilities);
			}

		} else if (type == 5) {
			driver = new HtmlUnitDriver();
		} else {

			Assert.fail("Incorrect browser type");
		}

	}

	/**
	 * Get the WebDriver instance embedded in BrowserEmulator
	 * 
	 * @return a WebDriver instance
	 */
	public WebDriver getBrowserCore() {
		return driver;
	}

	/**
	 * Get the JavascriptExecutor instance embedded in BrowserEmulator
	 * 
	 * @return a JavascriptExecutor instance
	 */
	public JavascriptExecutor getJavaScriptExecutor() {
		return javaScriptExecutor;
	}

	/**
	 * Open the URL
	 * 
	 * @param url
	 *            the target URL
	 */
	public void open(String url) {
		pause(stepInterval);
		try {
			driver.get(url);
		} catch (Exception e) {
			onTimeOut();
			// handleFailure("Failed to open url " + url);
		}
		LOGGER.info("Opened url " + url);
	}

	private void onTimeOut() {
		javaScriptExecutor.executeScript("window.stop();");
	}

	/*
	 * get the Value from the element on xpath
	 * 
	 * @param xpath the element xpath
	 */

	public String getAttribut(String xpath, String attribute) {
		pause(stepInterval);
		// expectElementExistOrNot(true, xpath, timeout);

		// String attributeValue =
		// driver.findElement(By.xpath(xpath)).getAttribute(attribute);
		String attributeValue = expectElementExistOrNot(true, xpath, timeout).getAttribute(attribute);
		LOGGER.info("get the attribute: " + attribute + " on element " + xpath + ", value is " + attributeValue);
		return attributeValue;
	}

	/*
	 * get the Value from the element on xpath
	 * 
	 * @param xpath the element xpath
	 */

	public String getVaule(String xpath) {
		pause(stepInterval);
		// expectElementExistOrNot(true, xpath, timeout);
		String text = getAttribut(xpath, "value");

		LOGGER.info("get the value: " + text + " on element " + xpath);
		return text;
	}

	/*
	 * get the Text from the element on xpath
	 * 
	 * @param xpath the element xpath
	 */

	public String getText(String xpath) {
		pause(stepInterval);
		// expectElementExistOrNot(true, xpath, timeout);
		// String text = driver.findElement(By.xpath(xpath)).getText();
		//String text = expectElementExistOrNot(true, xpath, timeout).getText();
		WebElement e = expectElementExistOrNot(true, xpath, timeout);
		String text = "";
		if (e != null){
			text = e.getText();
		}
		LOGGER.info("get the text: " + text + " on element " + xpath);
		return text;
	}

	/*
	 * 
	 * submit a form
	 */
	public void submit(String xpath) {
		driver.findElement(By.xpath(xpath)).submit();
	}

	/**
	 * Quit the browser
	 */
	public void quit() {
		pause(stepInterval);
		if (Integer.parseInt(ConfGlobalUtils.get("BrowserCoreType")) == 1) {
			driver.close();
		} else
			driver.quit();
		if (Integer.parseInt(ConfGlobalUtils.get("BrowserCoreType")) == 2) {

			if (ConfGlobalUtils.get("Parallel").equalsIgnoreCase("false")) {
				chromeServer.stop();
			}

			try {
				if (OSinfo.isWindows() && getProcess("chromedriver_for_win.exe")) {

					Runtime.getRuntime().exec("taskkill /im chromedriver_for_win.exe /f");

				} else if (OSinfo.isWindows() && getProcess("chrome.exe")) {
					Runtime.getRuntime().exec("taskkill /im chrome.exe /f");
				} else if (OSinfo.isLinux()) {
					Runtime.getRuntime().exec("killall -9 chrome");
					// Runtime.getRuntime().exec("killall -9 chromedriver");
				}

			} catch (IOException e) {
				LOGGER.info("Taskkill failed to kill any rogue chrome or chromedriver on " + OSinfo.getOSname());
			}
		}
		LOGGER.info("Quitted BrowserEmulator");
	}

	/**
	 * Click the page element
	 * 
	 * @param xpath
	 *            the element's xpath
	 */
	public void click(String xpath) {
		pause(stepInterval);
		expectElementExistOrNot(true, xpath, timeout);
		try {
			clickTheClickable(xpath, System.currentTimeMillis(), 2500);
		} catch (Exception e) {
			handleFailure("Failed to click " + xpath);
		}
		LOGGER.info("Clicked " + xpath);
	}

	/*
	 * Select a frame using its previously located {@link WebElement}.
	 * 
	 * @param frameXpath The frame element to switch to.
	 */
	public void selectFrame(String frameXpath) {
		pause(stepInterval);

		try {
			driver.switchTo().frame(expectElementExistOrNot(true, frameXpath, timeout));

		} catch (Exception e) {
			handleFailure("Failed to switch the frame " + frameXpath);
		}
		LOGGER.info("Switch to the frame " + frameXpath);
	}

	/*
	 * Select a frame by its (zero-based) index. Selecting a frame by index is
	 * equivalent to the JS expression window.frames[index] where "window" is
	 * the DOM window represented by the current context. Once the frame has
	 * been selected, all subsequent calls on the WebDriver interface are made
	 * to that frame.
	 * 
	 * @param index (zero-based) index
	 */
	public void selectFrame(int index) {
		pause(stepInterval);

		try {
			driver.switchTo().frame(index);

		} catch (Exception e) {
			handleFailure("Failed to switch the " + index + " frame.\t" + e.toString());
		}
		LOGGER.info("Switch to the " + index + " frame");
	}

	/*
	 * Select a frame by its name or ID. Frames located by matching name
	 * attributes are always given precedence over those matched by ID.
	 * 
	 * @param nameOrId the name of the frame window, the id of the &lt;frame&gt;
	 * or &lt;iframe&gt; element, or the (zero-based) index
	 * 
	 * @return This driver focused on the given frame
	 */
	public void selectFrameByIdorName(String nameOrId) {
		pause(stepInterval);

		try {
			driver.switchTo().frame(nameOrId);

		} catch (Exception e) {
			handleFailure("Failed to switch the " + nameOrId + " frame.\t" + e.toString());
		}
		LOGGER.info("Switch to the " + nameOrId + " frame");
	}

	/*
	 * select option by value
	 * 
	 * @param xpath the element's xpath
	 * 
	 * @param optionValue selected value
	 */
	public void selectByValue(String xpath, String optionValue) {
		pause(stepInterval);
		expectElementExistOrNot(true, xpath, timeout);
		try {
			new Select(driver.findElement(By.xpath(xpath))).selectByValue(optionValue);
		} catch (Exception e) {
			handleFailure("Failed to select " + xpath + " with value " + optionValue);
		}
		LOGGER.info("select  " + xpath + " with " + optionValue);
	}

	/*
	 * select option by Index
	 * 
	 * @param xpath the element's xpath
	 * 
	 * @param index selected index
	 */

	public void selectByIndex(String xpath, int index) {
		pause(stepInterval);
		expectElementExistOrNot(true, xpath, timeout);
		try {
			new Select(driver.findElement(By.xpath(xpath))).selectByIndex(index);
		} catch (Exception e) {
			handleFailure("Failed to select " + xpath + " with Index " + index);
		}
		LOGGER.info("select  " + xpath + " with " + index);
	}

	/*
	 * select option by test
	 * 
	 * @param xpath the element's xpath
	 * 
	 * @param optionTest selected Test
	 */
	public void selectByVisibleText(String xpath, String optionTest) {
		pause(stepInterval);
		expectElementExistOrNot(true, xpath, timeout);
		try {
			new Select(driver.findElement(By.xpath(xpath))).selectByVisibleText(optionTest);
		} catch (Exception e) {
			handleFailure("Failed to select " + xpath + " with test " + optionTest);
		}
		LOGGER.info("select  " + xpath + " with " + optionTest);
	}

	/**
	 * Click the page element by JS, this method is used to resolve the problem
	 * that the target element is not clickable.
	 * 
	 * @param xpath
	 *            the element's xpath
	 */
	private void clickByJS(String xpath) {
		pause(stepInterval);
		expectElementExistOrNot(true, xpath, timeout);
		try {
			WebElement we = driver.findElement(By.xpath(xpath));
			javaScriptExecutor.executeScript("arguments[0].click();", we);
		} catch (Exception e) {
			handleFailure("Failed to click " + xpath);
		}
		LOGGER.info("Clicked " + xpath);
	}

	/**
	 * Click an element until it's clickable or timeout
	 * 
	 * @param xpath
	 * @param startTime
	 * @param timeout
	 *            in millisecond
	 * @throws Exception
	 */
	private void clickTheClickable(String xpath, long startTime, int timeout) throws Exception {
		try {
			// WebElement we= driver.findElement(By.xpath(xpath));
			// ((Locatable) we).getCoordinates().inViewPort();
			// we.click();

			driver.findElement(By.xpath(xpath)).click();
		} catch (Exception e) {
			if (System.currentTimeMillis() - startTime > timeout) {
				LOGGER.info("Element " + xpath + " is unclickable");
				throw new Exception(e);
			} else {
				pause(500);
				LOGGER.info("Element " + xpath + " is unclickable, try again");
				clickByJS(xpath);
			}
		}
	}

	/**
	 * Type text at the page element<br>
	 * Before typing, try to clear existed text
	 * 
	 * @param xpath
	 *            the element's xpath
	 * @param text
	 *            the input text
	 */
	public void type(String xpath, String text) {
		pause(stepInterval);
		// expectElementExistOrNot(true, xpath, timeout);

		// WebElement we = driver.findElement(By.xpath(xpath));
		WebElement we = expectElementExistOrNot(true, xpath, timeout);
		try {
			we.clear();
		} catch (Exception e) {
			LOGGER.warn("Failed to clear text at " + xpath);
		}
		try {
			we.sendKeys(text);
		} catch (Exception e) {
			handleFailure("Failed to type " + text + " at " + xpath);
		}

		LOGGER.info("Type " + text + " at " + xpath);
	}

	/**
	 * Hover on the page element
	 * 
	 * @param xpath
	 *            the element's xpath
	 */
	public void mouseOver(String xpath) {
		pause(stepInterval);
		expectElementExistOrNot(true, xpath, timeout);

		if (Integer.parseInt(ConfGlobalUtils.get("BrowserCoreType")) == 1) {
			Assert.fail("Mouseover is not supported for Firefox now");
		}
		if (Integer.parseInt(ConfGlobalUtils.get("BrowserCoreType")) == 2) {
			// First make mouse out of browser
			Robot rb = null;
			try {
				rb = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
			rb.mouseMove(0, 0);

			// Then hover
			WebElement we = driver.findElement(By.xpath(xpath));
			try {
				Actions builder = new Actions(driver);
				builder.moveToElement(we).build().perform();
			} catch (Exception e) {
				e.printStackTrace();
				handleFailure("Failed to mouseover " + xpath);   
			}

			LOGGER.info("Mouseover " + xpath);
			return;
		}
		if (Integer.parseInt(ConfGlobalUtils.get("BrowserCoreType")) == 3) {
			Assert.fail("Mouseover is not supported for IE now");
		}

		Assert.fail("Incorrect browser type");
	}

	/**
	 * Switch window/tab
	 * 
	 * @param windowTitle
	 *            the window/tab's title
	 */
	public void selectWindow(String windowTitle) {
		pause(stepInterval);

		driver.switchTo().window(getHandle(windowTitle));
		// browser.selectWindow(windowTitle);
		LOGGER.info("Switched to window " + windowTitle);
	}

	/*
	 * Get the Handle with windowTitle
	 * 
	 * @param windowTitle the window/tab's title
	 */
	private String getHandle(String windowTitle) {
		String handle = null;
		Set<String> handles = driver.getWindowHandles();
		for (String h : handles) {
			if (driver.switchTo().window(h).getTitle().equals(windowTitle)) {
				handle = h;
				break;
			}
		}
		if (handle == null)
			Assert.fail("Can't find the widow or tab with windowTitle: " + windowTitle);
		return handle;
	}

	public void selectPopUpWindow() {
		currenWindowHanler = getcurrentWindowHandle();
		// 寰楀埌鎵?鏈夌獥鍙ｇ殑鍙ユ焺
		Set<String> handles = driver.getWindowHandles();
		Iterator<String> it = handles.iterator();
		while (it.hasNext()) {
			String handle = it.next();
			if (currenWindowHanler.equals(handle))
				continue;
			driver.switchTo().window(handle);
			LOGGER.info("Switched to window " + driver.getTitle());

			// System.out.println("title,url =
			// "+driver.getTitle()+","+driver.getCurrentUrl());
		}

	}

	public void closePopUpWindow() {
		driver.close();
		driver.switchTo().window(currenWindowHanler);
	}

	public String getcurrentWindowHandle() {
		return driver.getWindowHandle();
	}

	/**
	 * Enter the iframe
	 * 
	 * @param location
	 *            the iframe's location
	 */
	public void enterFrame(String location) {
		pause(stepInterval);

		if (location.startsWith("//")) {
			selectFrame(location);
		} else {
			selectFrameByIdorName(location);
		}

		// logger.info("Entered iframe " + location);
	}

	/**
	 * Leave the iframe
	 */
	public void leaveFrame() {
		pause(stepInterval);
		driver.switchTo().defaultContent();
		LOGGER.info("Left the iframe");
	}

	/**
	 * Refresh the browser
	 */
	public void refresh() {
		pause(stepInterval);
		driver.navigate().refresh();
		LOGGER.info("Refreshed");
	}

	/**
	 * Mimic system-level keyboard event
	 * 
	 * @param keyCode
	 *            such as KeyEvent.VK_TAB, KeyEvent.VK_F11,KeyEvent.VK_ENTER
	 */
	public void pressKeyboard(int keyCode) {
		pause(stepInterval);
		Robot rb = null;
		try {
			rb = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		rb.keyPress(keyCode); // press key
		rb.delay(100); // delay 100ms
		rb.keyRelease(keyCode); // release key
		LOGGER.info("Pressed key with code " + keyCode);
	}

	/**
	 * Expect some text exist or not on the page<br>
	 * Expect text exist, but not found after timeout => Assert fail<br>
	 * Expect text not exist, but found after timeout => Assert fail
	 * 
	 * @param expectExist
	 *            true or false
	 * @param text
	 *            the expected text
	 * @param timeout
	 *            timeout in millisecond
	 */
	public void expectTextExistOrNot(boolean expectExist, final String text, int timeout) {
		if (expectExist) {
			if (isTextPresent(text, timeout)) {
				LOGGER.info("Found desired text " + text);
			} else {
				handleFailure("Not found desired text " + text);
			}

		} else {
			// if (isTextPresent(text, timeout)&&
			// isElementPresent("//*[contains(text(),'" + text + "')]",timeout))
			// {
			if (isTextPresent(text, timeout)) {
				handleFailure("Found undesired text " + text);
			} else {
				LOGGER.info("Not found undesired text " + text);
			}
		}
	}

	/**
	 * Expect some text exist or not on the element<br>
	 * Expect text exist, but not found after timeout => Assert fail<br>
	 * Expect text not exist, but found after timeout => Assert fail
	 * 
	 * @param expectExist
	 *            true or false
	 * @param expectExist
	 *            the expected element xpath
	 * @param text
	 *            the expected text
	 * @param timeout
	 *            timeout in millisecond
	 */
	public void expectTextExistOnElementOrNot(boolean expectExist, final String xpath, final String text, int timeout) {
		if (expectExist) {

			if (isTextPresentOnElement(xpath, text, timeout)) {
				LOGGER.info("Found desired text " + text + " on the element" + xpath);
			} else {
				handleFailure("Failed to find text " + text + " on the element" + xpath + ", text of which is "
						+ driver.findElement(By.xpath(xpath)).getText());
			}

		} else {
			if (isTextPresentOnElement(xpath, text, -1)) {
				handleFailure("Found undesired text " + text + " on the element" + xpath);
			} else {
				LOGGER.info("Not found undesired text " + text + " on the element" + xpath);
			}
		}
	}

	/**
	 * Expect an element exist or not on the page<br>
	 * Expect element exist, but not found after timeout => Assert fail<br>
	 * Expect element not exist, but found after timeout => Assert fail<br>
	 * Here <b>exist</b> means <b>visible</b>
	 * 
	 * @param expectExist
	 *            true or false
	 * @param xpath
	 *            the expected element's xpath
	 * @param timeout
	 *            timeout in millisecond
	 */
	public WebElement expectElementExistOrNot(boolean expectExist, final String xpath, int timeout) {
		WebElement element = null;
		if (expectExist) {
			try {
				element = new WebDriverWait(driver, timeout / 1000)
						.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
				LOGGER.info("Found desired element " + xpath);
			} catch (Exception e) {
				LOGGER.error("Not found desired element " + xpath);
				ExtentManager.errorHandle("Not found desired element :" + xpath, getShotpath());
			}

		} else {

			try {
				new WebDriverWait(driver, timeout / 6000)
						.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
				handleFailure("Found undesired element " + xpath);
			} catch (Exception e) {
				LOGGER.error("Not found undesired element " + xpath);
				ExtentManager.errorHandle("Not found undesired element :" + xpath, getShotpath());
			}

			return null;
		}
		return element;
	}

	/**
	 * is an element attribute exist or not on the page<br>
	 * Expect element attribute exist, but not found after timeout => Assert
	 * fail<br>
	 * Expect element attribute not exist, but found after timeout => Assert
	 * fail<br>
	 * Here <b>exist</b> means <b>visible</b>
	 * 
	 * @param expectExist
	 *            true or false
	 * @param xpath
	 *            the expected element's xpath
	 * @param timeout
	 *            timeout in millisecond
	 */
	public boolean isElementAttributeExist(final String xpath, final String attribute, int timeout) {
		String value = expectElementExistOrNot(true, xpath, timeout).getAttribute(attribute);
		// String value =
		// driver.findElement(By.xpath(xpath)).getAttribute(attribute);
		if (value != null) {
			LOGGER.info("Found the attribut '" + attribute + "' on the element, value is :" + value);
			return true;
		} else {
			handleFailure("Failed to find attribute " + attribute + " on the element " + xpath);
			return false;
		}
	}

	/**
	 * Expect some attribute value exist or not on the Element<br>
	 * Expect text exist, but not found after timeout => Assert fail<br>
	 * Expect text not exist, but found after timeout => Assert fail
	 * 
	 * @param xpath
	 *            the expected element's xpath
	 * @param attribute
	 *            the attribute
	 * @param ExpectValue
	 *            the expected attribute value
	 * @param timeout
	 *            timeout in millisecond
	 */
	public void expectElementAttributeExist(final String xpath, final String attribute, String ExpectValue,
			int timeout) {
		isElementAttributeExist(xpath, attribute, timeout);
		String attributeValue = driver.findElement(By.xpath(xpath)).getAttribute(attribute).trim();
		if (!attributeValue.equals(ExpectValue.trim())) {
			handleFailure(
					"The attribute value is " + attributeValue + " on the element " + xpath + " , not " + ExpectValue);
		}

	}

	public void expectElementsCount(String xpath, int num, int timeout) {
		expectElementExistOrNot(true, xpath, timeout);
		int count = driver.findElements(By.xpath(xpath)).size();
		if (!(count == num)) {
			handleFailure("Elements " + xpath + " count is " + count + " , not " + num);
		}
	}

	public void expectTitleContains(String expectTitle, int timeout) {
		pause(stepInterval);
		String actualTitle = "";

		try {
			new WebDriverWait(driver, timeout / 1000).until(ExpectedConditions.titleContains(expectTitle));
			actualTitle = driver.getTitle();
			LOGGER.info("Found title" + actualTitle + ", contains " + expectTitle);
		} catch (Exception e) {
			actualTitle = driver.getTitle();
			handleFailure("This page title is " + actualTitle + ", not contains " + expectTitle);
		}
	}

	public void expectTitleExist(String expectTitle, int timeout) {
		pause(stepInterval);
		String actualTitle = "";

		try {
			new WebDriverWait(driver, timeout / 1000).until(ExpectedConditions.titleIs(expectTitle));
			LOGGER.info("Found title" + expectTitle);
		} catch (Exception e) {
			actualTitle = driver.getTitle();
			LOGGER.info("Found title" + actualTitle);
			handleFailure("This page title is " + actualTitle + ", not " + expectTitle);
		}
	}

	/**
	 * Is the text present on the page
	 * 
	 * @param text
	 *            the expected text
	 * @param time
	 *            wait a moment (in millisecond) before search text on page;<br>
	 *            minus time means search text at once
	 * @return
	 */
	public boolean isTextPresent(String text, int time) {
		pause(stepInterval);
		// boolean isPresent = browser.isTextPresent(text);
		boolean isPresent = driver.findElement(By.tagName("body")).getText().contains(text);
		if (isPresent) {
			LOGGER.info("Found text " + text);
			return true;
		} else {
			LOGGER.info("Not found text " + text);
			return false;
		}
	}

	/**
	 * Is the text present on the element
	 * 
	 * @param xpath
	 *            the expected element xpath
	 * @param text
	 *            the expected text
	 * @param time
	 *            wait a moment (in millisecond) before search text on page;<br>
	 *            minus time means search text at once
	 * @return
	 */
	public boolean isTextPresentOnElement(String xpath, String text, int time) {
		pause(stepInterval);
		// expectElementExistOrNot(true, xpath, timeout);
		try {
			WebElement element = expectElementExistOrNot(true, xpath, timeout);
			// WebElement element = driver.findElement(By.xpath(xpath));
			new WebDriverWait(driver, timeout / 1000).until(ExpectedConditions.textToBePresentInElement(element, text));
			LOGGER.info("Found text " + text);
			return true;
		} catch (Exception e) {
			LOGGER.info("Not found text " + text);
			return false;
		}
	}

	/*
	 * check link is broken or not
	 * 
	 * @Param url css is in the page of this url
	 * 
	 * @Param css by this css locator which the links is located
	 * 
	 * @removerWords maybe the css locator element has more words than title,
	 * which should be removed
	 * 
	 * public void checkLinkisBrokenOrNot(String url, String css, String...
	 * removeWords) {
	 * 
	 * Document doc = getDoc("", url); logger.info("Init Jsoup Document for " +
	 * url + " successfully"); Elements links = doc.select(css); String text =
	 * ""; String linkUrl = ""; String linkTitle = ""; for (Element link :
	 * links) { text = link.text(); linkUrl = link.absUrl("href"); Document
	 * linkDoc = getDoc(text, linkUrl); linkTitle = linkDoc.title().trim(); if
	 * (!linkTitle.contains(text) && !removeSpecialCharacter(linkTitle,
	 * removeWords) .contains(removeSpecialCharacter(text, removeWords)) &&
	 * linkDoc.title().contains("Page not found")) {
	 * handleFailure("the link text is " + text +
	 * ", but the link page title is " + linkTitle); } logger.info("The link " +
	 * linkUrl + " of the text " + text + " is OK"); } }
	 */
	/*
	 * Remove special character in a string,just leave letters and numbers
	 * 
	 * @param str a string which need to remove special character
	 * 
	 * @return a string
	 * 
	 * private String removeSpecialCharacter(String str, String... removeWorlds)
	 * { String result = "";
	 * 
	 * if (!removeWorlds.equals("")) { for (String s : removeWorlds) { str =
	 * str.replace(s, ""); } } result = str.replaceAll("[^\\s|a-z0-9A-Z]", "")
	 * .replaceAll("\\s{2,}", " ").trim();
	 * 
	 * return result; }
	 * 
	 * public Document getDoc(String text, String url) { url =
	 * url.replaceAll("\\s", "%20"); try { Document doc = Jsoup .connect(url)
	 * .ignoreContentType(true) .userAgent(
	 * "Mozilla/5.0 (Windows NT 6.1; rv:23.0) Gecko/20100101 Firefox/23.0")
	 * .timeout(timeout).get(); logger.info("Init Jsoup Document for " + url +
	 * " successfully");
	 * 
	 * return doc; } catch (IOException e) { if (text.equals("")) {
	 * handleFailure("Cann't access " + url); } else
	 * handleFailure("Cann't access " + url + " for text " + text); } return
	 * null; }
	 */
	/**
	 * Is the element present on the page<br>
	 * Here <b>present</b> means <b>visible</b>
	 * 
	 * @param xpath
	 *            the expected element's xpath
	 * @param time
	 *            wait a moment (in millisecond) before search element on
	 *            page;<br>
	 *            minus time means search element at once
	 * @return
	 */
	public boolean isElementPresent(String xpath, int time) {
		pause(stepInterval);

		boolean isPresent = false;
		try {
			isPresent = driver.findElement(By.xpath(xpath)).isDisplayed();
		} catch (Exception e) {
			isPresent = false;
		}

		if (isPresent) {
			LOGGER.info("Found element " + xpath);
			return true;
		} else {
			LOGGER.info("Not found element" + xpath);
			return false;
		}
	}

	public void addCookice(String name, String value, String domain, String path, Date expiry) {
		Cookie cookie = new Cookie(name, value, domain, path, expiry);
		driver.manage().addCookie(cookie);
	}

	/**
	 * Pause
	 * 
	 * @param time
	 *            in millisecond
	 */
	private void pause(int time) {
		if (time <= 0) {
			return;
		}
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.out.println("sleep doesn't work...");
		}
	}

	public void handleFailure(String notice) {

		String png = LogTools.screenShot(this);
		String log = notice + " >> capture screenshot at " + png;

		LOGGER.error(log);
		// Assert.fail(log);
	}
	public String getShotpath(){
		return LogTools.screenShot(this);
	}

	private boolean getProcess(String process) {
		boolean flag = false;
		Process p;
		try {
			p = Runtime.getRuntime().exec("cmd /c tasklist");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream os = p.getInputStream();
			byte b[] = new byte[256];
			while (os.read(b) > 0)
				baos.write(b);

			String s = baos.toString();

			if (s.indexOf(process) >= 0) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (IOException e) {

		}

		return flag;
	}

}
