package by.toukach.walletservice.controller.views;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exception.ArgumentValueException;
import by.toukach.walletservice.exception.EntityDuplicateException;
import java.util.Scanner;

/**
 * Класс для вывода формы по начислению средств в консоль.
 * */
public class CreditAccountViewChain extends TransactionViewChain {

  public CreditAccountViewChain(AccountDto accountDto, UserDto userDto) {
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
        .type(TransactionType.CREDIT)
        .userId(getUserDto().getId())
        .accountId(getAccountDto().getId())
        .value(answer)
        .build();

    try {
      getTransactionHandlerFactory().getHandler(TransactionType.CREDIT).handle(transactionDto);
    } catch (EntityDuplicateException | ArgumentValueException e) {
      System.out.println(e.getMessage());
      setNextViewChain(this);
    }

    Long accountId = getAccountDto().getId();

    AccountDto accountById = getAccountService().findAccountById(accountId);
    setNextViewChain(new SpecificAccountViewChain(accountById, getUserDto()));
  }
}
