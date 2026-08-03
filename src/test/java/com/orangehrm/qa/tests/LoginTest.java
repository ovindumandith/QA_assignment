package com.orangehrm.qa.tests;

import com.orangehrm.qa.pages.DashboardPage;
import com.orangehrm.qa.pages.LoginPage;
import com.orangehrm.qa.utils.ConfigReader;
import com.orangehrm.qa.utils.DriverManager;
import com.orangehrm.qa.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class covering all login-related scenarios for the OrangeHRM application.
 *
 * <p>All five test cases exercise the Login page and verify the expected
 * behaviour for valid credentials, invalid credentials, and empty-field
 * validation.  Results are logged to the Extent HTML report via the inherited
 * {@code test} field from {@link BaseTest}.
 *
 * <p>Test execution order is controlled by the {@code priority} attribute on
 * each {@code @Test} annotation.
 */
public class LoginTest extends BaseTest {

    // -------------------------------------------------------------------------
    // Test cases
    // -------------------------------------------------------------------------

    /**
     * TC-01: Verifies that a user can log in successfully with valid credentials
     * and that the Dashboard page is displayed afterwards.
     */
    @Test(
        description = "TC-01: Verify successful login with valid credentials",
        groups = {"smoke", "regression"},
        priority = 1
    )
    public void TC01_ValidLoginTest() {
        test = ExtentReportManager.createTest("TC-01 Valid Login");
        test.info("Navigating to login page");

        LoginPage loginPage = new LoginPage(DriverManager.getDriver());

        test.info("Entering valid credentials");
        loginPage.login(
            ConfigReader.get("admin.username"),
            ConfigReader.get("admin.password")
        );

        test.info("Verifying dashboard is displayed");
        DashboardPage dashboardPage = new DashboardPage(DriverManager.getDriver());

        Assert.assertTrue(
            dashboardPage.isDashboardDisplayed(),
            "Dashboard should be displayed after valid login"
        );

        String heading = dashboardPage.getDashboardHeadingText();
        Assert.assertEquals(
            heading,
            "Dashboard",
            "Dashboard heading text mismatch"
        );

        test.pass("Login successful. Dashboard heading: " + heading);
    }

    /**
     * TC-02: Verifies that an appropriate error message is shown when the
     * correct username is supplied with an incorrect password, and that the
     * browser remains on the Login page.
     */
    @Test(
        description = "TC-02: Verify error message with invalid password",
        groups = {"smoke", "regression"},
        priority = 2
    )
    public void TC02_InvalidPasswordTest() {
        test = ExtentReportManager.createTest("TC-02 Invalid Password");
        test.info("Navigating to login page");

        LoginPage loginPage = new LoginPage(DriverManager.getDriver());

        test.info("Entering valid username with invalid password");
        loginPage.login("Admin", "InvalidPass@999");

        test.info("Verifying error message is displayed");
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(
            errorMsg.contains("Invalid credentials"),
            "Expected 'Invalid credentials' in error message but got: " + errorMsg
        );

        test.info("Verifying browser remains on login page");
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(
            currentUrl.contains("auth/login"),
            "Expected URL to contain 'auth/login' but was: " + currentUrl
        );

        test.pass("Error message verified: " + errorMsg);
    }

    /**
     * TC-03: Verifies that a "Required" validation message appears below the
     * username field when login is attempted with an empty username.
     */
    @Test(
        description = "TC-03: Verify validation when username is empty",
        groups = {"regression"},
        priority = 3
    )
    public void TC03_EmptyUsernameTest() {
        test = ExtentReportManager.createTest("TC-03 Empty Username");
        test.info("Navigating to login page");

        LoginPage loginPage = new LoginPage(DriverManager.getDriver());

        test.info("Leaving username empty, entering valid password, then submitting");
        loginPage.enterPassword(ConfigReader.get("admin.password"));
        loginPage.clickLoginButton();

        test.info("Verifying validation message is present");
        List<String> messages = loginPage.getAllValidationMessages();
        Assert.assertTrue(
            messages.size() >= 1,
            "Expected at least 1 validation message but found: " + messages.size()
        );
        Assert.assertEquals(
            messages.get(0),
            "Required",
            "First validation message should be 'Required'"
        );

        test.pass("Validation message verified: " + messages);
    }

    /**
     * TC-04: Verifies that a "Required" validation message appears below the
     * password field when login is attempted with an empty password.
     */
    @Test(
        description = "TC-04: Verify validation when password is empty",
        groups = {"regression"},
        priority = 4
    )
    public void TC04_EmptyPasswordTest() {
        test = ExtentReportManager.createTest("TC-04 Empty Password");
        test.info("Navigating to login page");

        LoginPage loginPage = new LoginPage(DriverManager.getDriver());

        test.info("Entering valid username, leaving password empty, then submitting");
        loginPage.enterUsername(ConfigReader.get("admin.username"));
        loginPage.clickLoginButton();

        test.info("Verifying 'Required' validation message is present");
        List<String> messages = loginPage.getAllValidationMessages();
        Assert.assertTrue(
            messages.contains("Required"),
            "Expected a 'Required' validation message but got: " + messages
        );

        test.pass("Validation message verified: " + messages);
    }

    /**
     * TC-05: Verifies that exactly two "Required" validation messages appear —
     * one for the username field and one for the password field — when the
     * Login button is clicked without entering either credential.
     */
    @Test(
        description = "TC-05: Verify validation when both fields are empty",
        groups = {"regression"},
        priority = 5
    )
    public void TC05_BothFieldsEmptyTest() {
        test = ExtentReportManager.createTest("TC-05 Both Fields Empty");
        test.info("Navigating to login page");

        LoginPage loginPage = new LoginPage(DriverManager.getDriver());

        test.info("Leaving both fields empty and clicking Login");
        loginPage.clickLoginButton();

        test.info("Verifying exactly 2 'Required' validation messages are shown");
        List<String> messages = loginPage.getAllValidationMessages();
        Assert.assertEquals(
            messages.size(),
            2,
            "Expected exactly 2 validation messages but found: " + messages.size()
        );
        Assert.assertEquals(messages.get(0), "Required",
            "First validation message should be 'Required'");
        Assert.assertEquals(messages.get(1), "Required",
            "Second validation message should be 'Required'");

        test.pass("Both 'Required' validation messages confirmed: " + messages);
    }
}
