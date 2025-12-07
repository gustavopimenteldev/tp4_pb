package com.sistema.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {
    
    private static final By WELCOME_MESSAGE = By.id("welcome-message");
    private static final By PERSON_LIST_LINK = By.id("person-list-link");
    private static final By ADD_PERSON_LINK = By.id("add-person-link");
    private static final By SYSTEM_TITLE = By.id("system-title");
    
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    public void navigateTo() {
        driver.get("http://localhost:8080/");
        waitForPageLoad();
    }
    
    public String getWelcomeMessage() {
        return waitForElement(WELCOME_MESSAGE).getText();
    }
    
    public String getSystemTitle() {
        return waitForElement(SYSTEM_TITLE).getText();
    }
    
    public PersonListPage clickPersonListLink() {
        waitForClickableElement(PERSON_LIST_LINK).click();
        return new PersonListPage(driver);
    }
    
    public PersonFormPage clickAddPersonLink() {
        waitForClickableElement(ADD_PERSON_LINK).click();
        return new PersonFormPage(driver);
    }
    
    public boolean isWelcomeMessageVisible() {
        return isElementPresent(WELCOME_MESSAGE);
    }
    
    public boolean isPersonListLinkVisible() {
        return isElementPresent(PERSON_LIST_LINK);
    }
    
    public boolean isAddPersonLinkVisible() {
        return isElementPresent(ADD_PERSON_LINK);
    }
}