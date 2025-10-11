import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public class ProfileServlet extends HttpServlet {
    private UserRepository userRepository = new UserRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = (String) req.getSession().getAttribute("userEmail");
        if (email == null) {
            resp.sendRedirect("/registration/login");
            return;
        }
        try {
            User user = userRepository.findByEmail(email);
            req.setAttribute("user", user);
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "Грешка в базата данни");
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = (String) req.getSession().getAttribute("userEmail");
        if (email == null) {
            resp.sendRedirect("/registration/login");
            return;
        }

        String name = req.getParameter("name");
        String password = req.getParameter("password");
        User user = new User(name, email, password);

        String error = ValidationService.validateUser(user);
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("user", user);
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
            return;
        }

        try {
            userRepository.update(user);
            resp.sendRedirect("/registration/profile");
        } catch (SQLException e) {
            req.setAttribute("error", "Error in the database!");
            req.setAttribute("user", user);
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
        }
    }
}
