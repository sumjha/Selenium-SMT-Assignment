package com.smt.group.f.assignment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;


public class SeleniumAutomation {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private static final int TIMEOUT_SECONDS = 2;
    private InputHelper inputHelper;

    public SeleniumAutomation() {
        initializeDriver();
        inputHelper = new InputHelper(driver, wait);
    }
    

    private void initializeDriver() {
        try {
            String chromeDriverPath = "chromedriver-mac-arm64/chromedriver";   
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
            // Initialize WebDriver
            driver = new ChromeDriver();
            wait = new WebDriverWait(driver, TIMEOUT_SECONDS);
            
            System.out.println("Chrome driver initialized successfully from: " + chromeDriverPath);
            
        } catch (Exception e) {
            System.err.println("Failed to initialize Chrome driver: " + e.getMessage());
            throw new RuntimeException("Chrome driver initialization failed", e);
        }
    }


    public void test_search_flight(String source, String destination) throws InterruptedException {
        System.out.println("Testing search flight functionality...");
        System.out.println("Source: " + source);
        System.out.println("Destination: " + destination);
        driver.get("https://www.google.com/travel/flights");
        wait.until(ExpectedConditions.titleContains("Google Flights"));
        System.out.println("✓ Google Flights page loaded successfully");
        select_one_way_flight();
        Thread.sleep(500);
        inputHelper.clickAndType("Where from?", source);
        Thread.sleep(500);
        inputHelper.clickAndType("Where to?", destination);
        inputHelper.clickAndType("Departure", "Wed, Nov 12");
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).perform();
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("button[aria-label='Search']")).click();
    }
 
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
            System.out.println("Chrome driver closed successfully");
        }
    }

    public void select_one_way_flight() {
        WebElement hiddenSpan = driver.findElement(By.xpath("//span[@aria-label='Change ticket type.']"));
        WebElement parent = hiddenSpan.findElement(By.xpath(".."));
        wait.until(ExpectedConditions.elementToBeClickable(parent));
        parent.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='One way']/ancestor::li[@role='option']")));
        WebElement oneWay = driver.findElement(
            By.xpath("//span[text()='One way']/ancestor::li[@role='option']")
        );
        oneWay.click();
    }

    public void sameLocation(){
        By locator = By.xpath("//div[text()='No results returned.']");
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        List<WebElement> elements = driver.findElements(locator);
        if (elements.size() <= 0){
            throw new RuntimeException("Test failed");
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        SeleniumAutomation test = null;
        test = new SeleniumAutomation();

        ExcelUtility excelUtility = new ExcelUtility();
        //excelUtility.printAllCellValues();

        for (int i = ExcelUtility.TEST_CASE_START_ROW; i < ExcelUtility.TEST_CASE_END_ROW; i++) {
            try{
            String testCaseID = excelUtility.getCellValue(i, 0);
            String inputData1 = excelUtility.getCellValue(i, 4);
            String inputData2 = excelUtility.getCellValue(i, 5);
            System.out.println("Test Case ID: " + testCaseID);
            switch (testCaseID) {
                case "TC1_Search_flight":
                case "TC2_Search_flight":
                        test.test_search_flight(inputData1, inputData2);
                        break;
                case "TC3_Search_flight":
                        test.test_search_flight(inputData1, inputData1);
                        test.sameLocation();
                        break;

            }
            excelUtility.setTestStatus(i, ExcelUtility.PASS);
            excelUtility.saveWorkbook(); 
            Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("Test execution failed: " + e.getMessage());
                e.printStackTrace();
                excelUtility.setTestStatus(i, ExcelUtility.FAIL);
        } finally{
            excelUtility.saveWorkbook();
        }
        Thread.sleep(1000);
        } 
       // test.closeDriver();
    }
    }

