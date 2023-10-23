package by.toukach.walletservice.validator.impl;

import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.exception.ValidationError;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.validator.ValidationMessages;
import by.toukach.walletservice.validator.Validator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Класс для валидации SignUpDto.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpDtoValidator implements Validator<SignUpDto> {

  private static final Validator<SignUpDto> instance = new SignUpDtoValidator();
  private static final String LOGIN = "login";
  private static final String PASSWORD = "password";

  @Override
  public void validate(SignUpDto signUpDto, String... params) {
    ValidationExceptionList validationExceptionList = new ValidationExceptionList();

    if (signUpDto == null) {
      addLoginError(validationExceptionList);
      addPasswordError(validationExceptionList);
      throw validationExceptionList;
    }

    String login = signUpDto.getLogin();
    String password = signUpDto.getPassword();

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

  public static Validator<SignUpDto> getInstance() {
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
