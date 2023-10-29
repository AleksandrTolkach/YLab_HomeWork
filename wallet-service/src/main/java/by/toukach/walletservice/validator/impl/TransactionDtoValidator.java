package by.toukach.walletservice.validator.impl;

import by.toukach.walletservice.dto.TransactionDto;
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
public class TransactionDtoValidator implements Validator<TransactionDto> {

  private static final String TRANSACTION = "transaction";
  private static final String TYPE = "type";
  private static final String USER_ID = "userId";
  private static final String ACCOUNT_ID = "accountId";
  private static final String VALUE = "value";

  @Override
  public void validate(TransactionDto transactionDto, String... params) {
    ValidationExceptionList validationExceptionList = new ValidationExceptionList();

    if (transactionDto == null) {
      validationExceptionList.addError(
          new ValidationError(TRANSACTION, String.format(ValidationMessages.ENTITY_NULL,
              TRANSACTION)));
      throw validationExceptionList;
    }

    if (transactionDto.getType() == null) {
      validationExceptionList.addError(new ValidationError(TYPE, ValidationMessages.FIELD_NULL));
    } else if (StringUtils.isBlank(transactionDto.getType().name())) {
      validationExceptionList.addError(new ValidationError(TYPE,
          ValidationMessages.UNKNOWN_TRANSACTION));
    } else if (transactionDto.getType().equals(TransactionType.UNKNOWN)) {
      validationExceptionList.addError(new ValidationError(TYPE,
          ValidationMessages.UNKNOWN_TRANSACTION));
    }

    Long userId = transactionDto.getUserId();
    if (userId == null) {
      validationExceptionList.addError(new ValidationError(USER_ID, ValidationMessages.FIELD_NULL));
    } else if (!userId.equals(SecurityUtil.getUserDetails().getId())) {
      validationExceptionList.addError(new ValidationError(USER_ID,
          ValidationMessages.WRONG_USER_ID));
    }

    if (transactionDto.getAccountId() == null) {
      validationExceptionList.addError(new ValidationError(ACCOUNT_ID,
          ValidationMessages.FIELD_NULL));
    }

    if (transactionDto.getValue() == null) {
      validationExceptionList.addError(new ValidationError(VALUE, ValidationMessages.FIELD_NULL));
    } else if (transactionDto.getValue().compareTo(BigDecimal.ZERO) < 0) {
      validationExceptionList.addError(new ValidationError(VALUE,
          ValidationMessages.NEGATIVE_ARGUMENT));
    }

    if (!validationExceptionList.isEmpty()) {
      throw validationExceptionList;
    }
  }
}
