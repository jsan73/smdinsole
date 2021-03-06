package com.kokasin.insole.app.security.provider;

import com.kokasin.insole.app.security.SecurityConfig;
import com.kokasin.insole.app.security.service.AuthUserDetails;
import com.kokasin.insole.common.CommonUtil;
import com.kokasin.insole.common.model.TokenUserModel;
import io.jsonwebtoken.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JwtTokenProvider {

	private String secretKey;

	private String header = SecurityConfig.HEADER_TOKEN;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(SecurityConfig.SECRET_KEY.getBytes());
	}

	public String getSecretKey(){
		return secretKey;
	}


	// Jwt 토큰 생성
	public String createToken(TokenUserModel tokenUser, int addDate) {

		Claims claims = Jwts.claims().setSubject("insole token");

		String loginId = tokenUser.getLoingId();
		if(loginId != null) {
			claims.put("loginId", loginId);
		}else {
			claims.put("guardNo", tokenUser.getGuardNo());
			claims.put("guardPhone", tokenUser.getGuardPhone());
			claims.put("masterGuardNo", tokenUser.getMasterGuardNo());
			claims.put("maketingAgreeYn", tokenUser.getMaketingAgreeYn());
		}

		claims.put("roles", tokenUser.getRoles());
		Date nowDate = new Date();
		//Date exDate = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowDate);
		calendar.add(Calendar.DATE, addDate);
//		calendar.set(Calendar.HOUR_OF_DAY, 23);
//		calendar.set(Calendar.MINUTE, 59);
//		calendar.set(Calendar.SECOND, 59);

		Date exDate = calendar.getTime();

		return Jwts.builder()
				.setClaims(claims) // 데이터
				.setIssuedAt(nowDate) // 토큰 발행일
				.setExpiration(exDate) // set Expire Time
				.signWith(SignatureAlgorithm.HS512, secretKey) // 암호화 알고리즘, secret값 세팅
				.compact();
	}

	// Jwt 토큰으로 인증 정보를 조회
	public Authentication getAuthentication(String token) {
		return getAuthentication(token, secretKey);
	}


	public Authentication getAuthentication(String token, String secretKey) {

		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

		TokenUserModel tokenUser = claimsToObject(claims);
		AuthUserDetails userDetails = new AuthUserDetails();

		String loginId = tokenUser.getLoingId();
		if(loginId != null) {
			userDetails.setLoingId(loginId);
		}else {
			userDetails.setGuardNo(tokenUser.getGuardNo());
			userDetails.setGuardPhone(tokenUser.getGuardPhone());
			userDetails.setMasterGuardNo(tokenUser.getMasterGuardNo());
			userDetails.setMaketingAgreeYn(tokenUser.getMaketingAgreeYn());
		}
		userDetails.setRoles(tokenUser.getRoles());
		userDetails.setAccessToken(token);

		//userDetails.setUserNo(tokenUser.getGuardNo());

		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String role : userDetails.getRoles()) {
			  authorities.add(new SimpleGrantedAuthority(role));
		}
		userDetails.setAuthorities(authorities);

		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}


	// Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
	public String resolveToken(HttpServletRequest req) {
		return req.getHeader(header);
	}

	// Jwt 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	public TokenUserModel getTokenInfo(String token) throws ExpiredJwtException {

		TokenUserModel tokenUser = new TokenUserModel();

		Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
		if(!claims.getBody().getExpiration().before(new Date())) {
			tokenUser = claimsToObject(claims.getBody());
		}
		return tokenUser;
	}

	@SuppressWarnings("unchecked")
	private TokenUserModel claimsToObject(Claims body) {

		TokenUserModel tokenUser = new TokenUserModel();

		String loginId = CommonUtil.empty(body.get("loginId")) ? null : body.get("loginId").toString();

		if(loginId != null) {
			tokenUser.setLoingId(loginId);
		}else {
			long guardNo = CommonUtil.empty(body.get("guardNo").toString()) ? 0 : Long.parseLong(body.get("guardNo").toString());
			long masterGuardNo = CommonUtil.empty(body.get("masterGuardNo").toString()) ? 0 : Long.parseLong(body.get("masterGuardNo").toString());
			String maketingAgreeYn = CommonUtil.empty(body.get("maketingAgreeYn")) ? "N" : body.get("maketingAgreeYn").toString();
			tokenUser.setGuardNo(guardNo);
			tokenUser.setMasterGuardNo(masterGuardNo);
			tokenUser.setMaketingAgreeYn(maketingAgreeYn);
			tokenUser.setGuardPhone((String) body.get("guardPhone"));
		}
		tokenUser.setRoles((List<String>)body.get("roles"));

		return tokenUser;
	}

}
