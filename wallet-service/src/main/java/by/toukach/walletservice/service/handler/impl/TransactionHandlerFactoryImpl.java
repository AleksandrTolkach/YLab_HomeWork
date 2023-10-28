package by.toukach.walletservice.service.handler.impl;

import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.service.handler.TransactionHandler;
import by.toukach.walletservice.service.handler.TransactionHandlerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Класс представляет фабрику, ответственную за создание TransactionHandler.
 * */
@Service
public class TransactionHandlerFactoryImpl implements TransactionHandlerFactory {

  private final Map<TransactionType, TransactionHandler> transactionHandlerMap;

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
