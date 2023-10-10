package by.toukach.walletservice.domain.services.impl;

import by.toukach.walletservice.domain.models.TransactionDto;
import by.toukach.walletservice.domain.services.TransactionService;
import by.toukach.walletservice.domain.services.UserService;
import by.toukach.walletservice.exceptions.EntityDuplicateException;
import by.toukach.walletservice.exceptions.EntityNotFoundException;
import by.toukach.walletservice.exceptions.ExceptionMessage;
import by.toukach.walletservice.infrastructure.entity.TransactionEntity;
import by.toukach.walletservice.infrastructure.entity.converter.Converter;
import by.toukach.walletservice.infrastructure.entity.converter.impl.TransactionConverter;
import by.toukach.walletservice.infrastructure.repositories.TransactionRepository;
import by.toukach.walletservice.infrastructure.repositories.impl.TransactionRepositoryImpl;
import java.util.List;

/**
 * Класс для выполнения операций с Transaction.
 * */
public class TransactionServiceImpl implements TransactionService {

  private static final TransactionService instance = new TransactionServiceImpl();

  private final TransactionRepository transactionRepository;
  private final Converter<TransactionEntity, TransactionDto> transactionConverter;
  private final UserService userService;

  private TransactionServiceImpl() {
    transactionRepository = TransactionRepositoryImpl.getInstance();
    transactionConverter = TransactionConverter.getInstance();
    userService = UserServiceImpl.getInstance();
  }

  @Override
  public TransactionDto createTransaction(TransactionDto transaction) {
    Long id = transaction.getId();
    if (transactionRepository.isExists(id)) {
      throw new EntityDuplicateException(
          String.format(ExceptionMessage.TRANSACTION_DUPLICATE, id));
    }
    transactionRepository.createTransaction(transactionConverter.toEntity(transaction));
    return transaction;
  }

  @Override
  public TransactionDto findTransactionById(Long id) {
    TransactionEntity transactionEntity = transactionRepository.findTransactionById(id);
    if (transactionEntity == null) {
      throw new EntityNotFoundException(
          String.format(ExceptionMessage.TRANSACTION_NOT_FOUND, id));
    }
    return transactionConverter.toDto(transactionEntity);
  }

  @Override
  public List<TransactionDto> findTransactionByUserId(Long userId) {
    if (!userService.isExists(userId)) {
      throw new EntityNotFoundException(
          String.format(ExceptionMessage.USER_BY_ID_NOT_FOUND, userId));
    }

    return transactionRepository.findTransactionByUserId(userId)
        .stream()
        .map(transactionConverter::toDto)
        .toList();
  }

  public static TransactionService getInstance() {
    return instance;
  }
}
