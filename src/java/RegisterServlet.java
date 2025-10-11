import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {
    private UserRepository userRepository = new UserRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        req.setAttribute("captchaQuestion",CaptchaService.generateCaptcha(req.getSession()));
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String captchaAnswer = req.getParameter("captchaAnswer");

        User user = new User(name, email, password);
        String error = ValidationService.validateUser(user);
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("captchaQuestion", CaptchaService.generateCaptcha(req.getSession()));
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        int captchaValue;
        try {
            captchaValue = Integer.parseInt(captchaAnswer);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid answer of captcha!");
            req.setAttribute("captchaQuestion", CaptchaService.generateCaptcha(req.getSession()));
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if (!CaptchaService.validateCaptcha(req.getSession(), captchaValue)) {
            req.setAttribute("error", "Invalid captcha!!");
            req.setAttribute("captchaQuestion", CaptchaService.generateCaptcha(req.getSession()));
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        try {
            if (userRepository.findByEmail(email) != null) {
                req.setAttribute("error", "Email already exist!");
                req.setAttribute("captchaQuestion", CaptchaService.generateCaptcha(req.getSession()));
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
                return;
            }
            userRepository.save(user);
            resp.sendRedirect("/registration/login");
        } catch (SQLException e) {
            req.setAttribute("error", "Error in database!");
            req.setAttribute("captchaQuestion", CaptchaService.generateCaptcha(req.getSession()));
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}
