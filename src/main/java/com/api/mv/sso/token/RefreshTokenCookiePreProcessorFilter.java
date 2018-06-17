package com.api.mv.sso.token;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Map;

import static com.api.mv.sso.token.RefreshTokenPostProcessor.COOKIE_NAME;


/**
 * Classe criada para pegar o refresh_token da requisição via cookie
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessorFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if ("oauth/token".equalsIgnoreCase(request.getRequestURI())
                && COOKIE_NAME.equalsIgnoreCase(request.getParameter("grant_type"))
                && request.getCookies() != null) {

            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equalsIgnoreCase(COOKIE_NAME)) {
                    String valueRefreshToken = cookie.getValue();
                    request = new MyServletRequestWraper(request, valueRefreshToken);
                }

            }

        }
        filterChain.doFilter(request, servletResponse);

    }

    @Override
    public void destroy() {

    }

    static class MyServletRequestWraper extends HttpServletRequestWrapper {
        private String refreshToken;

        public MyServletRequestWraper(HttpServletRequest request, String refreshToken) {
            super(request);
            this.refreshToken = refreshToken;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
            map.put(COOKIE_NAME, new String[]{refreshToken});
            map.setLocked(true);
            return map;
        }

    }
}
