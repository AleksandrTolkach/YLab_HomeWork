package by.toukach.walletservice.application.views;

import by.toukach.walletservice.domain.models.TransactionDto;
import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.domain.services.TransactionService;
import by.toukach.walletservice.domain.services.impl.TransactionServiceImpl;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс для вывода формы с историей операций пользователя в консоль.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class HistoryViewChain extends AccountViewChain {

  private final TransactionService transactionService = TransactionServiceImpl.getInstance();

  public HistoryViewChain(UserDto userDto) {
    setUserDto(userDto);
  }

  @Override
  public void handle() {
    List<TransactionDto> transactionList = transactionService.findTransactionByUserId(
        getUserDto().getId());
    System.out.printf(ViewMessage.TABLE_HEADER);
    transactionList.forEach(t -> System.out.printf(ViewMessage.TABLE_ENTRY,
        t.getId(),
        getAccountService().findAccountById(t.getAccountId()).getTitle(),
        t.getType().getValue(),
        t.getValue()));

    setNextViewChain(new AccountActionViewChain(getUserDto()));
  }
}
