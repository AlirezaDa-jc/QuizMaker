package ir.maktab.quizmaker.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Alireza.d.a
 */
public class UserNotAllowedException extends AuthenticationException {
    public UserNotAllowedException(String msg) {
        super(msg);
    }
}

