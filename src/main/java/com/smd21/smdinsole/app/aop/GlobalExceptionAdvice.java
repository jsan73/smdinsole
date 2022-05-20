package com.smd21.smdinsole.app.aop;


import com.smd21.smdinsole.app.exception.AppException;
import com.smd21.smdinsole.common.model.RestOutModel;
//import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 공통 Exception Handler
 *
 * @author jf0004
 * <p><b>NOTE:</b> </p>
 * @since 2020. 12. 15.
 * @version
 * @see
 *
 * <pre>
 * == 개정이력(Modification Information) ==
 *
 * 수정일 수정자 수정내용
 * ------- -------- ---------------------------
 *
 * </pre>
 */
@RestControllerAdvice
public class GlobalExceptionAdvice extends Exception {

	final static Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

	private static final long serialVersionUID = -5297056074777366872L;
	private static final String ERR = "FAIL";

	/**
	 *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
	 *  HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
	 *  주로 @RequestBody, @RequestPart 어노테이션에서 발생
	 */
	@SuppressWarnings("rawtypes")
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<RestOutModel> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

		final RestOutModel response = new RestOutModel(AppException.INVALID_PARAMETER.value(), ERR, AppException.INVALID_PARAMETER.getReasonPhrase(),getInValidedList(e.getBindingResult()));
		logger.debug(response.toString());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
	 */
	@SuppressWarnings("rawtypes")
	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<RestOutModel> handleAccessDeniedException(AccessDeniedException e) {

		final RestOutModel response = new RestOutModel(HttpStatus.FORBIDDEN.value(), ERR, e.getMessage(), null);
		logger.debug(response.toString());
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);

	}

	/**
	 * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
	 */
	@SuppressWarnings("rawtypes")
	@ExceptionHandler(AuthenticationException.class)
	protected ResponseEntity<RestOutModel> handleAuthenticationException(AuthenticationException e) {

		final RestOutModel response = new RestOutModel(HttpStatus.UNAUTHORIZED.value(), ERR, e.getMessage(), null);
		logger.debug(response.toString());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<RestOutModel> handleException(Exception e) {
		final RestOutModel response = new RestOutModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERR, e.getMessage(), null);
		logger.debug(response.toString());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(UsernameNotFoundException.class)
	protected ResponseEntity<RestOutModel> handleUsernameNotFoundException(Exception e) {
		final RestOutModel response = new RestOutModel(HttpStatus.UNAUTHORIZED.value(), ERR, e.getMessage(), null);
		logger.debug(response.toString());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}



	@SuppressWarnings("rawtypes")
	@ExceptionHandler(ConnectException.class)
	protected ResponseEntity<RestOutModel> ConnectException(ConnectException e) {

		String rponseMessage = e.getMessage();

		final RestOutModel response = new RestOutModel(5001, ERR, rponseMessage, null);
		logger.debug(response.toString());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	protected List<Map<String, Object>> getInValidedList(final BindingResult bindingResult) {
		final List<Map<String, Object>> fieldList = new ArrayList<>();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			Map<String, Object> errMap = new HashMap<>();

			errMap.put("field", fieldError.getField());
			errMap.put("defaultMessage", fieldError.getDefaultMessage());
			errMap.put("rejectedValue", fieldError.getRejectedValue());
			errMap.put("code", fieldError.getCode());
			fieldList.add(errMap);
		}

		return fieldList;
	}
}
