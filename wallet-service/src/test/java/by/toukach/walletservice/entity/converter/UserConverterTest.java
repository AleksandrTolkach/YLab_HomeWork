package by.toukach.walletservice.entity.converter;

import static org.assertj.core.api.Assertions.assertThat;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.entity.converter.impl.UserConverter;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserConverterTest extends BaseTest {

  @InjectMocks
  private UserConverter userConverter;
  private User entity;
  private UserDto dto;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    entity = getCreatedUser();
    dto = getCreatedUserDto();
  }

  @Test
  @DisplayName("Тест конвертации UserDto в User")
  public void toEntityTest_should_ReturnEntity() {
    User expectedResult = entity;
    User actualResult = userConverter.toEntity(dto);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест конвертации UserDto в User")
  public void toDtoTest_should_ReturnDto() {
    UserDto expectedResult = dto;
    UserDto actualResult = userConverter.toDto(entity);

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
