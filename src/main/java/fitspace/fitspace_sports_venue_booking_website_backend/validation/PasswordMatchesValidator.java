package fitspace.fitspace_sports_venue_booking_website_backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        try {
            Field passwordField = value.getClass().getDeclaredField("password");
            Field confirmationPasswordField = value.getClass().getDeclaredField("confirmationPassword");

            passwordField.setAccessible(true);
            confirmationPasswordField.setAccessible(true);

            String password = (String) passwordField.get(value);
            String confirmationPassword = (String) confirmationPasswordField.get(value);

            return password != null && password.equals(confirmationPassword);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Fields 'password' and 'confirmationPassword' must be present", e);
        }
    }
}
