package by.toukach.walletservice.service.handler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.service.handler.impl.CreditTransactionHandler;
import by.toukach.walletservice.service.handler.impl.DebitTransactionHandler;
import by.toukach.walletservice.service.handler.impl.TransactionHandlerFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TransactionHandlerFactoryTest {

  private TransactionHandlerFactory transactionHandlerFactory;

  @BeforeEach
  public void setUp() {
    transactionHandlerFactory = TransactionHandlerFactoryImpl.getInstance();
  }

  @Test
  @DisplayName("Тест возвращения обработчика транзакций по типу транзакции")
  public void getHandlerTest_should_ReturnHandler() {
    TransactionHandler credit = transactionHandlerFactory.getHandler(TransactionType.CREDIT);
    TransactionHandler debit = transactionHandlerFactory.getHandler(TransactionType.DEBIT);

    assertTrue(credit instanceof CreditTransactionHandler);
    assertTrue(debit instanceof DebitTransactionHandler);
  }

}
