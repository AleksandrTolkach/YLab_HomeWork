package by.toukach.walletservice;

import by.toukach.speed.annotation.EnableMethodSpeedMeasurement;
import by.toukach.walletservice.utils.DbInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс, представляющий точку входа для приложения Wallet-Service.
 */
@SpringBootApplication
@EnableMethodSpeedMeasurement
@RequiredArgsConstructor
public class WalletServiceApplication {

  /**
   * Точка входа для приложения Wallet-Service.
   *
   * @param args аргументы.
   */
  public static void main(String[] args) {

    DbInitializer.prepareDb();

    SpringApplication.run(WalletServiceApplication.class, args);
  }
}
