package by.toukach.walletservice.validator.impl;

import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.exception.ValidationError;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.validator.ValidationMessages;
import by.toukach.walletservice.validator.Validator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Класс для валидации LogInDto.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogInDtoValidator implements Validator<LogInDto> {

  private static final Validator<LogInDto> instance = new LogInDtoValidator();
  private static final String LOGIN = "login";
  private static final String PASSWORD = "password";

  @Override
  public void validate(LogInDto logInDto, String... params) {
    ValidationExceptionList validationExceptionList = new ValidationExceptionList();

    if (logInDto == null) {
      addLoginError(validationExceptionList);
      addPasswordError(validationExceptionList);
      throw validationExceptionList;
    }

    String login = logInDto.getLogin();
    String password = logInDto.getPassword();

    if (StringUtils.isBlank(login)) {
      addLoginError(validationExceptionList);
    }

    if (StringUtils.isBlank(password)) {
      addPasswordError(validationExceptionList);
    }

    if (!validationExceptionList.isEmpty()) {
      throw validationExceptionList;
    }
  }

  public static Validator<LogInDto> getInstance() {
    return instance;
  }

  private void addLoginError(ValidationExceptionList validationExceptionList) {
    validationExceptionList.addError(new ValidationError(LOGIN, ValidationMessages.FIELD_NULL));
  }

  private void addPasswordError(ValidationExceptionList validationExceptionList) {
    validationExceptionList.addError(new ValidationError(PASSWORD,
        ValidationMessages.FIELD_NULL));
  }
}
