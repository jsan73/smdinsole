//package com.kokasin.insole.app.security.provider;
//
//import com.kokasin.insole.app.security.service.AuthUserDetails;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class ServiceAuthenticationProvider implements AuthenticationProvider{
//
//	@Autowired
//    JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//    	final Logger logger = LoggerFactory.getLogger(ServiceAuthenticationProvider.class);
//
//    	String loginId = authentication.get .getName();
//        Authentication auth = null;
//
//        //인증
//        AuthUserDetails userDetails = new AuthUserDetails();
//
//        // 권한서버에서 해당 사용자의 정보 및 권한을 가져와야 함...
//        // 임시
//        String[] arrRoles = {"ROLE_ACCESS"};
//
//        userDetails.setUserNo() .setLoginId(loginId);
//        List<String> roles = Arrays.asList(arrRoles);
//
//		//Token 생성
//		userDetails.setRoles(roles);
//
//		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//		for (String role : roles) {
//			  authorities.add(new SimpleGrantedAuthority(role));
//		}
//		userDetails.setAuthorities(authorities);
//
////		JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
//		String accessToken = jwtTokenProvider.createToken(userDetails);
//		userDetails.setAccessToken(accessToken);
//
//		auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//        return auth;
//    }
//
//
//    @Override
//    public boolean supports(Class<?> arg0) {
//        return true;
//    }
//
//}
