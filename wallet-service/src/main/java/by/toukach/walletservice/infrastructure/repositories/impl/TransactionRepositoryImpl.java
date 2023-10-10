package by.toukach.walletservice.infrastructure.repositories.impl;

import by.toukach.walletservice.infrastructure.entity.TransactionEntity;
import by.toukach.walletservice.infrastructure.repositories.TransactionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс для выполнения запросов, связанных с операциями, в память.
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionRepositoryImpl implements TransactionRepository {

  private static final TransactionRepository instance = new TransactionRepositoryImpl();

  private final List<TransactionEntity> transactionList = new ArrayList<>();

  @Override
  public TransactionEntity createTransaction(TransactionEntity transaction) {
    transactionList.add(transaction);
    return transaction;
  }

  @Override
  public TransactionEntity findTransactionById(Long id) {
    return transactionList.stream()
        .filter(t -> t.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  @Override
  public List<TransactionEntity> findTransactionByUserId(Long userId) {
    return transactionList.stream()
        .filter(t -> t.getUserId().equals(userId))
        .toList();
  }

  @Override
  public boolean isExists(Long id) {
    return findTransactionById(id) != null;
  }

  public static TransactionRepository getInstance() {
    return instance;
  }
}
