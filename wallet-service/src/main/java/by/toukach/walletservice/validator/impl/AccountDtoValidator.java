package by.toukach.walletservice.validator.impl;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.exception.ValidationError;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.utils.SecurityUtil;
import by.toukach.walletservice.validator.ValidationMessages;
import by.toukach.walletservice.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Класс для валидации AccountDto.
 */
@Component
public class AccountDtoValidator implements Validator<AccountDto> {

  private static final String TITLE = "title";
  private static final String USER_ID = "userId";
  private static final String ACCOUNT = "account";

  @Override
  public void validate(AccountDto accountDto, String... params) {
    ValidationExceptionList validationExceptionList = new ValidationExceptionList();

    if (accountDto == null) {
      validationExceptionList.addError(
          new ValidationError(ACCOUNT, String.format(ValidationMessages.ENTITY_NULL, ACCOUNT)));
      throw validationExceptionList;
    }

    if (StringUtils.isBlank(accountDto.getTitle())) {
      validationExceptionList.addError(new ValidationError(TITLE, ValidationMessages.FIELD_NULL));
    }

    Long userId = SecurityUtil.getUserDetails().getId();

    if (accountDto.getUserId() == null) {
      validationExceptionList.addError(new ValidationError(USER_ID, ValidationMessages.FIELD_NULL));
    } else if (!accountDto.getUserId().equals(userId)) {
      validationExceptionList.addError(new ValidationError(USER_ID,
          ValidationMessages.WRONG_USER_ID));
    }

    if (!validationExceptionList.isEmpty()) {
      throw validationExceptionList;
    }
  }
}
