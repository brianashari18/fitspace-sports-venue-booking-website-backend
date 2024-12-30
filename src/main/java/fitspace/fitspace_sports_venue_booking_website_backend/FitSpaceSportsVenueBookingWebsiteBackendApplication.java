package fitspace.fitspace_sports_venue_booking_website_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "fitspace.fitspace_sports_venue_booking_website_backend")
public class FitSpaceSportsVenueBookingWebsiteBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitSpaceSportsVenueBookingWebsiteBackendApplication.class, args);
	}

}
