package by.toukach.walletservice.validator.impl;

import by.toukach.walletservice.dto.CreateAccountDto;
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
public class CreateAccountDtoValidator implements Validator<CreateAccountDto> {

  private static final String TITLE = "title";
  private static final String USER_ID = "userId";
  private static final String ACCOUNT = "account";

  @Override
  public void validate(CreateAccountDto createAccountDto, String... params) {
    ValidationExceptionList validationExceptionList = new ValidationExceptionList();

    if (createAccountDto == null) {
      validationExceptionList.addError(
          new ValidationError(ACCOUNT, String.format(ValidationMessages.ENTITY_NULL, ACCOUNT)));
      throw validationExceptionList;
    }

    if (StringUtils.isBlank(createAccountDto.getTitle())) {
      validationExceptionList.addError(new ValidationError(TITLE, ValidationMessages.FIELD_NULL));
    }

    Long userId = SecurityUtil.getUserDetails().getId();

    if (createAccountDto.getUserId() == null) {
      validationExceptionList.addError(new ValidationError(USER_ID, ValidationMessages.FIELD_NULL));
    } else if (!createAccountDto.getUserId().equals(userId)) {
      validationExceptionList.addError(new ValidationError(USER_ID,
          ValidationMessages.WRONG_USER_ID));
    }

    if (!validationExceptionList.isEmpty()) {
      throw validationExceptionList;
    }
  }
}
