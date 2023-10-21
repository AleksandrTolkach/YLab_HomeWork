package by.toukach.walletservice.utils.param;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс отражающий параметры приложения в конфигурационном файле.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WalletServiceParam {
  private DataBaseParam database;
}
