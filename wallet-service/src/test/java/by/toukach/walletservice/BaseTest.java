package by.toukach.walletservice;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.LogDto;
import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.LogInResponseDto;
import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomDeserializer;
import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomSerializer;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.entity.Transaction;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.enumiration.UserRole;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.security.UserDetailsImpl;
import by.toukach.walletservice.utils.CookieUtil;
import by.toukach.walletservice.utils.JwtUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class BaseTest {

  protected static final long FIRST_ENTITY_ID = 1L;
  protected static final long SECOND_ENTITY_ID = 2L;
  protected static final long USER_ID = 2L;
  protected static final String LOGIN = "user";
  protected static final String PASSWORD = "password";
  protected static final String INCORRECT_PASSWORD = "incorrectPassword";
  protected static final long ADMIN_ID = 1L;
  protected static final String ADMIN_LOGIN = "admin";
  protected static final String ADMIN_PASSWORD
      = "$2a$10$6zpiUWPhVuv0h/G6JVgLM.NM.sAC.BpcQwTo6hy4Xc6kniYPZWeO2";
  protected static final long UN_EXISTING_ID = 99L;
  protected static final String UN_EXISTING_LOGIN = "unExistingLogin";
  protected static final long ACCOUNT_ID = 1L;
  protected static final String ACCOUNT_TITLE = "account";
  protected static final BigDecimal ACCOUNT_SUM = new BigDecimal("10.00");
  protected static final long TRANSACTION_ID = 1L;
  protected static final BigDecimal TRANSACTION_VALUE = new BigDecimal("5.00");
  protected static final BigDecimal NEGATIVE_TRANSACTION_VALUE = new BigDecimal("-5");
  protected static final BigDecimal UPDATED_VALUE = ACCOUNT_SUM.add(TRANSACTION_VALUE);
  protected static final String LOG_VALUE = "log";
  protected static final LocalDateTime CREATED_AT =
      LocalDateTime.parse("1970-10-10T10:10:10").withNano(0);
  protected static final Long LOG_ID = 2L;
  protected static final BigDecimal VALUE_BIG_AMOUNT = new BigDecimal("100000.0");
  protected static final String TAG_V_0_0 = "v0.0";
  protected static final String TOKEN = "token";
  protected static final String CONTENT_TYPE = "application/json; charset=UTF-8";
  protected static final String ID_PARAM = "id";
  protected static final String USER_ID_PARAM = "userId";
  protected static final String LOGIN_PARAM = "login";
  protected static final String ID_USER_ID_NOT_PROVIDED =
      String.format(ExceptionMessage.PARAMS_NOT_PROVIDED, ID_PARAM + "/" + USER_ID_PARAM);
  protected static final String ID_LOGIN_NOT_PROVIDED =
      String.format(ExceptionMessage.PARAMS_NOT_PROVIDED, ID_PARAM + "/" + LOGIN_PARAM);
  protected static final String ACCOUNT_URL = "/account";
  protected static final String ID_URL = "/id";
  protected static final String ACCOUNT_ID_URL = ACCOUNT_URL + ID_URL + "/%s";
  protected static final String USER_URL = "/user";
  protected static final String ACCOUNT_USER_URL = ACCOUNT_URL + USER_URL;
  protected static final String CREATE_ACCOUNT_FILE_PATH = "json/createAccount.txt";
  protected static final String LOG_IN_FILE_PATH = "json/logIn.txt";
  protected static final String SIGN_UP_FILE_PATH = "json/signUp.txt";
  protected static final String CREDIT_TRANSACTION_FILE_PATH = "json/creditTransaction.txt";
  protected static final String ADMIN_URL = "/admin";
  protected static final String LOGS_URL = "/logs";
  protected static final String ADMIN_LOGS_URL = ADMIN_URL + LOGS_URL;
  protected static final String AUTH_URL = "/auth";
  protected static final String LOG_IN_URL = "/login";
  protected static final String SIGN_UP_URL = "/sign-up";
  protected static final String AUTH_LOG_IN_URL = AUTH_URL + LOG_IN_URL;
  protected static final String AUTH_SING_UP_URL = AUTH_URL + SIGN_UP_URL;
  protected static final String TRANSACTION_URL = "/transaction";
  protected static final String TRANSACTION_USER = TRANSACTION_URL + USER_URL;
  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    SimpleModule serializeModule = new SimpleModule();
    serializeModule.addSerializer(LocalDateTime.class, new LocalDateTimeCustomSerializer());
    SimpleModule deSerializeModule = new SimpleModule();
    deSerializeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeCustomDeserializer());

    objectMapper.findAndRegisterModules();
    objectMapper.registerModule(serializeModule);
    objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE,
        true);
  }

  protected <T> T readValue(String value, TypeReference<T> type) throws Exception {
    return objectMapper.readValue(value, type);
  }

  protected String readJsonFile(String path) throws IOException {
    final File createRecipeFile = new ClassPathResource(path)
        .getFile();
    return Files.readString(createRecipeFile.toPath());
  }

  protected User getNewUser() {
    return User.builder()
        .createdAt(CREATED_AT)
        .login(LOGIN)
        .password(PASSWORD)
        .accountList(new ArrayList<>())
        .build();
  }

  protected User getNewUserWithRole() {
    User user = getNewUser();
    user.setRole(UserRole.USER);
    return user;
  }

  protected User getCreatedUser() {
    User user = getNewUserWithRole();
    user.setId(USER_ID);
    user.setCreatedAt(CREATED_AT);
    return user;
  }

  protected User getAdmin() {
    return User.builder()
        .id(ADMIN_ID)
        .createdAt(CREATED_AT)
        .login(ADMIN_LOGIN)
        .password(ADMIN_PASSWORD)
        .role(UserRole.ADMIN)
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

  protected UserDto getNewUserDtoWithRole() {
    UserDto user = getNewUserDto();
    user.setRole(UserRole.USER);
    return user;
  }

  protected UserDto getCreatedUserDto() {
    UserDto user = getNewUserDtoWithRole();
    user.setId(USER_ID);
    user.setCreatedAt(CREATED_AT);
    return user;
  }

  protected AccountDto getWrongAccountDtoFormat() {
    return AccountDto.builder()
        .build();
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
    account.setSum(ACCOUNT_SUM);
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
    account.setSum(ACCOUNT_SUM.add(TRANSACTION_VALUE));
    account.setCreatedAt(CREATED_AT);
    return account;
  }

  protected Log getNewLog() {
    return Log.builder()
        .type(LogType.DEBIT)
        .message(LOG_VALUE)
        .build();
  }

  protected Log getCreatedLog() {
    Log log = getNewLog();
    log.setId(LOG_ID);
    log.setCreatedAt(CREATED_AT);
    return log;
  }

  protected LogDto getNewLogDto() {
    return LogDto.builder()
        .type(LogType.DEBIT)
        .message(LOG_VALUE)
        .build();
  }

  protected LogDto getCreatedLogDto() {
    LogDto logDto = getNewLogDto();
    logDto.setId(LOG_ID);
    logDto.setCreatedAt(CREATED_AT);
    return logDto;
  }

  protected LogInDto getLogIn() {
    return LogInDto.builder()
        .login(LOGIN)
        .password(PASSWORD)
        .build();
  }

  protected LogInResponseDto getLogInDtoResponse() {
    String token = JwtUtil.generateTokenFromUsername(LOGIN);
    return LogInResponseDto.builder()
        .accessTokenCookieString(CookieUtil.generateAccessCookie(token).toString())
        .userDto(getCreatedUserDto())
        .build();
  }

  protected SignUpDto getSignUp() {
    return SignUpDto.builder()
        .login(LOGIN)
        .password(PASSWORD)
        .build();
  }

  protected TransactionDto getNewCreditTransactionDto() {
    return TransactionDto.builder()
        .type(TransactionType.CREDIT)
        .userId(USER_ID)
        .accountId(ACCOUNT_ID)
        .value(TRANSACTION_VALUE)
        .build();
  }

  protected TransactionDto getCreditTransactionDto() {
    return TransactionDto.builder()
        .id(TRANSACTION_ID)
        .createdAt(CREATED_AT)
        .type(TransactionType.CREDIT)
        .userId(USER_ID)
        .accountId(ACCOUNT_ID)
        .value(TRANSACTION_VALUE)
        .build();
  }

  protected TransactionDto getDebitTransactionDto() {
    TransactionDto transactionDto = getCreditTransactionDto();
    transactionDto.setType(TransactionType.DEBIT);
    return transactionDto;
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

  protected Authentication getAuthentication() {
    return new UsernamePasswordAuthenticationToken(LOGIN, PASSWORD);
  }

  protected UserDetails getUserDetails() {
    return new UserDetailsImpl(getCreatedUser());
  }
}
