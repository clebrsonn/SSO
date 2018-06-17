package com.api.mv.sso.token;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

    public static final String COOKIE_NAME = "refresh_token";

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.getMethod().getName().equals("postAccessToken");
    }

    @Override
    public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken oAuth2AccessToken, MethodParameter methodParameter,
                                             MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                             ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        HttpServletResponse response = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();

        String refreshToken = oAuth2AccessToken.getRefreshToken().getValue();
        refreshTokenToCookie(refreshToken, request, response);

        DefaultOAuth2AccessToken body = (DefaultOAuth2AccessToken) oAuth2AccessToken;

        removeRefreshTokenOfBody(body);

        return body;
    }

    protected void removeRefreshTokenOfBody(DefaultOAuth2AccessToken body) {
        body.setRefreshToken(null);
    }


    protected void refreshTokenToCookie(String refreshToken, HttpServletRequest request, HttpServletResponse response) {

        Cookie refresh_token = new Cookie(COOKIE_NAME, refreshToken);

        refresh_token.setHttpOnly(true);

        refresh_token.setSecure(false); //TODO: mudar para true quando for fazer deploy

        refresh_token.setPath(request.getContextPath().concat("/auth/token"));

        refresh_token.setMaxAge(3600 * 24 * 10); //10 dias

        response.addCookie(refresh_token);


    }

    ;
}
