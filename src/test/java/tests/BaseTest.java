package tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BaseTest {

    Playwright playwright;
    Page page;
    Browser browser;
    String baseUrl;

    @BeforeMethod(alwaysRun = true)
    public void setup() throws IOException {
        playwright = Playwright.create();
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/test/resources/config.properties");
        properties.load(fileInputStream);

        String browserName = System.getProperty("browser") != null ? System.getProperty("browser") : properties.getProperty("browser");
        String envName = System.getProperty("env") != null ? System.getProperty("env") : properties.getProperty("env");

        if ("firefox".equals(browserName)) {
            browser = playwright.firefox().launch();
        } else if ("safari".equals(browserName)) {
            browser = playwright.webkit().launch();
        } else {
            //     browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            browser = playwright.chromium().launch();
        }

        page = browser.newPage();

        baseUrl = properties.getProperty(envName + ".base_url");
        System.out.println(">>> ENV=" + envName + " BASE_URL=" + baseUrl);
        page.navigate(baseUrl);
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
    }
}
