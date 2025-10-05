package com.crunchtime;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Set;

public class TestHeroku extends TestBase{

    @BeforeSuite
    public void setup(){
        System.out.println("Running with suite:" + System.getProperty("threadCount") + " threads");
    }

    @AfterMethod
    public void tearDown(){
        try {
            DriverManager.quitDriver();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeTest() {
        WebDriver driver = getDriver();
        WebDriverWait waiter = new WebDriverWait(driver, Duration.ofSeconds(10));

        System.out.println("Running the test");

        // Navigate to the herokuApp
         driver.get("https://the-internet.herokuapp.com/");

        // Add and remove elements
        waiter.until(ExpectedConditions.elementToBeClickable(By.linkText("Add/Remove Elements")));
        driver.findElement(By.linkText("Add/Remove Elements")).click();
        driver.findElement(By.cssSelector("button[onclick='addElement()']")).click();
        Assert.assertTrue(driver.findElement(By.cssSelector("button[class='added-manually']")).isDisplayed());
        driver.get("https://the-internet.herokuapp.com/");

        // Dynamic controls
        driver.findElement(By.linkText("Dynamic Controls")).click();
        driver.findElement(By.cssSelector("button[onclick='swapCheckbox()']")).click();
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='message']")));
        driver.findElement(By.cssSelector("button[onclick='swapCheckbox()']")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[id='checkbox']")));
        driver.findElement(By.cssSelector("input[id='checkbox']")).click();
        driver.findElement(By.cssSelector("button[onclick='swapInput()']")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='text']")));
        driver.findElement(By.cssSelector("input[type='text']")).sendKeys("Playwright");
        driver.get("https://the-internet.herokuapp.com/");

        // Dynamic loading
        driver.findElement(By.linkText("Dynamic Loading")).click();
        driver.findElement(By.linkText("Example 1: Element on page that is hidden")).click();
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Start']")));
        driver.findElement(By.xpath("//button[text()='Start']")).click();
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='finish']")));
        driver.get("https://the-internet.herokuapp.com/dynamic_loading");
        driver.findElement(By.linkText("Example 2: Element rendered after the fact")).click();
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Start']")));
        driver.findElement(By.xpath("//button[text()='Start']")).click();
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='finish']")));
        driver.get("https://the-internet.herokuapp.com/");

        // jQuery menu and File Download
        driver.findElement(By.linkText("JQuery UI Menus")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li[id='ui-id-3']>a")));
        driver.findElement(By.cssSelector("li[id='ui-id-3']>a")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li[id='ui-id-4']>a")));
        driver.findElement(By.cssSelector("li[id='ui-id-4']>a")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li[id='ui-id-6']>a")));
        driver.findElement(By.cssSelector("li[id='ui-id-6']>a")).click();
        try {
            waitForFile(DriverManager.getDownloadDir(), "menu.csv", 30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        driver.get("https://the-internet.herokuapp.com/");

        // Multiple windows
        driver.findElement(By.linkText("Multiple Windows")).click();
        String originalWindow = driver.getWindowHandle();
        driver.findElement(By.linkText("Click Here")).click();
        Set<String> allWindowHandles = driver.getWindowHandles();
        for (String handle : allWindowHandles) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[text()='New Window']")));
        driver.close();
        driver.switchTo().window(originalWindow);
        driver.get("https://the-internet.herokuapp.com/");

        // Context Menu and Alert
        driver.findElement(By.linkText("Context Menu")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[id='hot-spot']")));
        WebElement hotSpot = driver.findElement(By.cssSelector("div[id='hot-spot']"));
        Actions actions = new Actions(driver);
        actions.contextClick(hotSpot).perform();
        waiter.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        alert.dismiss();

        // Select dropdowns
        driver.get("https://www.qaplayground.com/practice/select");
        driver.findElement(By.xpath("(//button[@role='combobox'])[1]")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@role='option']//span[text()='Banana']")));
        driver.findElement(By.xpath("//div[@role='option']//span[text()='Banana']")).click();

        WebElement superHero =  driver.findElement(By.xpath("//p[contains(text(),'hero')]/following-sibling::select"));
        Select heroDropdown  = new Select(superHero);
        heroDropdown.selectByValue("aquaman");

        driver.findElement(By.xpath("(//button[@role='combobox'])[2]")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@role='option']//span[text()='Java']")));
        driver.findElement(By.xpath("//div[@role='option']//span[text()='Java']")).click();

        driver.findElement(By.xpath("(//button[@role='combobox'])[3]")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@role='option']//span[text()='UK']")));
        driver.findElement(By.xpath("//div[@role='option']//span[text()='India']")).click();

        // Date inputs
        driver.get("https://www.qaplayground.com/practice/calendar");
        waiter.until(ExpectedConditions.elementToBeClickable(By.xpath("(//input[@name='my-birthday'])[1]")));
        driver.findElement(By.xpath("(//input[@name='my-birthday'])[1]")).sendKeys("2020-01-01");
        driver.findElement(By.xpath("(//input[@name='my-birthday'])[1]")).sendKeys("2026-01-01");

    }

    public static File waitForFile(Path dir, String fileName, int timeoutSeconds) throws InterruptedException {
        int waited = 0;

        while (waited < timeoutSeconds) {
            File[] files = dir.toFile().listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().equals(fileName) && !file.getName().endsWith(".crdownload")) {
                        return file;
                    }
                }
            }
            Thread.sleep(1000);
            waited++;
        }
        throw new RuntimeException("File " + fileName + " not downloaded in time");
    }

    @Test(groups = {"shard-1"})
    public void herokuTest1() {
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest2() {
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest3() {
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest4() {
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest5() {
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest6(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest7(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest8(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest9(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest10(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest11(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest12(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest13(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest14(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest15(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest16(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest17(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest18(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest19(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest20(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest21(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest22(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest23(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest24(){
        executeTest();
    }

    @Test(groups = {"shard-1"})
    public void herokuTest25(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest26(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest27(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest28(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest29(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest30(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest31(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest32(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest33(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest34(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest35(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest36(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest37(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest38(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest39(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest40(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest41(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest42(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest43(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest44(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest45(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest46(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest47(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest48(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest49(){
        executeTest();
    }

    @Test(groups = {"shard-2"})
    public void herokuTest50(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest51(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest52(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest53(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest54(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest55(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest56(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest57(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest58(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest59(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest60(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest61(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest62(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest63(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest64(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest65(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest66(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest67(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest68(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest69(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest70(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest71(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest72(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest73(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest74(){
        executeTest();
    }

    @Test(groups = {"shard-3"})
    public void herokuTest75(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest76(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest77(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest78(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest79(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest80(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest81(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest82(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest83(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest84(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest85(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest86(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest87(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest88(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest89(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest90(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest91(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest92(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest93(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest94(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest95(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest96(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest97(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest98(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest99(){
        executeTest();
    }

    @Test(groups = {"shard-4"})
    public void herokuTest100(){
        executeTest();
    }
}
