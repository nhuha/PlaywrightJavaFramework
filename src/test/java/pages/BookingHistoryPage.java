package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingHistoryPage {
    Page page;

    public BookingHistoryPage(Page page) {
        this.page = page;
    }

    public void goTo() {
        page.locator("#nav-events").click();
    }

    public void book(String bookingRef) {
        Locator bookingCards = page.locator("#booking-card");
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(targetBookingCard).isVisible();
        // seat count reduction check
        page.locator("#nav-events").click();
        page.waitForTimeout(1000);
    }

    public int getSeatsNumAfterBooking(String title) {
        Locator eventCardsAfterBooking = page.getByTestId("event-card"); //{loc1,loc2,loc3,loc4}
        //Visibility of the card which we have added
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText(title));
        String seatsTextAfterBooking = targetCardAfterBooking.getByText("seats").innerText();
        System.out.println(seatsTextAfterBooking);

        return Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);

    }
}
