package com.orangehrm.qa.tests;

import com.orangehrm.qa.pages.DashboardPage;
import com.orangehrm.qa.pages.LoginPage;
import com.orangehrm.qa.pages.PIMPage;
import com.orangehrm.qa.utils.ConfigReader;
import com.orangehrm.qa.utils.DriverManager;
import com.orangehrm.qa.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class covering PIM (Personnel Information Management) module scenarios.
 *
 * <p>Tests assume a logged-in Admin session and navigate directly to PIM URLs
 * for speed and isolation.  Results are logged to the Extent HTML report via
 * the inherited {@code test} field from {@link BaseTest}.
 *
 * <p>TC-07 depends on TC-06 having created the "AutoTest Employee" record.
 * If TC-06 is skipped or fails, TC-07 is automatically skipped by TestNG.
 */
public class PIMTest extends BaseTest {

    // -------------------------------------------------------------------------
    // Helper: derive the base root URL from the configured base.url
    // e.g. "https://opensource-demo.orangehrmlive.com"
    // -------------------------------------------------------------------------

    /**
     * Derives the protocol + host portion of the application URL by stripping
     * everything from the first {@code /web} path segment onwards.
     *
     * @return the URL root, e.g. {@code "https://opensource-demo.orangehrmlive.com"}
     */
    private String getBaseRoot() {
        String full = ConfigReader.get("base.url");
        int idx = full.indexOf("/web");
        return idx >= 0 ? full.substring(0, idx) : full;
    }

    // -------------------------------------------------------------------------
    // Private helper
    // -------------------------------------------------------------------------

    /**
     * Performs a full login as the Admin user and waits for the Dashboard page
     * to be displayed before returning.
     *
     * <p>This method is used by every PIM test to establish a valid session
     * before navigating directly to a PIM sub-page URL.
     */
    private void loginAsAdmin() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(
            ConfigReader.get("admin.username"),
            ConfigReader.get("admin.password")
        );
        // Wait for dashboard to confirm login succeeded before continuing
        DashboardPage dashboardPage = new DashboardPage(DriverManager.getDriver());
        Assert.assertTrue(
            dashboardPage.isDashboardDisplayed(),
            "Dashboard should be visible after admin login"
        );
    }

    // -------------------------------------------------------------------------
    // Test cases
    // -------------------------------------------------------------------------

    /**
     * TC-06: Verifies that a new employee record can be created via the PIM
     * Add Employee form and that the success toast notification is displayed.
     */
    @Test(
        description = "TC-06: Verify new employee can be added",
        groups = {"smoke", "regression"},
        priority = 6
    )
    public void TC06_AddNewEmployeeTest() {
        test = ExtentReportManager.createTest("TC-06 Add New Employee");
        test.info("Logging in as Admin");
        loginAsAdmin();

        test.info("Navigating directly to the Add Employee page");
        DriverManager.getDriver().get(getBaseRoot() + "/web/index.php/pim/addEmployee");

        PIMPage pimPage = new PIMPage(DriverManager.getDriver());

        test.info("Entering employee first name: AutoTest");
        pimPage.enterFirstName("AutoTest");

        test.info("Entering employee last name: Employee");
        pimPage.enterLastName("Employee");

        test.info("Clicking Save");
        pimPage.clickSave();

        test.info("Verifying success toast notification");
        String toastMsg = pimPage.getSuccessToastMessage();
        Assert.assertTrue(
            toastMsg.contains("Successfully Saved"),
            "Expected toast to contain 'Successfully Saved' but got: " + toastMsg
        );

        test.pass("New employee created successfully. Toast: " + toastMsg);
    }

    /**
     * TC-07: Verifies that searching for the employee created in TC-06 returns
     * at least one result and that the first result contains the expected name.
     *
     * <p>This test depends on {@link #TC06_AddNewEmployeeTest()} — if TC-06
     * did not pass, TestNG will automatically skip this test.
     */
    @Test(
        description = "TC-07: Verify employee search returns valid results",
        groups = {"smoke", "regression"},
        priority = 7,
        dependsOnMethods = "TC06_AddNewEmployeeTest"
    )
    public void TC07_SearchExistingEmployeeTest() {
        test = ExtentReportManager.createTest("TC-07 Search Existing Employee");
        test.info("Logging in as Admin");
        loginAsAdmin();

        test.info("Navigating directly to the Employee List page");
        DriverManager.getDriver().get(getBaseRoot() + "/web/index.php/pim/viewEmployeeList");

        PIMPage pimPage = new PIMPage(DriverManager.getDriver());

        test.info("Searching for employee: AutoTest");
        pimPage.searchEmployeeByName("AutoTest");

        test.info("Verifying at least one search result is returned");
        int resultCount = pimPage.getSearchResultCount();
        Assert.assertTrue(
            resultCount >= 1,
            "Expected at least 1 search result but found: " + resultCount
        );

        test.info("Verifying first result contains the expected name");
        String firstName = pimPage.getFirstResultEmployeeName();
        Assert.assertTrue(
            firstName.contains("AutoTest"),
            "Expected first result name to contain 'AutoTest' but got: " + firstName
        );

        test.pass("Employee search verified. Result count: " + resultCount
                + ", First name: " + firstName);
    }
}
