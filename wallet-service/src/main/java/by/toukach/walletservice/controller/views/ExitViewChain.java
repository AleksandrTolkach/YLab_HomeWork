package by.toukach.walletservice.controller.views;

import by.toukach.walletservice.dto.UserDto;
import lombok.NoArgsConstructor;

/**
 * Класс для вывода формы ожидания в консоль.
 * */
@NoArgsConstructor
public class ExitViewChain extends ViewChain {

  public ExitViewChain(UserDto userDto) {
    setUserDto(userDto);
  }

  @Override
  public void handle() {
    System.out.println(ViewMessage.BYE);
  }
}
