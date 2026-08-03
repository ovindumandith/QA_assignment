package com.orangehrm.qa.pages;

import com.orangehrm.qa.utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Abstract base class for all Page Object classes in the framework.
 *
 * <p>Provides a shared {@link WebDriver} reference, a pre-configured
 * {@link WebDriverWait}, and a set of protected helper methods that wrap
 * common Selenium interactions with explicit waits.  Every page class must
 * extend this class and call {@code super(driver)} from its own constructor
 * so that {@link PageFactory} element initialisation occurs consistently.
 */
public abstract class BasePage {

    private static final Logger log = LoggerFactory.getLogger(BasePage.class);

    /** Shared WebDriver instance injected via the constructor. */
    protected WebDriver driver;

    /** Explicit wait configured with the {@code "explicit.wait"} property. */
    protected WebDriverWait wait;

    /**
     * Constructs a new {@code BasePage}, storing the driver, initialising the
     * explicit wait, and triggering {@link PageFactory} element injection.
     *
     * @param driver the active {@link WebDriver} instance for this page
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(ConfigReader.getInt("explicit.wait")));
        PageFactory.initElements(driver, this);
    }

    /**
     * Waits until the given element is clickable and then clicks it.
     *
     * <p>If an overlay intercepts the normal click (e.g. a loading spinner
     * covering the button), the method falls back to a JavaScript click so
     * the interaction still reaches the correct target element.
     *
     * @param element the target {@link WebElement}
     */
    protected void click(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException e) {
            log.warn("Normal click intercepted — retrying via JavaScript click");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Waits until the given element is visible, clears any existing content,
     * and then types the supplied text.
     *
     * @param element the target input {@link WebElement}
     * @param text    the text to enter
     */
    protected void type(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Waits until the given element is visible and returns its trimmed text.
     *
     * @param element the target {@link WebElement}
     * @return the visible text of the element with leading/trailing whitespace removed
     */
    protected String getText(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText().trim();
    }

    /**
     * Checks whether the given element is visible within the explicit wait
     * timeout without throwing an exception.
     *
     * @param element the target {@link WebElement}
     * @return {@code true} if the element becomes visible before the timeout;
     *         {@code false} if a {@link TimeoutException} is caught
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Blocks until the browser reports that the page has fully loaded by
     * polling {@code document.readyState} via {@link JavascriptExecutor}.
     */
    protected void waitForPageLoad() {
        wait.until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState")
                .equals("complete"));
    }

    /**
     * Scrolls the viewport so that the given element is within view, using
     * the {@code scrollIntoView} JavaScript function.
     *
     * @param element the target {@link WebElement} to scroll to
     */
    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Waits for an element located by the given {@link By} strategy to become
     * visible and returns it.
     *
     * @param locator the {@link By} locator strategy
     * @return the visible {@link WebElement}
     */
    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
