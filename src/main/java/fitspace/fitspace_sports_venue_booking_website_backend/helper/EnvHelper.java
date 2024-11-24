package fitspace.fitspace_sports_venue_booking_website_backend.helper;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvHelper {

    private Dotenv dotenv;

    public EnvHelper() {
        this.dotenv = Dotenv.load();
    }

    public String mailUsername() {
        return dotenv.get("MAIL_USERNAME");
    }

    public String mailPassword() {
        return dotenv.get("MAIL_PASSWORD");
    }

    public String googleClientId() {
        return dotenv.get("GOOGLE_CLIENT_ID");
    }

    public String googleClientSecret() {
        return dotenv.get("GOOGLE_CLIENT_SECRET");
    }
}

