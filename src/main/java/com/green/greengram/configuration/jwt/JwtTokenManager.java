package com.green.greengram.configuration.jwt;


import com.green.greengram.configuration.constants.ConstJwt;
import com.green.greengram.configuration.model.JwtUser;
import com.green.greengram.configuration.model.UserPrincipal;
import com.green.greengram.configuration.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

//JWT 총괄 책임자
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenManager {
    private final ConstJwt constJwt; //설정 내용(문자열)
    private final CookieUtils cookieUtils; //쿠키 관련
    private final JwtTokenProvider jwtTokenProvider; //JWT 관련

    public void issue(HttpServletResponse response, JwtUser jwtUser) {
        setAccessTokenInCookie(response, jwtUser);
        setRefreshTokenInCookie(response, jwtUser);
    }

    public String generateAccessToken(JwtUser jwtUser) {
        return jwtTokenProvider.generateToken(jwtUser, constJwt.accessTokenValidityMilliseconds);
    }

    public void setAccessTokenInCookie(HttpServletResponse response, JwtUser jwtUser) {
        setAccessTokenInCookie(response, generateAccessToken(jwtUser));
    }

    public void setAccessTokenInCookie(HttpServletResponse response, String accessToken) {
        cookieUtils.setCookie(response
                            , constJwt.accessTokenCookieName
                            , accessToken
                            , constJwt.accessTokenCookieValiditySeconds
                            , constJwt.accessTokenCookiePath
                            , constJwt.domain
        );
    }

    public String getAccessTokenFromCookie(HttpServletRequest request) {
        return cookieUtils.getValue(request, constJwt.accessTokenCookieName);
    }

    public void deleteAccessTokenInCookie(HttpServletResponse response) {
        cookieUtils.deleteCookie(response
                , constJwt.accessTokenCookieName
                , constJwt.accessTokenCookiePath
                , constJwt.domain);
    }

    public String generateRefreshToken(JwtUser jwtUser) {
        return jwtTokenProvider.generateToken(jwtUser, constJwt.refreshTokenValidityMilliseconds);
    }

    public void setRefreshTokenInCookie(HttpServletResponse response, JwtUser jwtUser) {
        setRefreshTokenInCookie(response, generateRefreshToken(jwtUser));
    }

    public void setRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        cookieUtils.setCookie(response
                            , constJwt.refreshTokenCookieName
                            , refreshToken
                            , constJwt.refreshTokenCookieValiditySeconds
                            , constJwt.refreshTokenCookiePath
                            , constJwt.domain);
    }

    public void deleteRefreshTokenInCookie(HttpServletResponse response) {
        cookieUtils.deleteCookie(response
                                , constJwt.refreshTokenCookieName
                                , constJwt.refreshTokenCookiePath
                                , constJwt.domain);
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        return cookieUtils.getValue(request, constJwt.refreshTokenCookieName);
    }

    public JwtUser getJwtUserFromToken(String token) {
        return jwtTokenProvider.getJwtUserFromToken(token);
    }

    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        //request에서 refreshToken을 얻는다.
        String refreshToken = getRefreshTokenFromCookie(request);
        log.info("refreshToken: {}", refreshToken);

        //refreshToken에서 jwtUser를 만든다.
        JwtUser jwtUser = getJwtUserFromToken(refreshToken);
        log.info("jwtUser: {}", jwtUser);

        //jwtUser로 accessToken을 발행한다.
        String accessToken = generateAccessToken(jwtUser);
        log.info("accessToken: {}", accessToken);

        //accessToken을 쿠키에 담는다.
        setAccessTokenInCookie(response, accessToken);
    }

    public void signOut(HttpServletResponse response) {
        deleteAccessTokenInCookie(response);
        deleteRefreshTokenInCookie(response);
    }
}
