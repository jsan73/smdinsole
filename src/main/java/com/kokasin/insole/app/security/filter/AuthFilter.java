//package com.kokasin.insole.app.security.filter;
//
//import com.richnco.sso.lib.AuthModel;
//import com.richnco.sso.lib.AuthUser;
//import com.kokasin.insole.app.security.SecurityConfig;
//import com.kokasin.insole.app.security.service.AuthUserDetails;
//import com.kokasin.insole.user.model.GuardianModel;
//import com.kokasin.insole.user.service.UserService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetails;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.web.filter.GenericFilterBean;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class AuthFilter extends GenericFilterBean {
//	final static Logger logger = LoggerFactory.getLogger(AuthFilter.class);
//
//	private AuthenticationManager authenticationManager;
//
//	private String header = SecurityConfig.HEADER_TOKEN;
//
//	public AuthFilter(AuthenticationManager authenticationManager) {
//		this.authenticationManager = authenticationManager;
//	}
//
//	@Autowired
//	private UserService userService;
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse res = (HttpServletResponse) response;
//
//		try {
//
//			SecurityContext context = SecurityContextHolder.getContext();
//			Authentication auth = context.getAuthentication();
//
//			AuthModel authModel = AuthUser.getUserInfo(req, res);
//			boolean isLogin = authModel.getIsLogin().equals("Y");
//
//			String guardPhone = req.getParameter("phoneNumber");
//			String password = req.getParameter("pwd");
//			GuardianModel guardianModel = userService.loadUserByUserNo(guardPhone);
//
//			String loginId = "";
//			if ( isLogin ) {
//
//				loginId = authModel.getLoginId();
//				try {request.setAttribute("loginId", loginId);} catch(Exception e2) {}
//
//				if(auth == null) {
//
//					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginId, loginId);
//					((HttpServletRequest) request).getSession();
//
//					token.setDetails(new WebAuthenticationDetails((HttpServletRequest) request));
//					Authentication authenticatedUser = authenticationManager.authenticate(token);
//					SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
//
//					//Header??? ?????? ??????.
//					AuthUserDetails authUser = (AuthUserDetails) authenticatedUser.getPrincipal();
//					res.setHeader(header, authUser.getAccessToken());
//					response = res;
//
//				}
//
//			} else {
//
//				if(auth != null) new SecurityContextLogoutHandler().logout(req, res, auth);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		response = res;
//
//		chain.doFilter(request, response);
//	}
//
//}
