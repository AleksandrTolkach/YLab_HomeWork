package by.toukach.walletservice.controller.in.servlet;

import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.service.LoggerService;
import by.toukach.walletservice.service.impl.LoggerServiceImpl;
import by.toukach.walletservice.utils.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервлет для обработки HTTP запросов, связанных с логированием.
 */
@WebServlet("/admin/logger")
public class LoggerServlet extends HttpServlet {

  private static final String CONTENT_TYPE = "application/json; charset=UTF-8";

  private final LoggerService loggerService;

  public LoggerServlet() {
    loggerService = LoggerServiceImpl.getInstance();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    List<Log> logs = loggerService.findLogs();
    List<String> logsJson = logs.stream()
        .map(JsonUtil::mapToJson)
        .collect(Collectors.toList());

    PrintWriter writer = resp.getWriter();
    writer.println(logsJson);

    resp.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    resp.setContentType(CONTENT_TYPE);

    resp.setStatus(HttpServletResponse.SC_OK);
  }
}
