package by.toukach.walletservice.domain.services.handler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import by.toukach.walletservice.domain.services.handler.impl.CreditTransactionHandler;
import by.toukach.walletservice.domain.services.handler.impl.DebitTransactionHandler;
import by.toukach.walletservice.domain.services.handler.impl.TransactionHandlerFactoryImpl;
import by.toukach.walletservice.enumiration.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionHandlerFactoryTest {

  private TransactionHandlerFactory transactionHandlerFactory;

  @BeforeEach
  public void setUp() {
    transactionHandlerFactory = TransactionHandlerFactoryImpl.getInstance();
  }

  @Test
  public void getHandlerTest_should_ReturnHandler() {
    TransactionHandler credit = transactionHandlerFactory.getHandler(TransactionType.CREDIT);
    TransactionHandler debit = transactionHandlerFactory.getHandler(TransactionType.DEBIT);

    assertTrue(credit instanceof CreditTransactionHandler);
    assertTrue(debit instanceof DebitTransactionHandler);
  }

}
