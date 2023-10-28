package by.toukach.walletservice.controller.in.listener;

import by.toukach.walletservice.repository.Migration;
import by.toukach.walletservice.repository.impl.DbInitializerImpl;
import by.toukach.walletservice.repository.impl.MigrationImpl;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Listener для выполнения миграции БД во время старта приложения.
 */
@WebListener
public class StartupListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    DbInitializerImpl.getInstance();
    Migration migration = MigrationImpl.getInstance();
    migration.migrate();
  }
}
