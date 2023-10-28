package by.toukach.walletservice.controller.in.rest;

import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.service.TransactionService;
import by.toukach.walletservice.service.handler.TransactionHandler;
import by.toukach.walletservice.service.handler.TransactionHandlerFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionHandlerFactory transactionHandlerFactory;
  private final TransactionService transactionService;

  @GetMapping("/user/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public List<TransactionDto> findTransactionsByUserId(@PathVariable Long userId) {
    return transactionService.findTransactionByUserId(userId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TransactionDto createTransaction(@RequestBody TransactionDto transactionDto) {
    TransactionHandler handler = transactionHandlerFactory.getHandler(transactionDto.getType());
    return handler.handle(transactionDto);
  }
}
