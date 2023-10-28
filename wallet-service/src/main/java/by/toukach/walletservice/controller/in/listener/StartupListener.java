package by.toukach.walletservice.controller.in.listener;

import by.toukach.walletservice.repository.DbInitializer;
import by.toukach.walletservice.repository.Migration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener для выполнения миграции БД во время старта приложения.
 */
@Component
@RequiredArgsConstructor
public class StartupListener {

  private final DbInitializer dbInitializer;
  private final Migration migration;

  @EventListener(ContextRefreshedEvent.class)
  public void handle() {
    dbInitializer.prepareDb();
    migration.migrate();
  }
}
