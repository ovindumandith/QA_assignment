package com.orangehrm.qa.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
// import org.openqa.selenium.firefox.FirefoxDriver;
// import org.openqa.selenium.firefox.FirefoxOptions;
// import org.openqa.selenium.edge.EdgeDriver;
// import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;

/**
 * Thread-safe WebDriver lifecycle manager.
 *
 * <p>Uses a {@link ThreadLocal} so each test thread owns its own {@link WebDriver}
 * instance, enabling safe parallel execution without shared state.
 *
 * <p>Typical usage pattern in a test base class:
 * <pre>
 *   DriverManager.initDriver();          // @BeforeMethod
 *   WebDriver driver = DriverManager.getDriver();
 *   DriverManager.quitDriver();          // @AfterMethod
 * </pre>
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /** Private constructor — all members are static; instantiation is not intended. */
    private DriverManager() {}

    /**
     * Initialises a new {@link WebDriver} instance for the current thread.
     *
     * <p>The browser type is read from {@code config.properties} via
     * {@link ConfigReader#get(String)} using the key {@code "browser"}.
     * Currently Chrome is fully supported; Firefox and Edge stubs are commented
     * out and ready to activate.
     *
     * <p>The browser window is maximised and an implicit wait is configured from
     * the {@code "implicit.wait"} property.
     *
     * @throws RuntimeException if the configured browser type is unrecognised
     */
    public static void initDriver() {
        String browser = ConfigReader.get("browser").toLowerCase();
        WebDriver driver;

        switch (browser) {
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-notifications");
                options.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(options);
            }

            // case "firefox" -> {
            //     WebDriverManager.firefoxdriver().setup();
            //     FirefoxOptions options = new FirefoxOptions();
            //     driver = new FirefoxDriver(options);
            // }

            // case "edge" -> {
            //     WebDriverManager.edgedriver().setup();
            //     EdgeOptions options = new EdgeOptions();
            //     driver = new EdgeDriver(options);
            // }

            default -> throw new RuntimeException(
                    "Unsupported browser configured: '" + browser
                            + "'. Supported values: chrome, firefox, edge.");
        }

        driver.manage().window().maximize();
        driver.manage().timeouts()
              .implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicit.wait")));

        driverThreadLocal.set(driver);
    }

    /**
     * Returns the {@link WebDriver} instance bound to the current thread.
     *
     * <p>Must be called after {@link #initDriver()} has been invoked on the
     * same thread.
     *
     * @return the thread-local {@link WebDriver}, or {@code null} if not yet initialised
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Quits the {@link WebDriver} for the current thread and removes it from
     * the {@link ThreadLocal} to prevent memory leaks.
     *
     * <p>Safe to call even if the driver was never initialised (no-op in that case).
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
