package com.smd21.smdinsole.app.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smd21.smdinsole.app.security.provider.JwtTokenProvider;
import com.smd21.smdinsole.common.ObjectDataUtil;
import com.smd21.smdinsole.common.model.RestOutModel;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class JwtAuthFilter extends GenericFilterBean {

	private JwtTokenProvider jwtTokenProvider;

    public JwtAuthFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// TODO Auto-generated method stub
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		try {

			Jws<Claims> claims = Jwts.parser().setSigningKey(jwtTokenProvider.getSecretKey()).parseClaimsJws(token);

			if (token != null && !claims.getBody().getExpiration().before(new Date())) {
				Authentication auth = jwtTokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			try {
				if (ObjectDataUtil.notEmpty(claims)
						&& ObjectDataUtil.notEmpty(claims.getBody())
						&& claims.getBody().containsKey("guardNo")) {
					request.setAttribute("guardNo", claims.getBody().get("guardNo"));
				}
			}catch(Exception e2) {
				e2.printStackTrace();
			}

			chain.doFilter(request, response);

		} catch (SignatureException | MalformedJwtException e) {


			try {
				commence((HttpServletResponse)response, 401, "SignatureException error");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


		} catch (ExpiredJwtException e) {

			//유효 기간이 지난 JWT를 수신한 경우
//			((HttpServletResponse) response).sendError(402, "ExpiredJwtException error");
			try {
				commence((HttpServletResponse)response, 401, "ExpiredJwtException error");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (IllegalArgumentException e) {

			//유효 기간이 지난 JWT를 수신한 경우
//			((HttpServletResponse) response).sendError(402, "ExpiredJwtException error");
			try {
				commence((HttpServletResponse)response, 401, "JWT String argument cannot be null or empty.");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


		} catch(Exception e) {

			((HttpServletResponse) response).sendError(500, e.toString());
		}

	}

	private void commence(HttpServletResponse response, int code, String message) throws Exception {

		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		response.setStatus(code);

		ObjectMapper objectMapper = new ObjectMapper();

		RestOutModel<Object> outDto = new RestOutModel<Object>();
		outDto.setStatus("FAIL");
		outDto.setCode(code);
		outDto.setMessage(message);
		String json = objectMapper.writeValueAsString(outDto);

		PrintWriter printWriter = response.getWriter();
		printWriter.write(json);
		printWriter.flush();
	}

}
