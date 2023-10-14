package by.toukach.walletservice.controller.views;

import by.toukach.walletservice.service.handler.TransactionHandlerFactory;
import by.toukach.walletservice.service.handler.impl.TransactionHandlerFactoryImpl;
import lombok.Getter;

/**
 * Класс для вывода формы с данными о возможных операциях в консоль.
 * */
@Getter
public abstract class TransactionViewChain extends AccountViewChain {

  private final TransactionHandlerFactory transactionHandlerFactory
      = TransactionHandlerFactoryImpl.getInstance();
}
