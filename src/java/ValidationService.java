import java.util.regex.Pattern;

public class ValidationService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public static String validateUser(User user){
        if(user.getName() == null || user.getName().trim().length() < 2 || user.getName().length() > 50){
            return "The username should be between 2 and 50 symbols";
        }
        if(user.getEmail() == null || !EMAIL_PATTERN.matcher(user.getEmail()).matches()){
            return "Invalid email";
        }
        if(user.getPassword() == null || user.getPassword().length() < 8){
            return "Password must be at least 8 symbols";
        }

        return null;
    }
}
