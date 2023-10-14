package by.toukach.walletservice.controller.views;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.service.AccountService;
import by.toukach.walletservice.service.UserService;
import by.toukach.walletservice.service.impl.AccountServiceImpl;
import by.toukach.walletservice.service.impl.UserServiceImpl;

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
