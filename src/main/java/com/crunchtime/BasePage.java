package com.crunchtime;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait waiter;

    public BasePage(){
        this.driver = DriverManager.getDriver();
        this.waiter = new WebDriverWait(this.driver, Duration.ofSeconds(30));
    }
}