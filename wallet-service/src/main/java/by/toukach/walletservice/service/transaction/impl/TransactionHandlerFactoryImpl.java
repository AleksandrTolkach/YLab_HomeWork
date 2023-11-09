package by.toukach.walletservice.service.transaction.impl;

import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.service.transaction.TransactionHandler;
import by.toukach.walletservice.service.transaction.TransactionHandlerFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Класс представляет фабрику, ответственную за создание TransactionHandler.
 * */
@Service
public class TransactionHandlerFactoryImpl implements TransactionHandlerFactory {

  private final Map<TransactionType, TransactionHandler> transactionHandlerMap;

  /**
   * Конструктор для создания TransactionHandlerFactoryImpl.
   *
   * @param applicationContext текущий ApplicationContext.
   * @param transactionHandlerList список всех обработчиков транзакций.
   */
  public TransactionHandlerFactoryImpl(ApplicationContext applicationContext,
      List<TransactionHandler> transactionHandlerList) {
    transactionHandlerMap = transactionHandlerList.stream()
        .collect(Collectors.toMap(TransactionHandler::type,
            handler -> applicationContext.getBean(handler.getClass())));
  }

  @Override
  public TransactionHandler getHandler(TransactionType type) {
    return transactionHandlerMap.get(type);
  }
}
