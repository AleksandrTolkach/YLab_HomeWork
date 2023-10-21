package by.toukach.walletservice.controller.views;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exception.ArgumentValueException;
import by.toukach.walletservice.exception.EntityDuplicateException;
import by.toukach.walletservice.exception.InsufficientFundsException;
import java.util.Scanner;

/**
 * Класс для вывода формы по списанию средств в консоль.
 * */
public class DebitAccountViewChain extends TransactionViewChain {

  public DebitAccountViewChain(AccountDto accountDto, UserDto userDto) {
    setAccountDto(accountDto);
    setUserDto(userDto);
  }

  @Override
  public void handle() {
    Scanner scanner = getScanner();

    System.out.println(ViewMessage.SUM);
    double answer = scanner.nextDouble();
    scanner.nextLine();

    TransactionDto transactionDto = TransactionDto.builder()
        .type(TransactionType.DEBIT)
        .userId(getUserDto().getId())
        .accountId(getAccountDto().getId())
        .value(answer)
        .build();

    try {
      getTransactionHandlerFactory().getHandler(TransactionType.DEBIT)
          .handle(transactionDto);
    } catch (InsufficientFundsException | EntityDuplicateException | ArgumentValueException e) {
      System.out.println(e.getMessage());
      setNextViewChain(this);
    }

    Long accountId = getAccountDto().getId();

    setNextViewChain(new SpecificAccountViewChain(getAccountService()
        .findAccountById(accountId), getUserDto()));
  }
}
