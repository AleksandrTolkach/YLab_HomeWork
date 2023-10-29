package by.toukach.walletservice.controller.in.rest;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.security.UserDetailsImpl;
import by.toukach.walletservice.service.AccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с запросами пользователя. связанными со счетом.
 */
@RestController
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  /**
   * Метод для обработки запроса поиска счета по ID.
   *
   * @param id ID запрашиваемого счета.
   * @return запрашиваемый счет.
   */
  @GetMapping(value = "/id/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('USER')")
  @PostAuthorize("!returnObject.userId.equals(authentication.principal.id)")
  public AccountDto findAccount(@PathVariable(required = false) Long id) {
    return accountService.findAccountById(id);
  }

  /**
   * Метод для обработки запроса по получению списка счетов текущего пользователя.
   *
   * @param userDetailsImpl данные о пользователе.
   * @return запрашиваеммый список.
   */
  @GetMapping("/user")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('USER')")
  public List<AccountDto> findAccountsByUserId(
      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
    Long id = userDetailsImpl.getId();
    return accountService.findAccountsByUserId(id);
  }

  /**
   * Метод для обработки запросов по созданию счета.
   *
   * @param accountDto создаваемый счет.
   * @return созданный счет.
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('USER')")
  public AccountDto createAccount(@RequestBody(required = false) AccountDto accountDto) {
    return accountService.createAccount(accountDto);
  }
}
