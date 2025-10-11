import jakarta.servlet.http.HttpSession;

import java.util.Random;

public class CaptchaService {

    public static String generateCaptcha(HttpSession session){
        Random rand = new Random();
        int num1 = rand.nextInt(10)+1;
        int num2 = rand.nextInt(10)+1;
        int answer = num1 + num2;
        session.setAttribute("captchaAnswer", answer);
        return num1 + " + " + num2 + " = ?";
    }

    public static boolean validateCaptcha(HttpSession session, int userAnswer){
        Integer correct = (Integer) session.getAttribute("captchaAnswer");
        return correct != null && correct == userAnswer;
    }
}
