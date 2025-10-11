import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CaptchaServiceTest {

    private HttpSession session;

    @Before
    public void setUp() {
        session = mock(HttpSession.class);
    }

    @Test
    public void testCaptchaGenerationAndValidation() {
        doAnswer(invocation -> {
            Integer value = invocation.getArgument(1);
            when(session.getAttribute("captchaAnswer")).thenReturn(value);
            return null;
        }).when(session).setAttribute(eq("captchaAnswer"), any());

        String captcha = CaptchaService.generateCaptcha(session);
        assertNotNull(captcha);
        assertTrue(captcha.contains("+"));

        Integer correctAnswer = (Integer) session.getAttribute("captchaAnswer");
        assertTrue(CaptchaService.validateCaptcha(session, correctAnswer));

        assertFalse(CaptchaService.validateCaptcha(session, correctAnswer + 1));
    }

    @Test
    public void testInvalidCaptcha() {
        when(session.getAttribute("captchaAnswer")).thenReturn(null);
        assertFalse(CaptchaService.validateCaptcha(session, 5));
    }
}
