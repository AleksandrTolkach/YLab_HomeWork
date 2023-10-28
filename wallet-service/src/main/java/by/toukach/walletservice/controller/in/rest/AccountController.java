package by.toukach.walletservice.controller.in.rest;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.service.AccountService;
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
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @GetMapping("/id/{id}")
  @ResponseStatus(HttpStatus.OK)
  public AccountDto findAccount(@PathVariable Long id) {
    return accountService.findAccountById(id);
  }

  @GetMapping("/user/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public List<AccountDto> findAccountsByUserId(@PathVariable Long userId) {
    return accountService.findAccountsByUserId(userId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AccountDto createAccount(@RequestBody AccountDto accountDto) {
    return accountService.createAccount(accountDto);
  }
}
