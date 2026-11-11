package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {


    //Invoke browser ->Invoke tab/page->type url
    //HeadMode- Headless
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage(); //browser.newContext();
        page.setDefaultTimeout(5000);
        page.navigate("https://eventhub.rahulshettyacademy.com/login");

    }

    @Test(description = "sandbox banner is shown when 6 events are returned")
    public void DemoTest() {

        page.getByPlaceholder("you@email.com").fill("rahulshetty1@yahoo.com");
        page.getByLabel("Password").fill("Magiclife1!");
        //locator
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        //Step 1 - Create Event from Admin page
        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_6.json"))

        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");


        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible(); //5seconds -

        Assert.assertEquals(eventCards.count(), 6);
        assertThat(page.locator(".mx-1").first()).isVisible();

        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_4.json"))

        ));

        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventCards1 = page.getByTestId("event-card");
        assertThat(eventCards1.first()).isVisible(); //5seconds -

        Assert.assertEquals(eventCards1.count(), 4);
        assertThat(page.locator(".mx-1").first()).isHidden();
    }


    @Test
    public void routeResumeTest() {
        page.getByPlaceholder("you@email.com").fill("test@yahoo.com");
        page.getByLabel("Password").fill("Life1!");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        page.getByTestId("nav-bookings").click();

        page.route("**/api/bookings**", route -> route.resume(
                new Route.ResumeOptions().setUrl("https://*********/api/bookings/72116")
        ));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();

        assertThat(page.getByText("(?i)access denied")).isVisible();
    }
























}

