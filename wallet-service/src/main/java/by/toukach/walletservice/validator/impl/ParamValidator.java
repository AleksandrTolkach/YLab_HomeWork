package by.toukach.walletservice.validator.impl;

import by.toukach.walletservice.exception.ValidationError;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.validator.ValidationMessages;
import by.toukach.walletservice.validator.Validator;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Класс для валидации параметров запроса.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParamValidator implements Validator<String> {

  private static final Validator<String> instance = new ParamValidator();

  @Override
  public void validate(String param, String... types) {
    ValidationExceptionList validationExceptionList = new ValidationExceptionList();
    Type type = Type.valueOf(types[0]);
    String field = types[1];

    if (StringUtils.isBlank(param)) {
      validationExceptionList.addError(new ValidationError(field, ValidationMessages.FIELD_NULL));
      throw validationExceptionList;
    }

    switch (type) {
      case ID -> {
        try {
          Long.parseLong(param);
        } catch (Exception e) {
          validationExceptionList.addError(
              new ValidationError(field, ValidationMessages.WRONG_TYPE));
        }
      }
      case MONEY -> {
        try {
          new BigDecimal(param);
        } catch (Exception e) {
          validationExceptionList.addError(
              new ValidationError(field, ValidationMessages.WRONG_TYPE));
        }
      }
      default -> {
        return;
      }
    }

    if (!validationExceptionList.isEmpty()) {
      throw validationExceptionList;
    }
  }

  public static Validator<String> getInstance() {
    return instance;
  }

  /**
   * Перечисление типов параметров.
   */
  public enum Type {
    ID, MONEY, STRING;
  }
}
