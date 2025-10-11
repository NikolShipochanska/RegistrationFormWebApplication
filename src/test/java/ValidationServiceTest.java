import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValidationServiceTest {
    @Test
    public void testValidUser() {
        User user = new User("Test User", "test@example.com", "password123");
        assertNull(ValidationService.validateUser(user));
    }

    @Test
    public void testInvalidName() {
        User user = new User("", "test@example.com", "password123");
        assertEquals("The username should be between 2 and 50 symbols", ValidationService.validateUser(user));
        user.setName("A");
        assertEquals("The username should be between 2 and 50 symbols", ValidationService.validateUser(user));
        user.setName("A".repeat(51));
        assertEquals("The username should be between 2 and 50 symbols", ValidationService.validateUser(user));
    }

    @Test
    public void testInvalidEmail() {
        User user = new User("Test User", "invalid-email", "password123");
        assertEquals("Invalid email", ValidationService.validateUser(user));
        user.setEmail(null);
        assertEquals("Invalid email", ValidationService.validateUser(user));
    }

    @Test
    public void testInvalidPassword() {
        User user = new User("Test User", "test@example.com", "pass");
        assertEquals("Password must be at least 8 symbols", ValidationService.validateUser(user));
        user.setPassword(null);
        assertEquals("Password must be at least 8 symbols", ValidationService.validateUser(user));
    }
}