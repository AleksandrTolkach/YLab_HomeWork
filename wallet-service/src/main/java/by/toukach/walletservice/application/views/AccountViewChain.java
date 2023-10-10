package by.toukach.walletservice.application.views;

import by.toukach.walletservice.domain.models.AccountDto;
import by.toukach.walletservice.domain.services.AccountService;
import by.toukach.walletservice.domain.services.UserService;
import by.toukach.walletservice.domain.services.impl.AccountServiceImpl;
import by.toukach.walletservice.domain.services.impl.UserServiceImpl;

/**
 * Класс для вывода формы с данными о счете в консоль.
 * */
public abstract class AccountViewChain extends ViewChain {

  private final AccountService accountService = AccountServiceImpl.getInstance();
  private final UserService userService = UserServiceImpl.getInstance();
  private AccountDto accountDto;

  public AccountService getAccountService() {
    return accountService;
  }

  public UserService getUserService() {
    return userService;
  }

  public AccountDto getAccountDto() {
    return accountDto;
  }

  public void setAccountDto(AccountDto accountDto) {
    this.accountDto = accountDto;
  }
}
