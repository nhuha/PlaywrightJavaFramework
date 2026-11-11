package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class UIValidationTest {
    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        context = browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
        page.navigate("https://*********/loginpagePractise/");

    }

    @AfterMethod()
    public void tearDown() {
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
    }

    @Test
    public void ChildWindowHandle() {

        Locator blinkingTexts = page.locator(".blinkingText");

        Page newPage = context.waitForPage(() -> blinkingTexts.first().click());
        newPage.waitForLoadState();
        String childText = newPage.locator(".red").textContent();
        String emailId = childText.split("at ")[1].split(" ")[0];

        page.getByLabel("Username:").fill(emailId);
        System.out.println(page.getByLabel("Username:").inputValue());


    }


    @Test()
    public void UIControls() {
        Locator userRdBtn = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("User"));
        userRdBtn.click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Okay")).click();
        Assert.assertTrue(userRdBtn.isChecked());

        Locator checkBoxTerms = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I agree to the terms and conditions"));

        checkBoxTerms.check();
        Assert.assertTrue(checkBoxTerms.isChecked());

        page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");

    }

}

