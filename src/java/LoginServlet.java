import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
    private UserRepository userRepository = new UserRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = userRepository.findByEmail(email);
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                req.getSession().setAttribute("userEmail", email);
                resp.sendRedirect("/registration/profile");
            } else {
                req.setAttribute("error", "Invalid email or password!");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            req.setAttribute("error", "Error in the database!");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
