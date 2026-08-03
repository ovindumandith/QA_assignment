# QA_assignment
# OrangeHRM QA Automation Framework

A production-ready **Selenium + TestNG + Page Object Model (POM)** test automation framework for the [OrangeHRM](https://opensource-demo.orangehrmlive.com) demo application, built as part of Y4 Automated Quality Assurance (AQA) coursework at UCSC.

---

## Tech Stack

| Tool | Version |
|------|---------|
| Java | 17 |
| Maven | 3.x |
| Selenium Java | 4.18.1 |
| TestNG | 7.9.0 |
| WebDriverManager | 5.7.0 |
| ExtentReports | 5.1.1 |
| Apache Commons Lang3 | 3.14.0 |
| SLF4J Simple | 2.0.12 |

---

## Project Structure
OrangeHRM-QA-Automation/
│
├── pom.xml
├── testng.xml
│
├── src/
│   ├── main/java/com/orangehrm/qa/
│   │   ├── pages/
│   │   │   ├── BasePage.java
│   │   │   ├── LoginPage.java
│   │   │   ├── DashboardPage.java
│   │   │   └── PIMPage.java
│   │   └── utils/
│   │       ├── ConfigReader.java
│   │       ├── DriverManager.java
│   │       ├── ExtentReportManager.java
│   │       └── ScreenshotUtil.java
│   │
│   └── test/
│       ├── java/com/orangehrm/qa/tests/
│       │   ├── BaseTest.java
│       │   ├── LoginTest.java
│       │   └── PIMTest.java
│       └── resources/
│           └── config.properties
│
├── reports/           # Generated HTML report (git-ignored)
└── screenshots/       # Failure screenshots (git-ignored)



---

## Test Cases

### Login Module — `LoginTest.java`

| ID | Description | Groups |
|----|-------------|--------|
| TC-01 | Valid login with correct credentials | smoke, regression |
| TC-02 | Error message shown for invalid password | smoke, regression |
| TC-03 | Validation shown when username is empty | regression |
| TC-04 | Validation shown when password is empty | regression |
| TC-05 | Validation shown when both fields are empty | regression |

### PIM Module — `PIMTest.java`

| ID | Description | Groups |
|----|-------------|--------|
| TC-06 | Add a new employee via the Add Employee form | smoke, regression |
| TC-07 | Search for the employee created in TC-06 *(depends on TC-06)* | smoke, regression |

---

## Prerequisites

- Java 17 installed and `JAVA_HOME` set
- Maven 3.x installed
- Google Chrome browser installed
- Internet connection (tests run against the live OrangeHRM demo site)

> **Note:** ChromeDriver is downloaded automatically by WebDriverManager — no manual setup needed.

---

## Configuration

All settings are in `src/test/resources/config.properties`:

```properties
base.url=https://opensource-demo.orangehrmlive.com/web/index.php/auth/login
admin.username=Admin
admin.password=admin123
browser=chrome
implicit.wait=10
explicit.wait=15
screenshot.path=screenshots/
How to Run
Run all tests

mvn test
Run only smoke tests
Uncomment the <groups> block in testng.xml:


<groups>
  <run>
    <include name="smoke"/>
  </run>
</groups>
Then run:


mvn test
Reports & Screenshots
Artifact	Location
HTML Extent Report	reports/ExtentReport.html
Failure Screenshots	screenshots/
Open reports/ExtentReport.html in any browser after the test run to view the full report with step logs, pass/fail status, and embedded failure screenshots.

Framework Design
ThreadLocal WebDriver — each test gets its own driver instance, safe for parallel execution
Page Object Model — all element locators and interactions are encapsulated in page classes
BasePage helpers — click(), type(), getText(), isDisplayed(), waitForPageLoad(), scrollToElement() used consistently across all pages
ElementClickInterceptedException fallback — click() automatically falls back to a JavaScript click when an overlay intercepts the normal click
Retry on navigation — beforeMethod retries page load up to 3 times with back-off to handle transient demo-site connection refusals
Unique test data — employee first name and ID are generated from the current timestamp to prevent duplicate-record conflicts across repeated runs
Application Under Test
Field	Value
URL	https://opensource-demo.orangehrmlive.com/web/index.php/auth/login
Username	Admin
Password	admin123
This is a publicly available demo environment shared by all users. Test data created during runs (e.g. employee records) may appear alongside data from other users.

Author
Ovindu — UCSC Year 4, Automated Quality Assurance module
