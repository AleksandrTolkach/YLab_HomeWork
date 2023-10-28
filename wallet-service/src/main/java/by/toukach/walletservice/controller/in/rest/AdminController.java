package by.toukach.walletservice.controller.in.rest;

import by.toukach.walletservice.dto.LogDto;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.service.LoggerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final LoggerService loggerService;

  @GetMapping("/logs")
  @ResponseStatus(HttpStatus.OK)
  public List<LogDto> findLogs() {
    return loggerService.findLogs();
  }
}
