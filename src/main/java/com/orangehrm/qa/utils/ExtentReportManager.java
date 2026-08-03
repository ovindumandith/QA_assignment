package com.orangehrm.qa.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Singleton manager for the Extent Reports HTML test report.
 *
 * <p>Call {@link #initReport()} once before the test suite runs, then use
 * {@link #createTest(String)} inside each test method to obtain an
 * {@link ExtentTest} for logging steps. Call {@link #flushReport()} at the
 * end of the suite to write the HTML file to disk.
 *
 * <p>The generated report is placed at {@code reports/ExtentReport.html}
 * relative to the working directory (typically the project root when run with
 * Maven).
 */
public class ExtentReportManager {

    private static ExtentReports extentReports;

    /** Private constructor — all members are static; instantiation is not intended. */
    private ExtentReportManager() {}

    /**
     * Initialises the {@link ExtentReports} instance and configures the
     * {@link ExtentSparkReporter} with report metadata and system information.
     *
     * <p>Must be called exactly once, typically from a {@code @BeforeSuite}
     * method in the base test class.
     */
    public static void initReport() {
        ExtentSparkReporter sparkReporter =
                new ExtentSparkReporter("reports/ExtentReport.html");

        sparkReporter.config().setReportName("OrangeHRM QA Automation Report");
        sparkReporter.config().setDocumentTitle("OrangeHRM Test Results");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setEncoding("UTF-8");

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);

        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("Author", "QA Engineer");
        extentReports.setSystemInfo("Application URL", ConfigReader.get("base.url"));
        extentReports.setSystemInfo("Browser", ConfigReader.get("browser"));
    }

    /**
     * Returns the singleton {@link ExtentReports} instance.
     *
     * <p>{@link #initReport()} must have been called before this method.
     *
     * @return the active {@link ExtentReports} instance
     */
    public static ExtentReports getReport() {
        return extentReports;
    }

    /**
     * Writes all pending test logs to the HTML report file.
     *
     * <p>Must be called once after all tests have finished, typically from an
     * {@code @AfterSuite} method in the base test class.
     */
    public static void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    /**
     * Creates and registers a new {@link ExtentTest} node in the report.
     *
     * <p>The returned instance should be stored in the calling test's
     * {@code test} field so that subsequent {@code info}, {@code pass},
     * {@code fail}, and {@code skip} calls are logged under the correct node.
     *
     * @param testName a human-readable name for the test (e.g.
     *                 {@code "TC-01 Valid Login"})
     * @return a new {@link ExtentTest} node ready for logging
     */
    public static ExtentTest createTest(String testName) {
        return extentReports.createTest(testName);
    }
}
