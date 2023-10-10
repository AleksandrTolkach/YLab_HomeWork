package by.toukach.walletservice.infrastructure.repositories.entity.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.domain.models.AccountDto;
import by.toukach.walletservice.infrastructure.entity.AccountEntity;
import by.toukach.walletservice.infrastructure.entity.converter.impl.AccountConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountConverterTest extends BaseTest {

  @InjectMocks
  private AccountConverter accountConverter;
  private AccountEntity entity;
  private AccountDto dto;

  @BeforeEach
  public void setUp() {
    entity = getCreatedAccountEntity();
    dto = getCreatedAccountDto();
  }

  @Test
  public void toEntityTest_should_ReturnEntity() {
    AccountEntity expectedResult = entity;
    AccountEntity actualResult = accountConverter.toEntity(dto);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void toDtoTest_should_ReturnDto() {
    AccountDto expectedResult = dto;
    AccountDto actualResult = accountConverter.toDto(entity);

    assertEquals(expectedResult, actualResult);
  }
}
