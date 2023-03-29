package biz.digissance.graalvmdemo.http;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler({ConstraintViolationException.class})
    public String constraintViolation(HttpServletRequest req, ConstraintViolationException e) {
        // Nothing to do.  Returns the logical view name of an error page, passed
        // to the view-resolver(s) in usual way.
        // Note that the exception is NOT available to this view (it is not added
        // to the model) but see "Extending ExceptionHandlerExceptionResolver"
        // below.
        log.error(e.getMessage(), e);
        return "constraintError";
    }

    @ExceptionHandler({Exception.class})
    public String anyException(HttpServletRequest req, Exception e) {
        // Nothing to do.  Returns the logical view name of an error page, passed
        // to the view-resolver(s) in usual way.
        // Note that the exception is NOT available to this view (it is not added
        // to the model) but see "Extending ExceptionHandlerExceptionResolver"
        // below.
        log.error(e.getMessage(), e);
        return "error";
    }
}