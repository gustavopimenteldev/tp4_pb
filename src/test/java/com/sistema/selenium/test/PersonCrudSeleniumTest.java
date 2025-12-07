package com.sistema.selenium.test;

import com.sistema.CrudSystemApplication;
import com.sistema.selenium.config.SeleniumWebDriverFactory;
import com.sistema.selenium.page.PersonListPage;
import com.sistema.selenium.page.PersonFormPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CrudSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PersonCrudSeleniumTest {
    
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
    @DisplayName("Should create a new person successfully")
    void shouldCreatePersonSuccessfully() {
        personListPage.navigateTo();
        
        PersonFormPage formPage = personListPage.clickAddPersonButton();
        assertEquals("Add New Person", formPage.getFormTitle());
        
        formPage.fillForm(1L, "João Silva", "joao@email.com", 30);
        PersonListPage resultPage = formPage.submitForm();
        
        assertEquals("João Silva", resultPage.getPersonName(1L));
        assertEquals("joao@email.com", resultPage.getPersonEmail(1L));
        assertEquals("30", resultPage.getPersonAge(1L));
    }
        
    @Test
    @DisplayName("Should delete a person successfully")
    void shouldDeletePersonSuccessfully() {
        createTestPerson(1L, "Test Person", "test@email.com", 25);
        
        personListPage.navigateTo();
        assertTrue(personListPage.isPersonInList(1L));
        
        personListPage.clickDeleteButton(1L);
        
        assertFalse(personListPage.isPersonInList(1L));
    }
    
    @Test
    @DisplayName("Should show no persons message when list is empty")
    void shouldShowNoPersonsMessage() {
        personListPage.navigateTo();
        
        assertTrue(personListPage.isNoPersonsMessageVisible());
        assertFalse(personListPage.isPersonsTableVisible());
    }
    
    @ParameterizedTest
    @EnumSource(SeleniumWebDriverFactory.BrowserType.class)
    @DisplayName("Should work across different browsers")
    void shouldWorkAcrossDifferentBrowsers(SeleniumWebDriverFactory.BrowserType browserType) {
        driver.quit();
        driver = webDriverFactory.createDriver(browserType);
        personListPage = new PersonListPage(driver);
        
        personListPage.navigateTo();
        PersonFormPage formPage = personListPage.clickAddPersonButton();
        
        formPage.fillForm(1L, "Test User", "test@email.com", 30);
        PersonListPage resultPage = formPage.submitForm();
        
        assertTrue(resultPage.isPersonInList(1L));
    }
    
    private void createTestPerson(Long id, String name, String email, Integer age) {
        personListPage.navigateTo();
        PersonFormPage formPage = personListPage.clickAddPersonButton();
        formPage.fillForm(id, name, email, age);
        formPage.submitForm();
    }
}