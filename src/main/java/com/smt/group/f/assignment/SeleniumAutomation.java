package com.smt.group.f.assignment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;


public class SeleniumAutomation {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private static final int TIMEOUT_SECONDS = 10;
    

    public SeleniumAutomation() {
        initializeDriver();
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

    public void test_search_flight(String inputData1, String inputData2) throws InterruptedException {
        System.out.println("Testing search flight functionality...");
        System.out.println("Input data 1: " + inputData1);
        System.out.println("Input data 2: " + inputData2);
        driver.get("https://www.google.com/travel/flights");
        wait.until(ExpectedConditions.titleContains("Google Flights"));
        System.out.println("✓ Google Flights page loaded successfully");
        WebElement fromInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[aria-label='Where else?']")));
        fromInput.click();
        fromInput.clear();
        Thread.sleep(1000);
        fromInput.sendKeys(inputData1);
        Thread.sleep(1000);
        fromInput.sendKeys(Keys.ENTER);
        Thread.sleep(1000);
        WebElement toInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[aria-label='Where to? ']")));
        toInput.click();
        toInput.sendKeys(inputData2);
       // toInput.sendKeys(Keys.ENTER);
        WebElement searchButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[aria-label='Explore destinations']")));
        searchButton.click();
        }
    
    
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
            System.out.println("Chrome driver closed successfully");
        }
    }
    
    public static void main(String[] args) throws InterruptedException {


        SeleniumAutomation test = new SeleniumAutomation();
        ExcelUtility excelUtility = new ExcelUtility();
        //excelUtility.printAllCellValues();

        for (int i = ExcelUtility.TEST_CASE_START_ROW; i < ExcelUtility.TEST_CASE_END_ROW; i++) {
            String testCaseID = excelUtility.getCellValue(i, 0);
            String inputData1 = excelUtility.getCellValue(i, 4);
            String inputData2 = excelUtility.getCellValue(i, 5);
            System.out.println("Test Case ID: " + testCaseID);
            switch (testCaseID) {
                case "TC1_Search_flight": test.test_search_flight(inputData1, inputData2);
                    break;

            }
            excelUtility.saveWorkbook(); 
        }
        

        test.closeDriver();
    }
}
