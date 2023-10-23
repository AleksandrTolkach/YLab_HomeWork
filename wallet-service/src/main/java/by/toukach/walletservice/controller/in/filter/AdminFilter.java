package by.toukach.walletservice.controller.in.filter;

import by.toukach.walletservice.enumiration.UserRole;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.security.Authentication;
import by.toukach.walletservice.security.AuthenticationManager;
import by.toukach.walletservice.security.impl.AuthenticationManagerImpl;
import by.toukach.walletservice.utils.CookieUtil;
import by.toukach.walletservice.utils.JwtUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс для ограничения доступа к странице администратора.
 */
@WebFilter(filterName = "filter2", urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

  private final AuthenticationManager authenticationManager;

  public AdminFilter() {
    authenticationManager = AuthenticationManagerImpl.getInstance();
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse resp = (HttpServletResponse) servletResponse;

    String accessToken = CookieUtil.getAccessTokenFromCookies(req);

    String login = JwtUtil.getUsernameFromJwtToken(accessToken);

    Authentication authentication = authenticationManager.getAuthentication(login);
    UserRole authority = authentication.getAuthority();

    if (authority != null && authority.equals(UserRole.ADMIN)) {
      filterChain.doFilter(servletRequest, servletResponse);
    } else {
      resp.sendError(HttpServletResponse.SC_FORBIDDEN, ExceptionMessage.ACCESS_DENIED);
    }
  }
}
