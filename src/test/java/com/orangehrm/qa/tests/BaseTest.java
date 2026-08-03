package com.orangehrm.qa.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.orangehrm.qa.utils.ConfigReader;
import com.orangehrm.qa.utils.DriverManager;
import com.orangehrm.qa.utils.ExtentReportManager;
import com.orangehrm.qa.utils.ScreenshotUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.time.Duration;

/**
 * Abstract base class for all TestNG test classes in the framework.
 *
 * <p>Manages the full lifecycle of the WebDriver and the Extent HTML report:
 * <ul>
 *   <li>{@link #beforeSuite()} — initialises the Extent report once.</li>
 *   <li>{@link #beforeMethod()} — launches a fresh browser before every test.</li>
 *   <li>{@link #afterMethod(ITestResult)} — logs the result, captures a
 *       screenshot on failure, then quits the browser.</li>
 *   <li>{@link #afterSuite()} — flushes the Extent report to disk once.</li>
 * </ul>
 *
 * <p>Subclasses receive:
 * <ul>
 *   <li>{@link #extent} — the singleton {@link ExtentReports} instance.</li>
 *   <li>{@link #test} — the per-test {@link ExtentTest} node; each test method
 *       must assign this field before calling any logging methods.</li>
 * </ul>
 */
public abstract class BaseTest {

    /** Singleton Extent Reports instance shared across all tests in the suite. */
    protected ExtentReports extent;

    /**
     * Per-test Extent node.  Must be assigned in each {@code @Test} method
     * via {@link ExtentReportManager#createTest(String)}.
     */
    protected ExtentTest test;

    // -------------------------------------------------------------------------
    // Suite-level hooks
    // -------------------------------------------------------------------------

    /**
     * Initialises the Extent HTML report before any test in the suite runs.
     * Called exactly once per suite execution.
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        ExtentReportManager.initReport();
        extent = ExtentReportManager.getReport();
    }

    /**
     * Flushes all pending log entries to the Extent HTML report file after
     * the last test in the suite has finished. Called exactly once per suite
     * execution.
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        ExtentReportManager.flushReport();
    }

    // -------------------------------------------------------------------------
    // Method-level hooks
    // -------------------------------------------------------------------------

    /**
     * Launches a new browser instance and navigates to the application URL
     * before each individual test method.
     *
     * <p>Rather than a fixed sleep, an explicit wait polls until the login
     * username field is visible (up to 30 s). This handles both fast and slow
     * loads of the React SPA without wasting time on fast machines.
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        DriverManager.initDriver();
        DriverManager.getDriver().get(ConfigReader.get("base.url"));
        // Wait for the SPA to render the login form — more reliable than Thread.sleep
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(30))
                .until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
    }

    /**
     * Records the test result in the Extent report, takes a screenshot on
     * failure, and shuts down the browser after each individual test method.
     *
     * <p>This method always quits the driver regardless of whether the test
     * passed or failed.
     *
     * @param result the {@link ITestResult} injected by TestNG, containing the
     *               pass/fail status and any thrown exception
     */
    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        if (test != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                String screenshotPath = ScreenshotUtil.takeScreenshot(
                        DriverManager.getDriver(), result.getName());
                if (!screenshotPath.isEmpty()) {
                    try {
                        test.addScreenCaptureFromPath(screenshotPath,
                                "Failure screenshot — " + result.getName());
                    } catch (Exception e) {
                        test.info("Screenshot could not be attached: " + e.getMessage());
                    }
                }
                test.fail(result.getThrowable());
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                test.pass("Test passed successfully");
            } else if (result.getStatus() == ITestResult.SKIP) {
                test.skip("Test skipped: " + result.getThrowable());
            }
        }
        DriverManager.quitDriver();
    }
}
