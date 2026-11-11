package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UIComponents {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://*********/AutomationPractice/");
        page.setDefaultTimeout(5000);
    }

    @Test(groups = {"smoke"})
    public void popupValidations() {
        assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();
        page.locator("#hide-textbox").click();
        assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();
        page.onDialog(dialog -> dialog.accept());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Alert")).click();


        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Mouse Hover")).hover();
        FrameLocator framesPage = page.frameLocator("#courses-iframe");

        framesPage.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Learning Paths")).click();
        String textCheck = framesPage.locator(".inner-box h1").textContent();
    }

    @Test
    public void screenShotTest() {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagescreenshot.png")));
        Locator displayedEditBox = page.getByPlaceholder("Hide/Show Example");
        displayedEditBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("editboxscreenshot.png")));
        page.locator("#hide-textbox").click();
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagePostscreenshot.png")));
    }
}