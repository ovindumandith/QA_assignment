package com.orangehrm.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the OrangeHRM Dashboard page.
 *
 * <p>Represents the main landing page displayed after a successful login.
 * Provides methods to verify dashboard state, read the logged-in user's name,
 * navigate to other modules via the sidebar, and log out.
 */
public class DashboardPage extends BasePage {

    // -------------------------------------------------------------------------
    // Page elements (PageFactory)
    // -------------------------------------------------------------------------

    /** Primary breadcrumb heading that reads "Dashboard". */
    @FindBy(css = ".oxd-topbar-header-breadcrumb h6")
    private WebElement dashboardHeading;

    /** User avatar label showing the currently logged-in user's name. */
    @FindBy(css = ".oxd-userdropdown-name")
    private WebElement userAvatarName;

    /** Clickable user dropdown tab (avatar area in the top-right corner). */
    @FindBy(css = ".oxd-userdropdown-tab")
    private WebElement userDropdownTab;

    /** "Logout" option inside the user dropdown menu. */
    @FindBy(xpath = "//a[normalize-space()='Logout']")
    private WebElement logoutOption;

    /** PIM link in the left-side navigation menu. */
    @FindBy(xpath = "//span[text()='PIM']")
    private WebElement pimNavLink;

    /** Admin link in the left-side navigation menu. */
    @FindBy(xpath = "//span[text()='Admin']")
    private WebElement adminNavLink;

    /** Leave link in the left-side navigation menu. */
    @FindBy(xpath = "//span[text()='Leave']")
    private WebElement leaveNavLink;

    /** Quick-launch widget panel on the dashboard. */
    @FindBy(css = ".orangehrm-dashboard-widget")
    private WebElement quickLaunchWidget;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs the DashboardPage and initialises all {@code @FindBy} elements
     * via the parent {@link BasePage} constructor.
     *
     * @param driver the active {@link WebDriver} instance
     */
    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Checks whether the Dashboard page is currently displayed by verifying
     * the visibility of the breadcrumb heading element.
     *
     * @return {@code true} if the heading is visible within the explicit wait
     *         timeout; {@code false} otherwise
     */
    public boolean isDashboardDisplayed() {
        return isDisplayed(dashboardHeading);
    }

    /**
     * Returns the text of the dashboard breadcrumb heading.
     *
     * <p>The expected value for a successful login is {@code "Dashboard"}.
     *
     * @return the trimmed heading text
     */
    public String getDashboardHeadingText() {
        return getText(dashboardHeading);
    }

    /**
     * Returns the display name of the currently logged-in user as shown in
     * the top-right avatar label.
     *
     * @return the trimmed username text (e.g., {@code "Admin"})
     */
    public String getLoggedInUsername() {
        return getText(userAvatarName);
    }

    /**
     * Clicks the PIM navigation link in the sidebar, navigating to the PIM
     * module.
     */
    public void clickPIMNavLink() {
        click(pimNavLink);
    }

    /**
     * Clicks the Admin navigation link in the sidebar, navigating to the
     * Admin module.
     */
    public void clickAdminNavLink() {
        click(adminNavLink);
    }

    /**
     * Clicks the Leave navigation link in the sidebar, navigating to the
     * Leave module.
     */
    public void clickLeaveNavLink() {
        click(leaveNavLink);
    }

    /**
     * Logs out the current user by clicking the avatar dropdown to expand the
     * menu, waiting for the Logout option to become clickable, and then
     * clicking it.
     *
     * <p>After this call the browser will be redirected to the Login page.
     */
    public void logout() {
        click(userDropdownTab);
        wait.until(ExpectedConditions.elementToBeClickable(logoutOption));
        click(logoutOption);
    }
}
