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
