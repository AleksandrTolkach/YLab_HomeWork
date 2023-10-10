package by.toukach.walletservice.application.views;

import by.toukach.walletservice.domain.models.AccountDto;
import by.toukach.walletservice.domain.models.TransactionDto;
import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exceptions.ArgumentValueException;
import by.toukach.walletservice.exceptions.EntityDuplicateException;
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

    System.out.println(ViewMessage.TRANSACTION_ID);
    long transactionId = scanner.nextLong();
    scanner.nextLine();

    TransactionDto transactionDto = TransactionDto.builder()
        .id(transactionId)
        .type(TransactionType.CREDIT)
        .userId(getUserDto().getId())
        .accountId(getAccountDto().getId())
        .value(answer)
        .build();

    try {
      getTransactionHandlerFactory().getHandler(TransactionType.CREDIT)
          .handle(transactionDto);
    } catch (EntityDuplicateException | ArgumentValueException e) {
      System.out.println(e.getMessage());
      setNextViewChain(this);
    }

    Long accountId = getAccountDto().getId();

    setNextViewChain(new SpecificAccountViewChain(getAccountService()
        .findAccountById(accountId), getUserDto()));
  }
}
