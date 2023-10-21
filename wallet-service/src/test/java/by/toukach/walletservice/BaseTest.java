package by.toukach.walletservice;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.entity.Transaction;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.enumiration.TransactionType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.testcontainers.containers.PostgreSQLContainer;

public class BaseTest {

  protected static final long FIRST_ENTITY_ID = 1L;
  protected static final long SECOND_ENTITY_ID = 2L;
  protected static final long USER_ID = 2L;
  protected static final String LOGIN = "user";
  protected static final String SECOND_LOGIN = "user2";
  protected static final String PASSWORD = "password";
  protected static final String INCORRECT_PASSWORD = "incorrectPassword";
  protected static final long ADMIN_ID = 1L;
  protected static final String ADMIN_LOGIN = "admin";
  protected static final String ADMIN_PASSWORD = "admin";
  protected static final long UN_EXISTING_ID = 99L;
  protected static final String UN_EXISTING_LOGIN = "unExistingLogin";
  protected static final long ACCOUNT_ID = 1L;
  protected static final String ACCOUNT_TITLE = "account";
  protected static final double ACCOUNT_SUM = 10D;
  protected static final double UPDATED_ACCOUNT_SUM = 10D;
  protected static final long TRANSACTION_ID = 1L;
  protected static final double TRANSACTION_VALUE = 5D;
  protected static final double UPDATED_VALUE = ACCOUNT_SUM + TRANSACTION_VALUE;
  protected static final String LOG_VALUE = "log";
  protected static final LocalDateTime CREATED_AT =
      LocalDateTime.parse("1970-10-10T10:10:10").withNano(0);
  protected static final Long LOG_ID = 2L;
  protected static final Double VALUE_BIG_AMOUNT = 100000D;
  protected static final String DB_IMAGE_VERSION = "postgres:14.2-alpine";
  protected static final String DB_NAME = "wallet";
  protected static final String DB_USERNAME = "toukach";
  protected static final String DB_PASSWORD = "ylab";
  protected static final String DB_SCRIPT_PATH = "db/create-scheme.sql";
  protected static final String TAG_V_0_0 = "v0.0";

  protected User getNewUser() {
    return User.builder()
        .createdAt(CREATED_AT)
        .login(LOGIN)
        .password(PASSWORD)
        .accountList(new ArrayList<>())
        .build();
  }

  protected User getCreatedUser() {
    User user = getNewUser();
    user.setId(USER_ID);
    user.setCreatedAt(CREATED_AT);
    return user;
  }

  protected User getUpdatedUser() {
    User user = getCreatedUser();
    user.setAccountList(List.of(getCreatedAccount()));
    return user;
  }

  protected User getAdmin() {
    return User.builder()
        .id(ADMIN_ID)
        .createdAt(CREATED_AT)
        .login(ADMIN_LOGIN)
        .password(ADMIN_PASSWORD)
        .accountList(new ArrayList<>())
        .build();
  }

  protected UserDto getNewUserDto() {
    return UserDto.builder()
        .login(LOGIN)
        .password(PASSWORD)
        .accountList(new ArrayList<>())
        .build();
  }

  protected UserDto getSecondNewUserDto() {
    return UserDto.builder()
        .login(SECOND_LOGIN)
        .password(PASSWORD)
        .accountList(new ArrayList<>())
        .build();
  }

  protected UserDto getCreatedUserDto() {
    UserDto user = getNewUserDto();
    user.setId(USER_ID);
    user.setCreatedAt(CREATED_AT);
    return user;
  }

  protected UserDto getUpdatedUserDto() {
    UserDto userDto = getSecondNewUserDto();
    userDto.setId(USER_ID);
    userDto.setCreatedAt(CREATED_AT);
    AccountDto account = getCreatedAccountDto();
    account.setSum(UPDATED_ACCOUNT_SUM);
    userDto.setAccountList(List.of(account));
    return userDto;
  }

  protected Account getNewAccount() {
    return Account.builder()
        .createdAt(CREATED_AT)
        .title(ACCOUNT_TITLE)
        .sum(ACCOUNT_SUM)
        .userId(USER_ID)
        .build();
  }

  protected Account getCreatedAccount() {
    Account account = getNewAccount();
    account.setId(ACCOUNT_ID);
    account.setCreatedAt(CREATED_AT);
    return account;
  }

  protected Account getUpdatedAccount() {
    Account account = getCreatedAccount();
    account.setSum(UPDATED_VALUE);
    account.setCreatedAt(CREATED_AT);
    return account;
  }

  protected Account getUnExistingAccount() {
    Account account = getCreatedAccount();
    account.setId(UN_EXISTING_ID);

    return account;
  }

  protected List<Account> getAccountList() {
    return List.of(getCreatedAccount());
  }

  protected AccountDto getNewAccountDto() {
    return AccountDto.builder()
        .title(ACCOUNT_TITLE)
        .sum(ACCOUNT_SUM)
        .userId(USER_ID)
        .build();
  }

  protected AccountDto getCreatedAccountDto() {
    AccountDto account = getNewAccountDto();
    account.setId(ACCOUNT_ID);
    account.setCreatedAt(CREATED_AT);
    return account;
  }

  protected AccountDto getUpdatedAccountDto() {
    AccountDto account = getCreatedAccountDto();
    account.setSum(ACCOUNT_SUM + TRANSACTION_VALUE);
    account.setCreatedAt(CREATED_AT);
    return account;
  }

  protected List<AccountDto> getAccountDtoList() {
    return List.of(getCreatedAccountDto());
  }

  protected Log getNewLog() {
    return Log.builder()
        .type(LogType.TRANSACTION)
        .value(LOG_VALUE)
        .createdAt(CREATED_AT)
        .build();
  }

  protected Log getCreatedLog() {
    Log log = getNewLog();
    log.setId(LOG_ID);
    return log;
  }

  protected LogInDto getLogIn() {
    return LogInDto.builder()
        .login(LOGIN)
        .password(PASSWORD)
        .build();
  }

  protected SignUpDto getSignUp() {
    return SignUpDto.builder()
        .login(LOGIN)
        .password(PASSWORD)
        .build();
  }

  protected TransactionDto getTransactionDto() {
    return TransactionDto.builder()
        .id(TRANSACTION_ID)
        .createdAt(CREATED_AT)
        .type(TransactionType.CREDIT)
        .userId(USER_ID)
        .accountId(ACCOUNT_ID)
        .value(TRANSACTION_VALUE)
        .build();
  }

  protected Transaction getTransaction() {
    return Transaction.builder()
        .id(TRANSACTION_ID)
        .createdAt(CREATED_AT)
        .type(TransactionType.CREDIT)
        .userId(USER_ID)
        .accountId(ACCOUNT_ID)
        .value(TRANSACTION_VALUE)
        .build();
  }
}
