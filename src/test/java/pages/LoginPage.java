package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {
    Page page;
    String baseUrl;
    private static final String email_placeHolder = "you@email.com";
    private static final String password_label = "Password";

    public LoginPage(Page page, String baseUrl) {
        this.page = page;
        this.baseUrl = baseUrl;
    }

    public DashboardPage loginToApplication() {
        page.navigate(baseUrl);
        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByPlaceholder(email_placeHolder).fill("rahulshetty1@yahoo.com");
        page.getByLabel(password_label).fill("Magiclife1!");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        DashboardPage dashboardPage = new DashboardPage(page);
        return dashboardPage;
    }
}
