package by.toukach.walletservice.validator.impl;

import by.toukach.walletservice.dto.CreateTransactionDto;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exception.ValidationError;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.utils.SecurityUtil;
import by.toukach.walletservice.validator.ValidationMessages;
import by.toukach.walletservice.validator.Validator;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Класс для валидации TransactionDto.
 */
@Repository
public class CreateTransactionDtoValidator implements Validator<CreateTransactionDto> {

  private static final String TRANSACTION = "transaction";
  private static final String TYPE = "type";
  private static final String USER_ID = "userId";
  private static final String ACCOUNT_ID = "accountId";
  private static final String VALUE = "value";

  @Override
  public void validate(CreateTransactionDto createTransactionDto, String... params) {
    ValidationExceptionList validationExceptionList = new ValidationExceptionList();

    if (createTransactionDto == null) {
      validationExceptionList.addError(
          new ValidationError(TRANSACTION, String.format(ValidationMessages.ENTITY_NULL,
              TRANSACTION)));
      throw validationExceptionList;
    }

    if (createTransactionDto.getType() == null) {
      validationExceptionList.addError(new ValidationError(TYPE, ValidationMessages.FIELD_NULL));
    } else if (StringUtils.isBlank(createTransactionDto.getType().name())) {
      validationExceptionList.addError(new ValidationError(TYPE,
          ValidationMessages.UNKNOWN_TRANSACTION));
    } else if (createTransactionDto.getType().equals(TransactionType.UNKNOWN)) {
      validationExceptionList.addError(new ValidationError(TYPE,
          ValidationMessages.UNKNOWN_TRANSACTION));
    }

    Long userId = createTransactionDto.getUserId();
    if (userId == null) {
      validationExceptionList.addError(new ValidationError(USER_ID, ValidationMessages.FIELD_NULL));
    } else if (!userId.equals(SecurityUtil.getUserDetails().getId())) {
      validationExceptionList.addError(new ValidationError(USER_ID,
          ValidationMessages.WRONG_USER_ID));
    }

    if (createTransactionDto.getAccountId() == null) {
      validationExceptionList.addError(new ValidationError(ACCOUNT_ID,
          ValidationMessages.FIELD_NULL));
    }

    if (createTransactionDto.getValue() == null) {
      validationExceptionList.addError(new ValidationError(VALUE, ValidationMessages.FIELD_NULL));
    } else if (createTransactionDto.getValue().compareTo(BigDecimal.ZERO) < 0) {
      validationExceptionList.addError(new ValidationError(VALUE,
          ValidationMessages.NEGATIVE_ARGUMENT));
    }

    if (!validationExceptionList.isEmpty()) {
      throw validationExceptionList;
    }
  }
}
