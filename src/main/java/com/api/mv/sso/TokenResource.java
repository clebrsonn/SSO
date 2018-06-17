package com.api.mv.sso;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.api.mv.sso.token.RefreshTokenPostProcessor.COOKIE_NAME;

@RestController
@RequestMapping("tokens")
public class TokenResource {

    @DeleteMapping("/revoke")
    public void revoke(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshToken = new Cookie(COOKIE_NAME, null);
        refreshToken.setHttpOnly(true);
        refreshToken.setSecure(false);

        refreshToken.setPath(request.getContextPath().concat("/oauth/token"));

        refreshToken.setMaxAge(0);

        response.addCookie(refreshToken);

        response.setStatus(HttpStatus.NO_CONTENT.value());


    }
}
