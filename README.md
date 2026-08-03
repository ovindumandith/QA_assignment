
# OrangeHRM QA Automation Framework

A **Selenium + TestNG + Page Object Model (POM)** test automation framework for the [OrangeHRM Demo Application](https://opensource-demo.orangehrmlive.com/), developed as part of the **Y4 Automated Quality Assurance (AQA)** coursework at the **University of Colombo School of Computing (UCSC)**.

---

## 📌 Overview

This project automates key functional scenarios of the OrangeHRM application using a maintainable and scalable test automation architecture.

The framework follows the **Page Object Model (POM)** design pattern and includes:

- Automated login testing
- Employee management testing through the PIM module
- Positive and negative test scenarios
- Test grouping using TestNG
- Automatic WebDriver management
- Explicit and implicit waits
- Screenshot capture for failed tests
- HTML test reporting using ExtentReports
- Reusable page and utility classes
- Support for parallel-safe WebDriver management

---

## 🛠️ Tech Stack

| Technology | Version |
|---|---:|
| Java | 17 |
| Maven | 3.x |
| Selenium Java | 4.18.1 |
| TestNG | 7.9.0 |
| WebDriverManager | 5.7.0 |
| ExtentReports | 5.1.1 |
| Apache Commons Lang3 | 3.14.0 |
| SLF4J Simple | 2.0.12 |

---

## 📂 Project Structure

```text
OrangeHRM-QA-Automation/
│
├── pom.xml
├── testng.xml
├── README.md
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── orangehrm/
│   │               └── qa/
│   │                   ├── pages/
│   │                   │   ├── BasePage.java
│   │                   │   ├── LoginPage.java
│   │                   │   ├── DashboardPage.java
│   │                   │   └── PIMPage.java
│   │                   │
│   │                   └── utils/
│   │                       ├── ConfigReader.java
│   │                       ├── DriverManager.java
│   │                       ├── ExtentReportManager.java
│   │                       └── ScreenshotUtil.java
│   │
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── orangehrm/
│       │           └── qa/
│       │               └── tests/
│       │                   ├── BaseTest.java
│       │                   ├── LoginTest.java
│       │                   └── PIMTest.java
│       │
│       └── resources/
│           └── config.properties
│
├── reports/
│   └── ExtentReport.html
│
└── screenshots/
    └── Failure screenshots
```

---

## 🧪 Test Cases

### 🔐 Login Module

**Test Class:** `LoginTest.java`

| ID | Test Scenario | Groups |
|---|---|---|
| TC-01 | Valid login with correct credentials | smoke, regression |
| TC-02 | Error message shown for invalid password | smoke, regression |
| TC-03 | Validation shown when username is empty | regression |
| TC-04 | Validation shown when password is empty | regression |
| TC-05 | Validation shown when both fields are empty | regression |

### 👤 PIM Module

**Test Class:** `PIMTest.java`

| ID | Test Scenario | Groups |
|---|---|---|
| TC-06 | Add a new employee using the Add Employee form | smoke, regression |
| TC-07 | Search for the employee created in TC-06 | smoke, regression |

> **Note:** TC-07 depends on TC-06 because the employee created in TC-06 is used as test data for the search scenario.

---

## ⚙️ Prerequisites

Before running the project, make sure the following are installed:

- **Java 17** or later
- **Maven 3.x**
- **Google Chrome**
- A stable **Internet connection**

Verify the installations using:

```bash
java -version
mvn -version
```

---

## 🔧 Configuration

Application and test settings are stored in:

```text
src/test/resources/config.properties
```

```properties
base.url=https://opensource-demo.orangehrmlive.com/web/index.php/auth/login
admin.username=Admin
admin.password=admin123
browser=chrome
implicit.wait=10
explicit.wait=15
screenshot.path=screenshots/
```

---

## ▶️ How to Run

### Run All Tests

From the project root directory, run:

```bash
mvn test
```

### Run Smoke Tests

To execute only the smoke tests, enable the smoke group in `testng.xml`:

```xml
<groups>
    <run>
        <include name="smoke"/>
    </run>
</groups>
```

Then run:

```bash
mvn test
```

### Run Regression Tests

To execute the regression test suite, configure the regression group in `testng.xml`:

```xml
<groups>
    <run>
        <include name="regression"/>
    </run>
</groups>
```

Then run:

```bash
mvn test
```

---

## 📊 Test Reports

After test execution, an HTML report is generated at:

```text
reports/ExtentReport.html
```

This report includes:
- Test execution summary
- Pass/fail status for each test
- Detailed logs
- Screenshots for failed tests

---

## 📸 Screenshots

Screenshots for failed tests are automatically captured and saved in:

```text
screenshots/
```

---

## 📝 TestNG Configuration

The `testng.xml` file configures test execution with the following features:

- Parallel test execution
- Test grouping
- Suite-level configuration
- Listener setup for reporting

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="OrangeHRM Test Suite" parallel="tests" thread-count="2">
    <listeners>
        <listener class-name="com.orangehrm.qa.utils.ExtentReportManager"/>
    </listeners>
    <test name="Login Tests">
        <classes>
            <class name="com.orangehrm.qa.tests.LoginTest"/>
        </classes>
    </test>
    <test name="PIM Tests">
        <classes>
            <class name="com.orangehrm.qa.tests.PIMTest"/>
        </classes>
    </test>
</suite>
```

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is developed for educational purposes as part of the AQA coursework at UCSC.

---

## 👥 Authors

- **Ovindu Gunatunga ** - *Initial work* - [YourGitHub][(https://github.com/yourusername](https://github.com/ovindumandith))

---

