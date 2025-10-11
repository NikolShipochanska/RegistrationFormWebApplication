import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserRepository {
    protected Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public User save(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId((int) rs.getLong(1));
            }
            return user;
        }
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId((int) rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                return user;
            }
            return null;
        }
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, password = ? WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword().isEmpty() ? findByEmail(user.getEmail()).getPassword() : BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();
        }
    }
}
