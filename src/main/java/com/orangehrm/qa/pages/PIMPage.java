package com.orangehrm.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for the OrangeHRM PIM (Personnel Information Management) module.
 *
 * <p>Covers two sub-pages within the PIM module:
 * <ul>
 *   <li><b>Add Employee</b> — {@code /web/index.php/pim/addEmployee}</li>
 *   <li><b>Employee List</b> — {@code /web/index.php/pim/viewEmployeeList}</li>
 * </ul>
 */
public class PIMPage extends BasePage {

    // -------------------------------------------------------------------------
    // Page elements (PageFactory)
    // -------------------------------------------------------------------------

    /** "Add Employee" navigation link in the PIM sub-menu. */
    @FindBy(xpath = "//a[normalize-space()='Add Employee']")
    private WebElement addEmployeeNavLink;

    /**
     * First-name input on the Add Employee form.
     * The class {@code orangehrm-firstname} is on the wrapper div, not the input;
     * the input itself is reliably targeted by its placeholder attribute.
     */
    @FindBy(css = "input[placeholder='First Name']")
    private WebElement firstNameField;

    /**
     * Last-name input on the Add Employee form.
     * Same note as firstNameField — placeholder is more stable than wrapper class.
     */
    @FindBy(css = "input[placeholder='Last Name']")
    private WebElement lastNameField;

    /**
     * Employee ID input on the Add Employee form.
     *
     * <p>The {@code label} attribute is not a standard HTML form attribute, so
     * we use an XPath that navigates from the visible label text to the
     * associated input sibling.
     */
    @FindBy(xpath = "//label[text()='Employee Id']/following-sibling::div//input")
    private WebElement employeeIdField;

    /** Save / Submit button (used on both Add Employee and Search forms). */
    @FindBy(css = "button[type='submit']")
    private WebElement saveButton;

    /**
     * Success toast notification text that appears after a record is saved.
     *
     * <p>This element is transient — always use {@link #getSuccessToastMessage()}
     * rather than accessing this field directly.
     */
    @FindBy(css = ".oxd-toast-content .oxd-text--p")
    private WebElement successToast;

    /** Autocomplete search input on the Employee List page. */
    @FindBy(css = ".oxd-autocomplete-text-input > input")
    private WebElement employeeNameSearch;

    /** Employee List nav link in the PIM sub-menu. */
    @FindBy(xpath = "//a[normalize-space()='Employee List']")
    private WebElement employeeListNavLink;

    // -------------------------------------------------------------------------
    // Dynamic locators (not bound at construction time)
    // -------------------------------------------------------------------------

    private static final By RESULT_ROWS      = By.cssSelector(".oxd-table-body .oxd-table-row");
    private static final By NO_RECORDS_MSG   = By.cssSelector(".oxd-table-body span");
    private static final By AUTOCOMPLETE_OPTION = By.cssSelector(".oxd-autocomplete-option");

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs the PIMPage and initialises all {@code @FindBy} elements via
     * the parent {@link BasePage} constructor.
     *
     * @param driver the active {@link WebDriver} instance
     */
    public PIMPage(WebDriver driver) {
        super(driver);
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Clicks the "Add Employee" link in the PIM sub-navigation menu.
     */
    public void clickAddEmployee() {
        click(addEmployeeNavLink);
    }

    /**
     * Types the given first name into the First Name input field on the
     * Add Employee form.
     *
     * <p>Waits for the field to be visible before typing, which also acts as
     * a readiness gate after direct URL navigation to the Add Employee page.
     *
     * @param firstName the first name to enter
     */
    public void enterFirstName(String firstName) {
        // Explicit visibility wait doubles as a form-ready gate after direct navigation
        wait.until(ExpectedConditions.visibilityOf(firstNameField));
        type(firstNameField, firstName);
    }

    /**
     * Types the given last name into the Last Name input field on the
     * Add Employee form.
     *
     * @param lastName the last name to enter
     */
    public void enterLastName(String lastName) {
        type(lastNameField, lastName);
    }

    /**
     * Clicks the Save (submit) button, submitting the currently active form.
     *
     * <p>Waits for the {@code oxd-form-loader} overlay to disappear before
     * clicking — the overlay covers the button during the SPA's initial page
     * render and would otherwise intercept the click.
     */
    public void clickSave() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".oxd-form-loader")));
        click(saveButton);
    }

    /**
     * Waits for the success toast notification to appear after saving and
     * returns its text.
     *
     * <p>OrangeHRM navigates from the Add Employee page to the employee detail
     * page upon a successful save, and the toast is rendered on that new page.
     * This method therefore first waits for the URL to leave {@code addEmployee}
     * (confirming navigation completed) and then waits up to 10 seconds for
     * the toast element to become visible.
     *
     * @return the trimmed text content of the toast notification
     */
    public String getSuccessToastMessage() {
        // Wait for navigation away from the Add Employee page to complete
        new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.not(
                        ExpectedConditions.urlContains("addEmployee")));
        // Toast element on the destination page — class used by OrangeHRM for the message text
        WebElement toast = new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".oxd-toast-message-text")));
        return toast.getText().trim();
    }

    /**
     * Searches for an employee by name using the autocomplete search field on
     * the Employee List page.
     *
     * <p>Steps performed:
     * <ol>
     *   <li>Types the name into the autocomplete input.</li>
     *   <li>Waits for at least one dropdown suggestion to appear.</li>
     *   <li>Clicks the first suggestion to populate the search field.</li>
     *   <li>Clicks the Search (submit) button.</li>
     * </ol>
     *
     * @param name the employee name or partial name to search for
     */
    public void searchEmployeeByName(String name) {
        type(employeeNameSearch, name);
        // Wait for the autocomplete drop-down to appear
        WebElement firstOption = wait.until(
                ExpectedConditions.visibilityOfElementLocated(AUTOCOMPLETE_OPTION));
        firstOption.click();
        // Click the Search button — there may be multiple submit buttons; use
        // the first one that is clickable (the Search form's submit button)
        WebElement searchBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        searchBtn.click();
    }

    /**
     * Returns the number of employee rows currently displayed in the search
     * results table.
     *
     * @return row count (0 if the table body is empty)
     */
    public int getSearchResultCount() {
        try {
            List<WebElement> rows = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(RESULT_ROWS));
            return rows.size();
        } catch (org.openqa.selenium.TimeoutException e) {
            return 0;
        }
    }

    /**
     * Checks whether the "No Records Found" message is displayed in the
     * results table body.
     *
     * @return {@code true} if the no-records span is visible and its text
     *         contains "No Records Found"; {@code false} otherwise
     */
    public boolean isNoRecordsMessageDisplayed() {
        try {
            WebElement msg = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(NO_RECORDS_MSG));
            return msg.getText().contains("No Records Found");
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    /**
     * Clicks the "Employee List" link in the PIM sub-navigation menu,
     * navigating to the employee search page.
     */
    public void clickEmployeeListNavLink() {
        click(employeeListNavLink);
    }

    /**
     * Returns the full name of the employee displayed in the first row of the
     * search results table.
     *
     * <p>The name cell is the second {@code .oxd-table-cell} within the first
     * result row (index 1, because index 0 is the checkbox column).
     *
     * @return the trimmed employee name string from the first result row
     */
    public String getFirstResultEmployeeName() {
        WebElement firstRow = wait.until(
                ExpectedConditions.visibilityOfElementLocated(RESULT_ROWS));
        // Second cell (index 1) contains the employee name
        List<WebElement> cells = firstRow.findElements(By.cssSelector(".oxd-table-cell"));
        return cells.get(1).getText().trim();
    }
}
