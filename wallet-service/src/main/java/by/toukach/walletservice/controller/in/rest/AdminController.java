package by.toukach.walletservice.controller.in.rest;

import by.toukach.logger.dto.LogDto;
import by.toukach.walletservice.service.audit.AuditService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для обработки запросов, связанных с администрированием приложения.
 */
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminController {

  private final AuditService auditService;

  /**
   * Метод для работы с запросом на вывод логов приложения.
   *
   * @return запрашиваемые логи.
   */
  @GetMapping("/logs")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ADMIN')")
  public List<LogDto> findLogs() {
    return auditService.findLogs();
  }
}
