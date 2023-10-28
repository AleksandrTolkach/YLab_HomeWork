package by.toukach.walletservice.controller.in.filter;

import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.enumiration.UserRole;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.security.SecurityContext;
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
 * Класс для ограничения доступа к пользователям.
 */
@WebFilter(filterName = "filter4", urlPatterns = {"/user"})
public class UserFilter implements Filter {

  private static final String GET_METHOD = "GET";
  private static final String ID = "id";
  private static final String LOGIN = "login";

  public UserFilter() {
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse resp = (HttpServletResponse) servletResponse;

    UserDto currentUser = SecurityContext.getCurrentUser();

    if (currentUser.getRole().equals(UserRole.ADMIN)) {
      filterChain.doFilter(req, resp);
      return;
    }

    if (req.getMethod().equals(GET_METHOD)) {
      String id = req.getParameter(ID);
      if (!StringUtils.isBlank(id) && !currentUser.getId().toString().equals(id)) {
        resp.sendError(HttpServletResponse.SC_FORBIDDEN, ExceptionMessage.ELSE_USER);
      }

      String login = req.getParameter(LOGIN);
      if (!StringUtils.isBlank(login) && !login.equals(currentUser.getLogin())) {
        resp.sendError(HttpServletResponse.SC_FORBIDDEN, ExceptionMessage.ELSE_USER);
        return;
      }
    }

    filterChain.doFilter(req, resp);
  }
}
