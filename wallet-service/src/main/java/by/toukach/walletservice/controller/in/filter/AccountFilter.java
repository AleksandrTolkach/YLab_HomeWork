package by.toukach.walletservice.controller.in.filter;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.UserDto;
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
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 * Класс для ограничения доступа к счетам.
 */
@WebFilter(filterName = "filter3", urlPatterns = {"/account"})
public class AccountFilter implements Filter {

  private static final String GET_METHOD = "GET";
  private static final String ID = "id";
  private static final String USER_ID = "userId";

  public AccountFilter() {
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse resp = (HttpServletResponse) servletResponse;

    UserDto currentUser = SecurityContext.getCurrentUser();

    if (currentUser == null) {
      resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, ExceptionMessage.LOGIN_OFFER);
      return;
    }

    if (req.getMethod().equals(GET_METHOD)) {
      String id = req.getParameter(ID);
      if (!StringUtils.isBlank(id)) {
        Optional<AccountDto> accountDtoOptional = currentUser.getAccountList().stream()
            .filter(a -> a.getId().toString().equals(id))
            .findFirst();

        if (accountDtoOptional.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN, ExceptionMessage.SOMEONE_ELSE_ACCOUNT);
          return;
        }
      }

      String userId = req.getParameter(USER_ID);
      if (!StringUtils.isBlank(userId) && !userId.equals(currentUser.getId().toString())) {
        resp.sendError(HttpServletResponse.SC_FORBIDDEN, ExceptionMessage.SOMEONE_ELSE_ACCOUNT);
        return;
      }
    }

    filterChain.doFilter(req, resp);
  }
}
