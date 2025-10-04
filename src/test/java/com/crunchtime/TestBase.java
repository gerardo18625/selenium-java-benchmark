package com.crunchtime;

import org.openqa.selenium.WebDriver;

import java.nio.file.Path;

public class TestBase {

    public WebDriver getDriver(){
        return DriverManager.getDriver();
    }
}
