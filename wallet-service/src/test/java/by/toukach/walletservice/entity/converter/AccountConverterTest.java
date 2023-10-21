package by.toukach.walletservice.entity.converter;

import static org.assertj.core.api.Assertions.assertThat;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.converter.impl.AccountConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountConverterTest extends BaseTest {

  @InjectMocks
  private AccountConverter accountConverter;
  private Account entity;
  private AccountDto dto;

  @BeforeEach
  public void setUp() {
    entity = getCreatedAccount();
    dto = getCreatedAccountDto();
  }

  @Test
  @DisplayName("Тест конвертации AccountDto в Account")
  public void toEntityTest_should_ReturnEntity() {
    Account expectedResult = entity;
    Account actualResult = accountConverter.toEntity(dto);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест конвертации Account в AccountDto")
  public void toDtoTest_should_ReturnDto() {
    AccountDto expectedResult = dto;
    AccountDto actualResult = accountConverter.toDto(entity);

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
