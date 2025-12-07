package com.sistema.selenium.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PersonListPage extends BasePage {

    private static final By ADD_PERSON_BUTTON = By.id("add-person-btn");
    private static final By PERSONS_TABLE = By.id("persons-table");
    private static final By NO_PERSONS_MESSAGE = By.id("no-persons-message");
    private static final By SUCCESS_ALERT = By.cssSelector(".alert-success");
    private static final By ERROR_ALERT = By.cssSelector(".alert-danger");

    public PersonListPage(WebDriver driver) {
        super(driver);
        waitForPageLoad();
    }

    public void navigateTo() {
        String baseUrl = System.getProperty("base.url", "http://localhost:8080");
        driver.get(baseUrl + "/persons");
        waitForPageLoad();
    }

    public PersonFormPage clickAddPersonButton() {
        waitForClickableElement(ADD_PERSON_BUTTON).click();
        return new PersonFormPage(driver);
    }

    public boolean isPersonsTableVisible() {
        return waitForElementIfPresent(PERSONS_TABLE);
    }

    public boolean isNoPersonsMessageVisible() {
        return waitForElementIfPresent(NO_PERSONS_MESSAGE);
    }

    public List<WebElement> getPersonRows() {
        if (!isPersonsTableVisible()) {
            return List.of();
        }
        return driver.findElements(By.cssSelector("#persons-table tbody tr"));
    }

    public boolean isPersonInList(Long personId) {
        if (waitForElementIfPresent(PERSONS_TABLE)) {
            return isElementPresent(By.id("person-row-" + personId));
        }

        if (waitForElementIfPresent(NO_PERSONS_MESSAGE)) {
            return false;
        }

        return false;
    }

    public PersonFormPage clickEditButton(Long personId) {
        WebElement editButton = waitForClickableElement(By.id("edit-btn-" + personId));
        editButton.click();
        return new PersonFormPage(driver);
    }

    public void clickDeleteButton(Long personId) {
        WebElement deleteButton = waitForClickableElement(By.id("delete-btn-" + personId));
        deleteButton.click();

        driver.switchTo().alert().accept();
    }

    public boolean isSuccessAlertVisible() {
        return waitForElementIfPresent(SUCCESS_ALERT);
    }

    public boolean isErrorAlertVisible() {
        return waitForElementIfPresent(ERROR_ALERT);
    }

    public String getSuccessMessage() {
        if (!isSuccessAlertVisible()) {
            return "";
        }
        return waitForElement(SUCCESS_ALERT).getText();
    }

    public String getErrorMessage() {
        if (!isErrorAlertVisible()) {
            return "";
        }
        return waitForElement(ERROR_ALERT).getText();
    }

    public String getPersonName(Long personId) {
        waitForElement(PERSONS_TABLE);
        WebElement nameCell = driver.findElement(
                By.cssSelector("#person-row-" + personId + " .person-name"));
        return nameCell.getText();
    }

    public String getPersonEmail(Long personId) {
        waitForElement(PERSONS_TABLE);
        WebElement emailCell = driver.findElement(
                By.cssSelector("#person-row-" + personId + " .person-email"));
        return emailCell.getText();
    }

    public String getPersonAge(Long personId) {
        waitForElement(PERSONS_TABLE);
        WebElement ageCell = driver.findElement(
                By.cssSelector("#person-row-" + personId + " .person-age"));
        return ageCell.getText();
    }
}