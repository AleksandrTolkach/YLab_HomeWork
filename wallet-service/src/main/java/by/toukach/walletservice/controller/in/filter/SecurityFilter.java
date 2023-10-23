package by.toukach.walletservice.controller.in.filter;

import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.security.Authentication;
import by.toukach.walletservice.security.AuthenticationManager;
import by.toukach.walletservice.security.impl.AuthenticationManagerImpl;
import by.toukach.walletservice.utils.CookieUtil;
import by.toukach.walletservice.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/**
 * Класс для ограничения доступа к приложению не авторизованным пользователям.
 */
@WebFilter(filterName = "filter1",
    urlPatterns = {"/account", "/transaction", "/user", "/auth/logout", "/admin/*"})
public class SecurityFilter implements Filter {

  private final AuthenticationManager authenticationManager;

  public SecurityFilter() {
    authenticationManager = AuthenticationManagerImpl.getInstance();
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse resp = (HttpServletResponse) servletResponse;

    String accessToken = CookieUtil.getAccessTokenFromCookies(req);

    String login = null;
    try {
      login = JwtUtil.getUsernameFromJwtToken(accessToken);
    } catch (ExpiredJwtException e) {
      resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, ExceptionMessage.SESSION_EXPIRED);
      return;
    }

    if (!StringUtils.isBlank(login)) {
      Authentication authentication = authenticationManager.getAuthentication(login);
      if (authentication.isAuthenticated()) {
        filterChain.doFilter(servletRequest, servletResponse);
      } else {
        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, ExceptionMessage.LOGIN_OFFER);
      }
    } else {
      resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, ExceptionMessage.LOGIN_OFFER);
    }
  }
}
