package com.sistema.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PersonFormPage extends BasePage {

    private static final By FORM_TITLE = By.id("form-title");
    private static final By ID_FIELD = By.id("id");
    private static final By NAME_FIELD = By.id("name");
    private static final By EMAIL_FIELD = By.id("email");
    private static final By AGE_FIELD = By.id("age");
    private static final By SUBMIT_BUTTON = By.id("submit-btn");
    private static final By CANCEL_BUTTON = By.id("cancel-btn");

    private static final By ID_ERROR = By.id("id-error");
    private static final By NAME_ERROR = By.id("name-error");
    private static final By EMAIL_ERROR = By.id("email-error");
    private static final By AGE_ERROR = By.id("age-error");

    public PersonFormPage(WebDriver driver) {
        super(driver);
    }

    public String getFormTitle() {
        return waitForElement(FORM_TITLE).getText();
    }

    public void fillForm(Long id, String name, String email, Integer age) {
        if (id != null) {
            fillIdField(id.toString());
        }
        fillNameField(name);
        fillEmailField(email);
        if (age != null) {
            fillAgeField(age.toString());
        }
    }

    public void fillIdField(String id) {
        var idElement = waitForElement(ID_FIELD);
        idElement.clear();
        if (id != null && !id.isEmpty()) {
            idElement.sendKeys(id);
        }
    }

    public void fillNameField(String name) {
        var nameElement = waitForElement(NAME_FIELD);
        nameElement.clear();
        if (name != null) {
            nameElement.sendKeys(name);
        }
    }

    public void fillEmailField(String email) {
        var emailElement = waitForElement(EMAIL_FIELD);
        emailElement.clear();
        if (email != null) {
            emailElement.sendKeys(email);
        }
    }

    public void fillAgeField(String age) {
        var ageElement = waitForElement(AGE_FIELD);
        ageElement.clear();
        if (age != null && !age.isEmpty()) {
            ageElement.sendKeys(age);
        }
    }

    public PersonListPage submitForm() {
        waitForClickableElement(SUBMIT_BUTTON).click();
        waitForPageLoad();
        return new PersonListPage(driver);
    }

    public PersonListPage clickCancel() {
        waitForClickableElement(CANCEL_BUTTON).click();
        waitForPageLoad();
        return new PersonListPage(driver);
    }

    public boolean hasIdError() {
        return waitForElementIfPresent(ID_ERROR);
    }

    public boolean hasNameError() {
        return waitForElementIfPresent(NAME_ERROR);
    }

    public boolean hasEmailError() {
        return waitForElementIfPresent(EMAIL_ERROR);
    }

    public boolean hasAgeError() {
        return waitForElementIfPresent(AGE_ERROR);
    }

    public String getIdErrorMessage() {
        return hasIdError() ? waitForElement(ID_ERROR).getText() : "";
    }

    public String getNameErrorMessage() {
        return hasNameError() ? waitForElement(NAME_ERROR).getText() : "";
    }

    public String getEmailErrorMessage() {
        return hasEmailError() ? waitForElement(EMAIL_ERROR).getText() : "";
    }

    public String getAgeErrorMessage() {
        return hasAgeError() ? waitForElement(AGE_ERROR).getText() : "";
    }

    public boolean isIdFieldReadonly() {
        return waitForElement(ID_FIELD).getAttribute("readonly") != null;
    }
}