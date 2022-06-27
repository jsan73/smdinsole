package com.smd21.smdinsole.app.exception;

import org.springframework.lang.Nullable;

public enum AppException {

	SUCCESS(0, ""),

	INTERNAL_SERVER_ERROR(500, "내부 서버 오류가 발생했습니다."),

	ACCESS_NOT_ALLOWED(3001, "허용되지 않는 접근입니다. "),

	HTTP_REQUEST_METHOD_NOT_SUPPORTED(3002, "지원하지 않는 Method입니다."),

	MISSING_PARAMETER(3003, "필수 파라미터가 누락 되었습니다."),

	INVALID_PARAMETER(3004, "파라미터가 올바르지 않습니다."),

	RQSTCLAS_NOT_SUPPORTED(3011, "지정되지 않은 요청구분 입니다."),

	NO_DATA_FOUND(3101, "검색결과가 없습니다."),

	NO_GUARD(3102, "보호자번호가 미등록되어 있습니다.<br>먼저 등록 요청 해주세요."),

	NO_MATCH_GUARD(3103, "로그인 실패 - 비밀번호가 틀립니다.");




	private final int value;
	private final String reasonPhrase;

	AppException(int value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}

	/**
	 * Return the integer value of this status code.
	 */
	public int value() {
		return this.value;
	}

	/**
	 * Return the reason phrase of this status code.
	 */
	public String getReasonPhrase() {
		return this.reasonPhrase;
	}

	@Override
	public String toString() {
		return this.value + " " + name();
	}

	public static AppException valueOf(int errorCode) {
		AppException error = resolve(errorCode);
		if (error == null) {
			error = AppException.INTERNAL_SERVER_ERROR;
//			throw new IllegalArgumentException("No matching constant for [" + errorCode + "]");
		}
		return error;
	}

	@Nullable
	public static AppException resolve(int statusCode) {
		for (AppException error : values()) {
			if (error.value == statusCode) {
				return error;
			}
		}
		return null;
	}
}
