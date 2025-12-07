package com.sistema.selenium.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.Duration;

@TestConfiguration
@Profile("test")
public class SeleniumConfig {
    
    public static final String BASE_URL = "http://localhost:8080";
    public static final Duration DEFAULT_WAIT_TIMEOUT = Duration.ofSeconds(10);
    public static final Duration IMPLICIT_WAIT_TIMEOUT = Duration.ofSeconds(5);
    public static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(30);
    
    @Bean
    public SeleniumProperties seleniumProperties() {
        return new SeleniumProperties();
    }
    
    public static class SeleniumProperties {
        private String baseUrl = BASE_URL;
        private Duration waitTimeout = DEFAULT_WAIT_TIMEOUT;
        private Duration implicitWait = IMPLICIT_WAIT_TIMEOUT;
        private Duration pageLoadTimeout = PAGE_LOAD_TIMEOUT;
        private boolean headless = false;
        private String browserType = "CHROME";
        
        public String getBaseUrl() {
            return baseUrl;
        }
        
        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
        
        public Duration getWaitTimeout() {
            return waitTimeout;
        }
        
        public void setWaitTimeout(Duration waitTimeout) {
            this.waitTimeout = waitTimeout;
        }
        
        public Duration getImplicitWait() {
            return implicitWait;
        }
        
        public void setImplicitWait(Duration implicitWait) {
            this.implicitWait = implicitWait;
        }
        
        public Duration getPageLoadTimeout() {
            return pageLoadTimeout;
        }
        
        public void setPageLoadTimeout(Duration pageLoadTimeout) {
            this.pageLoadTimeout = pageLoadTimeout;
        }
        
        public boolean isHeadless() {
            return headless;
        }
        
        public void setHeadless(boolean headless) {
            this.headless = headless;
        }
        
        public String getBrowserType() {
            return browserType;
        }
        
        public void setBrowserType(String browserType) {
            this.browserType = browserType;
        }
        
        public SeleniumWebDriverFactory.BrowserType getBrowserTypeEnum() {
            return SeleniumWebDriverFactory.BrowserType.valueOf(browserType.toUpperCase());
        }
    }
}