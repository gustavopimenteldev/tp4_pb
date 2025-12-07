package com.sistema.selenium.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.stereotype.Component;

@Component
public class SeleniumWebDriverFactory {

    public enum BrowserType {
        CHROME,
        FIREFOX
    }

    public WebDriver createDriver(BrowserType browserType) {
        return switch (browserType) {
            case CHROME -> createChromeDriver();
            case FIREFOX -> createFirefoxDriver();
        };
    }

    private WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        if (isHeadlessMode()) {
            options.addArguments("--headless");
        }

        return new ChromeDriver(options);
    }

    private WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();

        if (isHeadlessMode()) {
            options.addArguments("--headless");
        }

        return new FirefoxDriver(options);
    }

    private boolean isHeadlessMode() {
        String prop = System.getProperty("headless");
        if (prop != null) {
            return Boolean.parseBoolean(prop);
        }

        String env = System.getenv("HEADLESS");
        if (env != null) {
            return Boolean.parseBoolean(env);
        }

        return false;
    }
}