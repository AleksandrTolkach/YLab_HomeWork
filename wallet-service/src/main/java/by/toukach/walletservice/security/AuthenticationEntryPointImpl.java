package by.toukach.walletservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

/**
 * Класс для подготовки сообщения для аутентификационных исключений.
 */
@Service
@Primary
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

  private static final String MESSAGE = "message";
  private static final String PATH = "path";

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    Map<String, String> responseBodyMap = new HashMap<>();
    responseBodyMap.put(MESSAGE, authException.getMessage());
    responseBodyMap.put(PATH, request.getServletPath());

    objectMapper.writeValue(response.getOutputStream(), responseBodyMap);
  }
}
