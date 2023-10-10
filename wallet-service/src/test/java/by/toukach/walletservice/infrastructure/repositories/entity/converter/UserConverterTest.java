package by.toukach.walletservice.infrastructure.repositories.entity.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.infrastructure.entity.UserEntity;
import by.toukach.walletservice.infrastructure.entity.converter.impl.UserConverter;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserConverterTest extends BaseTest {

  @InjectMocks
  private UserConverter userConverter;
  private UserEntity entity;
  private UserDto dto;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    entity = getCreatedUserEntity();
    dto = getCreatedUserDto();
  }

  @Test
  public void toEntityTest_should_ReturnEntity() {
    UserEntity expectedResult = entity;
    UserEntity actualResult = userConverter.toEntity(dto);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void toDtoTest_should_ReturnDto() {
    UserDto expectedResult = dto;
    UserDto actualResult = userConverter.toDto(entity);

    assertEquals(expectedResult, actualResult);
  }
}
