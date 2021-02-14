package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.exception.UniqueException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    public static final String DEFAULT_ERROR_VIEW = "exception";

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, javax.validation.UnexpectedTypeException.class, org.springframework.validation.BindException.class})
    public String defaultErrorHandler(HttpServletRequest req, Exception e, Model model) throws Exception {
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        model.addAttribute("err", "Error 400: Invalid Inputs");
        model.addAttribute("url", req.getRequestURL());
        return DEFAULT_ERROR_VIEW;
    }

    @ExceptionHandler(value = {java.sql.SQLIntegrityConstraintViolationException.class, UniqueException.class})
    public String uniqueErrorHandler(HttpServletRequest req, Exception e, Model model) throws Exception {
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        model.addAttribute("err", "Error 409: It Is Already Defined In Database!");
        model.addAttribute("url", req.getRequestURL());
        return DEFAULT_ERROR_VIEW;
    }
}