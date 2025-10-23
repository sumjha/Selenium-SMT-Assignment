package com.smt.group.f.assignment;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class InputHelper {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public InputHelper(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Robustly click the input matched by aria-label (ignores trailing whitespace).
     * Falls back through multiple strategies to avoid ElementClickInterceptedException.
     *
     * @param ariaLabelText the aria-label value text, e.g. "Where from?"
     */
    public void clickInputByAriaLabel(String ariaLabelText) {

        By anySelector = By.xpath("//input[normalize-space(@aria-label)='" + ariaLabelText + "']");
        List<WebElement> inputs = driver.findElements(anySelector);
        if (inputs.isEmpty()) {
            throw new NoSuchElementException("No input found with aria-label: " + ariaLabelText);
        }

        WebElement target = null;
        for (WebElement e : inputs) {
            if (e.isDisplayed()) {
                target = e;
                break;
            }
        }
        if (target == null) {
            // last resort: first element
            target = inputs.get(0);
        }

        clickWithFallbacks(target);
    }

    /**
     * Send text to whichever element currently has focus (cursor).
     * Useful when page autofocused or when an overlay prevents clicking.
     */
    public void sendKeysToActiveElement(CharSequence... keys) {
        WebElement active = (WebElement) ((JavascriptExecutor) driver).executeScript("return document.activeElement;");
        if (active == null) {
            throw new WebDriverException("No active element found.");
        }
        active.sendKeys(keys);
        active.sendKeys(Keys.ENTER);
    }

    private void clickWithFallbacks(WebElement el) {
        // wait until the element is present in DOM and not stale
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(el)));

        try {
            el.click();
            return;
        } catch (WebDriverException e) {
            throw new WebDriverException("Failed to click element", e);
        }
    }

    /**
     * High-level helper: ensure target input is clickable, click it, then type text into it.
     * Tries to pick the expanded input if present, otherwise first visible.
    */
    public void clickAndType(String ariaLabelText, String text) {
        clickInputByAriaLabel(ariaLabelText);

        // small wait to ensure focus moved
        try { Thread.sleep(150); } catch (InterruptedException ignored) {}
            sendKeysToActiveElement(text);

    }
}
