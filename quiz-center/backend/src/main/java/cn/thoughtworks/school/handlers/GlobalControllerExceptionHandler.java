package cn.thoughtworks.school.handlers;

import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationError handleException(MethodArgumentNotValidException exception) {
        return createValidationError(exception);
    }

    private ValidationError createValidationError(MethodArgumentNotValidException e) {
        return ValidationErrorBuilder.fromBindingErrors(e.getBindingResult());
    }

    /**
     * to be continue
     */
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity handleRestClientException(HttpServletRequest req, RestClientException e) {
        Map<String, Object> result = new HashMap<>();
        System.out.println(e.getMessage());
        result.put("message", e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleValidationException(HttpServletRequest req, ValidationException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "测试数据JSON格式不符合规范");
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JSONException.class)
    public ResponseEntity handleJSONException(HttpServletRequest req, JSONException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "测试数据JSON格式错误");
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
