package by.toukach.walletservice.entity.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.entity.Transaction;
import by.toukach.walletservice.entity.converter.impl.TransactionConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionConverterTest extends BaseTest {

  @InjectMocks
  private TransactionConverter transactionConverter;
  private Transaction entity;
  private TransactionDto dto;

  @BeforeEach
  public void setUp() {
    entity = getTransactionEntity();
    dto = getTransactionDto();
  }

  @Test
  @DisplayName("Тест конвертации TransactionDto в TransactionEntity")
  public void toEntityTest_should_ReturnEntity() {
    Transaction expectedResult = entity;
    Transaction actualResult = transactionConverter.toEntity(dto);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест конвертации Transaction в TransactionDto")
  public void toDtoTest_should_ReturnDto() {
    TransactionDto expectedDto = dto;
    TransactionDto actualResult = transactionConverter.toDto(entity);

    assertEquals(expectedDto, actualResult);
  }
}
