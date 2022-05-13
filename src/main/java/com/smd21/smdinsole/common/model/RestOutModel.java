package com.smd21.smdinsole.common.model;


import com.smd21.smdinsole.app.exception.AppException;
import lombok.Getter;

@Getter
public class RestOutModel<T> {

    private String status = "SUCCESS";
    private Object code;
//    private String devMessage;
    private String message;

    private T data;

    public RestOutModel() {

    }

	@SuppressWarnings("unchecked")
	public RestOutModel(int code, String status, String message, Object data) {
		this.code = code;
		this.status = status;
		this.message = message;
		this.data = (T)data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCode() {

		if(code instanceof Integer) return (Integer) code;
		else {
			AppException err = (AppException) code;
			return err.value();
		}
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setCode(AppException appError) {
		this.code = AppException.valueOf(appError.value());
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RestOutModel [status=");
		builder.append(status);
		builder.append(", code=");
		builder.append(code);
		builder.append(", message=");
		builder.append(message);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

}
