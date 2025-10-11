import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserRepositoryTest {
    private UserRepository userRepository;
    private Connection conn;

    @Before
    public void setUp() throws SQLException {
        // In-memory H2 база
        conn = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        // Премахваме таблицата, ако вече съществува
        conn.createStatement().execute("DROP TABLE IF EXISTS users");
        conn.createStatement().execute(
                "CREATE TABLE users (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(50), " +
                        "email VARCHAR(100) UNIQUE, " +
                        "password VARCHAR(255))"
        );

        // Подменяме getConnection() за тестовете
        userRepository = new UserRepository() {
            @Override
            protected Connection getConnection() throws SQLException {
                return conn;
            }
        };
    }

    @Test
    public void testSaveAndFindByEmail() throws SQLException {
        User user = new User("Test User", "test@example.com", "password123");
        user = userRepository.save(user);

        assertNotNull(user.getId());
        User found = userRepository.findByEmail("test@example.com");
        assertEquals("Test User", found.getName());
        assertTrue(found.getPassword().startsWith("$2a$"));
    }

    @Test
    public void testFindByEmailNotFound() throws SQLException {
        User found = userRepository.findByEmail("nonexistent@example.com");
        assertNull(found);
    }

    @Test
    public void testUpdate() throws SQLException {
        User user = new User("Old Name", "user@example.com", "oldpass");
        userRepository.save(user);

        User updated = new User("New Name", "user@example.com", "newpass");
        userRepository.update(updated);

        User found = userRepository.findByEmail("user@example.com");
        assertEquals("New Name", found.getName());
        assertTrue(found.getPassword().startsWith("$2a$"));
    }

    @Test
    public void testUpdateWithoutPasswordChange() throws SQLException {
        User user = new User("Old Name", "user@example.com", "oldpass");
        userRepository.save(user);

        User updated = new User("New Name", "user@example.com", "");
        userRepository.update(updated);

        User found = userRepository.findByEmail("user@example.com");
        assertEquals("New Name", found.getName());
        assertTrue(found.getPassword().startsWith("$2a$"));
    }
}
