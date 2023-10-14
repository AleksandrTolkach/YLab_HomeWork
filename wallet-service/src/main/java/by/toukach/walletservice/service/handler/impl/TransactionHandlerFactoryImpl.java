package by.toukach.walletservice.service.handler.impl;

import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.service.handler.TransactionHandler;
import by.toukach.walletservice.service.handler.TransactionHandlerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс представляет фабрику, ответственную за создание TransactionHandler.
 * */
public class TransactionHandlerFactoryImpl implements TransactionHandlerFactory {

  private static final TransactionHandlerFactory instance = new TransactionHandlerFactoryImpl();

  private final Map<TransactionType, TransactionHandler> transactionHandlerMap = new HashMap<>();

  private TransactionHandlerFactoryImpl() {
    TransactionHandler creditTransactionHandler = new CreditTransactionHandler();
    TransactionHandler debitTransactionHandler = new DebitTransactionHandler();

    transactionHandlerMap.put(creditTransactionHandler.type(), creditTransactionHandler);
    transactionHandlerMap.put(debitTransactionHandler.type(), debitTransactionHandler);
  }

  @Override
  public TransactionHandler getHandler(TransactionType type) {
    return transactionHandlerMap.get(type);
  }

  public static TransactionHandlerFactory getInstance() {
    return instance;
  }
}
