package by.toukach.walletservice.controller.in.rest;

import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.security.UserDetailsImpl;
import by.toukach.walletservice.service.TransactionService;
import by.toukach.walletservice.service.handler.TransactionHandler;
import by.toukach.walletservice.service.handler.TransactionHandlerFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для обработки запросов, связанных с транзакциями.
 */
@RestController
@RequestMapping(value = "/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionHandlerFactory transactionHandlerFactory;
  private final TransactionService transactionService;

  /**
   * Метод для обработки запроса по поиску транзакций текущего пользователя.
   *
   * @param userDetailsImpl данные о текущем пользователе.
   * @return запрашиваемые транзакции.
   */
  @GetMapping("/user")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('USER')")
  public List<TransactionDto> findTransactionsByUserId(
      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
    return transactionService.findTransactionByUserId(userDetailsImpl.getId());
  }

  /**
   * Метод для обработки запроса на создание трназакции.
   *
   * @param transactionDto данные о транзакции.
   * @return обработанная транзакция.
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('USER')")
  public TransactionDto createTransaction(
      @RequestBody(required = false) TransactionDto transactionDto) {
    TransactionHandler handler = transactionHandlerFactory.getHandler(transactionDto.getType());
    return handler.handle(transactionDto);
  }
}
