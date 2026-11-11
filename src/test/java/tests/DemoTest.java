package tests;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.DataProviderUtil;

import java.io.IOException;
import java.util.HashMap;

public class DemoTest extends BaseTest {


    @DataProvider(name = "eventBookingData")
    public Object[][] eventBookingData() throws IOException {
        return DataProviderUtil.getJsonDataToMap("/src/test/resources/eventBookingData.json");
    }

    @Test(dataProvider = "eventBookingData", description = "Create and book event", groups = {"smoke"})
    public void DemoTest(HashMap<String, String> data) {
        LoginPage loginPage = new LoginPage(page, baseUrl);
        DashboardPage dashboardPage = loginPage.loginToApplication();

        dashboardPage.waitForEventsToLoad();

        //create event
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.goTo();
        adminEventsPage.createEvent(data.get("titlePrefix"),
                data.get("description"),
                data.get("city"),
                data.get("venue"),
                data.get("dateTime"),
                data.get("price"),
                data.get("totalSeats"));

        //find new created event
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        Locator targetCard = eventsPage.findEventCard(data.get("titlePrefix"));
        int seatsNumBeforeBooking = eventsPage.getSeatsCount(targetCard);
        BookingFormPage bookingFormPage = eventsPage.proceedToBookingEvent(targetCard);

        // Book the ticket for event
        bookingFormPage.fillAndConfirm(data.get("fullName"),
                data.get("email"),
                data.get("phone"));

        String bookingRef = bookingFormPage.getBookingRef();
        BookingHistoryPage bookingHistoryPage = bookingFormPage.navigateToBookingHistoryPage();

        bookingHistoryPage.book(bookingRef);
        int seatsNumAfterBooking = bookingHistoryPage.getSeatsNumAfterBooking(data.get("titlePrefix"));

        //verify that ticket is booked
        Assert.assertTrue(seatsNumBeforeBooking > seatsNumAfterBooking);
    }
}
