package cn.thoughtworks.school.programCenter.handlers;

import cn.thoughtworks.school.programCenter.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
//@ResponseBody
class GlobalControllerExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException e) {

        Set<String> messages = new HashSet<>(e.getConstraintViolations().size());

        messages.addAll(e.getConstraintViolations().stream()
                .map(constraintViolation -> String.format("%s value '%s' %s", constraintViolation.getPropertyPath(),
                        constraintViolation.getInvalidValue(), constraintViolation.getMessage()))
                .collect(Collectors.toList()));

        Map result = new HashMap<String, Object>();
        result.put("message", messages);

        return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessException(HttpServletRequest req, BusinessException e) {
        Map result = new HashMap<String, Object>();
        result.put("message", e.getMessage());
        return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(HttpServletRequest req, Exception e) {
        Map result = new HashMap<String, Object>();
        result.put("message", e.getMessage());
        e.printStackTrace();

        return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
