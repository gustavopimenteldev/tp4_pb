package com.sistema.selenium.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.sistema.CrudSystemApplication;
import com.sistema.selenium.config.SeleniumWebDriverFactory;
import com.sistema.selenium.page.PersonFormPage;
import com.sistema.selenium.page.PersonListPage;

@SpringBootTest(classes = CrudSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PersonValidationSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private PersonListPage personListPage;
    private final SeleniumWebDriverFactory webDriverFactory = new SeleniumWebDriverFactory();

    @BeforeEach
    void setUp() {
        driver = webDriverFactory.createDriver(SeleniumWebDriverFactory.BrowserType.CHROME);
        personListPage = new PersonListPage(driver);
        updateBaseUrl();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void updateBaseUrl() {
        System.setProperty("base.url", "http://localhost:" + port);
    }

    @Test
    @DisplayName("Should show validation errors for empty fields")
    void shouldShowValidationErrorsForEmptyFields() {
        personListPage.navigateTo();
        PersonFormPage formPage = personListPage.clickAddPersonButton();

        formPage.fillForm(null, "", "", null);
        formPage.submitForm();

        assertTrue(formPage.hasNameError());
        assertTrue(formPage.hasEmailError());
        assertTrue(formPage.hasAgeError());
    }

    @ParameterizedTest
    @ValueSource(strings = { "invalid-email", "test@", "@domain.com", "test.domain" })
    @DisplayName("Should show validation error for invalid email formats")
    void shouldShowValidationErrorForInvalidEmail(String invalidEmail) {
        personListPage.navigateTo();
        PersonFormPage formPage = personListPage.clickAddPersonButton();

        formPage.fillForm(1L, "Test Name", invalidEmail, 25);
        formPage.submitForm();

        assertTrue(formPage.hasEmailError());
        assertTrue(formPage.getEmailErrorMessage().contains("Invalid email format"));
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 151, 200 })
    @DisplayName("Should show validation error for invalid age values")
    void shouldShowValidationErrorForInvalidAge(Integer invalidAge) {
        personListPage.navigateTo();
        PersonFormPage formPage = personListPage.clickAddPersonButton();

        formPage.fillForm(1L, "Test Name", "test@email.com", invalidAge);
        formPage.submitForm();

        assertTrue(formPage.hasAgeError());
    }

    @Test
    @DisplayName("Should show error when creating duplicate person")
    void shouldShowErrorForDuplicatePerson() {
        createTestPerson(1L, "Test Person", "test@email.com", 25);

        personListPage.navigateTo();
        PersonFormPage formPage = personListPage.clickAddPersonButton();

        formPage.fillForm(1L, "Duplicate Person", "duplicate@email.com", 30);
        formPage.submitForm();

        assertTrue(personListPage.isErrorAlertVisible());
        assertTrue(personListPage.getErrorMessage().contains("already exists"));
    }

    @Test
    @DisplayName("Should validate boundary age values")
    void shouldValidateBoundaryAgeValues() {
        personListPage.navigateTo();
        PersonFormPage formPage = personListPage.clickAddPersonButton();

        formPage.fillForm(1L, "Test Name", "test@email.com", 0);
        PersonListPage resultPage = formPage.submitForm();
        assertTrue(resultPage.isPersonInList(1L));

        personListPage.navigateTo();
        PersonFormPage formPage2 = personListPage.clickAddPersonButton();

        formPage2.fillForm(2L, "Test Name 2", "test2@email.com", 150);
        PersonListPage resultPage2 = formPage2.submitForm();
        assertTrue(resultPage2.isPersonInList(2L));
    }

    private void createTestPerson(Long id, String name, String email, Integer age) {
        personListPage.navigateTo();
        PersonFormPage formPage = personListPage.clickAddPersonButton();
        formPage.fillForm(id, name, email, age);
        formPage.submitForm();
    }
}