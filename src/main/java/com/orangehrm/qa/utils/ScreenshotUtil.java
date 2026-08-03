package com.orangehrm.qa.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for capturing and persisting browser screenshots.
 *
 * <p>Screenshots are named {@code <testName>_<timestamp>.png} and written to
 * the directory defined by the {@code "screenshot.path"} property in
 * {@code config.properties}.  The directory is created automatically if it
 * does not already exist.
 */
public class ScreenshotUtil {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtil.class);
    private static final DateTimeFormatter TIMESTAMP_FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /** Private constructor — all members are static; instantiation is not intended. */
    private ScreenshotUtil() {}

    /**
     * Captures the current browser viewport as a PNG screenshot and saves it
     * to the configured screenshots directory.
     *
     * <p>The file is placed at:
     * {@code <screenshot.path>/<testName>_<yyyyMMdd_HHmmss>.png}
     *
     * <p>The screenshots directory is created if it does not already exist.
     * Any {@link IOException} is logged rather than propagated so that a
     * screenshot failure never masks the underlying test failure.
     *
     * @param driver   the active {@link WebDriver} instance; must implement
     *                 {@link TakesScreenshot}
     * @param testName a short, file-system-safe name for the test (e.g.
     *                 {@code "TC01_ValidLogin"}) used to build the file name
     * @return the absolute path of the saved screenshot file, or an empty
     *         string if the capture failed
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        String screenshotDir = ConfigReader.get("screenshot.path");
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FMT);
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = screenshotDir + fileName;

        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);

            // Create the target directory tree if it does not exist
            File parentDir = destFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                Files.createDirectories(parentDir.toPath());
            }

            Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            log.info("Screenshot saved: {}", destFile.getAbsolutePath());
            return destFile.getAbsolutePath();

        } catch (IOException e) {
            log.error("Failed to save screenshot for test '{}': {}", testName, e.getMessage(), e);
            return "";
        }
    }
}
