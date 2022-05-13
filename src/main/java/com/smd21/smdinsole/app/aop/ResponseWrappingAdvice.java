package com.smd21.smdinsole.app.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smd21.smdinsole.app.exception.AppException;
import com.smd21.smdinsole.common.model.RestOutModel;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@RestControllerAdvice(annotations = RestController.class)
public class ResponseWrappingAdvice implements ResponseBodyAdvice<Object> {

	final static Logger logger = LoggerFactory.getLogger(ResponseWrappingAdvice.class);

	private static final String SUC = "SUCCESS";

    @Override
    public boolean supports(final MethodParameter p_methodParameter, final Class<? extends HttpMessageConverter<?>> p_class)
    {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
        ServerHttpResponse response) {
        // TODO Auto-generated method stub

        final RestOutModel<Object> output = new RestOutModel<>();
        output.setData(body);
        output.setCode(AppException.SUCCESS);
        output.setStatus(SUC);

//        logger.debug(output.toString());
        if(body instanceof String) {
            ObjectMapper om = new ObjectMapper();
            return om.writeValueAsString(output);
        }

        return output;

    }

}
