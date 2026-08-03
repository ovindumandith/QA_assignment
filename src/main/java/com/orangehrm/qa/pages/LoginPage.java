package com.orangehrm.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for the OrangeHRM Login page.
 *
 * <p>
 * Encapsulates all interactions with the login form, including entering
 * credentials, triggering login, and retrieving validation or error messages.
 * Extend this class is not intended; all public methods represent the page's
 * full public API.
 *
 * <p>
 * URL:
 * {@code https://opensource-demo.orangehrmlive.com/web/index.php/auth/login}
 */
public class LoginPage extends BasePage {

    // -------------------------------------------------------------------------
    // Page elements (PageFactory)
    // -------------------------------------------------------------------------

    /** Username input field. */
    @FindBy(name = "username")
    private WebElement usernameField;

    /** Password input field. */
    @FindBy(name = "password")
    private WebElement passwordField;

    /** Login submit button. */
    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    /**
     * Red error alert shown for invalid credentials (e.g. "Invalid credentials").
     */
    @FindBy(xpath = "//div[contains(@class,'oxd-alert-content')]//p")
    private WebElement errorMessage;

    /** Per-field validation messages shown when required fields are left empty. */
    @FindAll(@FindBy(css = ".oxd-input-field-error-message"))
    private List<WebElement> validationMessages;

    /** OrangeHRM logo image on the login page. */
    @FindBy(css = ".orangehrm-login-logo img")
    private WebElement pageLogo;

    /** "Forgot your password?" link. */
    @FindBy(css = ".orangehrm-login-forgot > p")
    private WebElement forgotPasswordLink;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs the LoginPage and initialises all {@code @FindBy} elements
     * via the parent {@link BasePage} constructor.
     *
     * @param driver the active {@link WebDriver} instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Types the given username into the username input field.
     *
     * @param username the username string to enter
     */
    public void enterUsername(String username) {
        type(usernameField, username);
    }

    /**
     * Types the given password into the password input field.
     *
     * @param password the password string to enter
     */
    public void enterPassword(String password) {
        type(passwordField, password);
    }

    /**
     * Clicks the Login button.
     */
    public void clickLoginButton() {
        click(loginButton);
    }

    /**
     * Performs a complete login flow: enters the username, enters the password,
     * then clicks the Login button.
     *
     * @param username the username to log in with
     * @param password the password to log in with
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Returns the text of the error alert displayed after an unsuccessful
     * login attempt (e.g., "Invalid credentials").
     *
     * @return the trimmed alert text
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Returns the text of the first inline validation message, which appears
     * below a field that was left empty.
     *
     * @return the trimmed text of the first validation message element
     */
    public String getValidationMessage() {
        return getText(validationMessages.get(0));
    }

    /**
     * Returns the text of every inline validation message currently visible
     * on the page.
     *
     * @return an unmodifiable {@link List} of trimmed validation message strings
     *         (may be empty if no validation errors are showing)
     */
    public List<String> getAllValidationMessages() {
        return validationMessages.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    /**
     * Determines whether the Login page is currently displayed by checking
     * the visibility of the Login button.
     *
     * @return {@code true} if the Login button is visible; {@code false} otherwise
     */
    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginButton);
    }

    /**
     * Clicks the "Forgot your password?" link, navigating to the password
     * reset page.
     */
    public void clickForgotPasswordLink() {
        click(forgotPasswordLink);
    }
}
