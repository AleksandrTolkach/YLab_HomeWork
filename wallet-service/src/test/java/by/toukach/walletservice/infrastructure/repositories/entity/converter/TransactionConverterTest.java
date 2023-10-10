package by.toukach.walletservice.infrastructure.repositories.entity.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.domain.models.TransactionDto;
import by.toukach.walletservice.infrastructure.entity.TransactionEntity;
import by.toukach.walletservice.infrastructure.entity.converter.impl.TransactionConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionConverterTest extends BaseTest {

  @InjectMocks
  private TransactionConverter transactionConverter;
  private TransactionEntity entity;
  private TransactionDto dto;

  @BeforeEach
  public void setUp() {
    entity = getTransactionEntity();
    dto = getTransactionDto();
  }

  @Test
  public void toEntityTest_should_ReturnEntity() {
    TransactionEntity expectedResult = entity;
    TransactionEntity actualResult = transactionConverter.toEntity(dto);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void toDtoTest_should_ReturnDto() {
    TransactionDto expectedDto = dto;
    TransactionDto actualResult = transactionConverter.toDto(entity);

    assertEquals(expectedDto, actualResult);
  }
}
