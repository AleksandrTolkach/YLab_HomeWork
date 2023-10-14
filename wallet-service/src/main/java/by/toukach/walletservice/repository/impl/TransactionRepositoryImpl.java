package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.entity.Transaction;
import by.toukach.walletservice.repository.TransactionRepository;
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

  private final List<Transaction> transactionList = new ArrayList<>();

  @Override
  public Transaction createTransaction(Transaction transaction) {
    transactionList.add(transaction);
    return transaction;
  }

  @Override
  public Transaction findTransactionById(Long id) {
    return transactionList.stream()
        .filter(t -> t.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  @Override
  public List<Transaction> findTransactionByUserId(Long userId) {
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
