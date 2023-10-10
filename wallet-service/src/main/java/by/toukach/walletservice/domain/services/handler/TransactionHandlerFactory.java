package by.toukach.walletservice.domain.services.handler;

import by.toukach.walletservice.enumiration.TransactionType;

/**
 * Интерфейс представляет фабрику, ответственную за создание TransactionHandler.
 * */
public interface TransactionHandlerFactory {

  /**
   * Метод создает TransactionHandler.
   *
   * @param type тип требуемого TransactionHandler.
   * @return запрашиваемый TransactionHandler.
   */
  TransactionHandler getHandler(TransactionType type);
}
