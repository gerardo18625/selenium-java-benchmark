package com.crunchtime;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DriverManager {

    private static ThreadLocal<WebDriver> driver= driver = new ThreadLocal<>();
    private static final ThreadLocal<Path> downloadDir = new ThreadLocal<>();

    private DriverManager(){

    }

    public static Path getDownloadDir() {
        return downloadDir.get();
    }

    public static WebDriver getDriver(){
        if(driver.get() == null){
            String uniqueDir = "browser-profile-" + Thread.currentThread().getId();
            String userDataDir = "/tmp/" + uniqueDir;
            Map<String, Object> prefs = new HashMap<>();
            try {
                Path dir = Files.createTempDirectory("downloads_");
                prefs.put("download.default_directory", dir.toAbsolutePath().toString());
                downloadDir.set(dir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            prefs.put("download.prompt_for_download", false);
            prefs.put("download.directory_upgrade", true);
            prefs.put("safebrowsing.enabled", true);

            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", prefs);
            options.addArguments("--user-data-dir=" + userDataDir);
            WebDriver instance = new ChromeDriver(options);
            driver.set(instance);
        }
        return driver.get();
    }

    public static void quitDriver() throws IOException {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }

        if (downloadDir.get() != null) {
            Files.walk(downloadDir.get())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            downloadDir.remove();
        }
    }
}
