package by.toukach.walletservice.service.impl;

import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.entity.Transaction;
import by.toukach.walletservice.entity.converter.Converter;
import by.toukach.walletservice.entity.converter.impl.TransactionConverter;
import by.toukach.walletservice.exceptions.EntityDuplicateException;
import by.toukach.walletservice.exceptions.EntityNotFoundException;
import by.toukach.walletservice.exceptions.ExceptionMessage;
import by.toukach.walletservice.repository.TransactionRepository;
import by.toukach.walletservice.repository.impl.TransactionRepositoryImpl;
import by.toukach.walletservice.service.TransactionService;
import by.toukach.walletservice.service.UserService;
import java.util.List;

/**
 * Класс для выполнения операций с Transaction.
 * */
public class TransactionServiceImpl implements TransactionService {

  private static final TransactionService instance = new TransactionServiceImpl();

  private final TransactionRepository transactionRepository;
  private final Converter<Transaction, TransactionDto> transactionConverter;
  private final UserService userService;

  private TransactionServiceImpl() {
    transactionRepository = TransactionRepositoryImpl.getInstance();
    transactionConverter = TransactionConverter.getInstance();
    userService = UserServiceImpl.getInstance();
  }

  @Override
  public TransactionDto createTransaction(TransactionDto transactionDto) {
    Long id = transactionDto.getId();
    if (transactionRepository.isExists(id)) {
      throw new EntityDuplicateException(
          String.format(ExceptionMessage.TRANSACTION_DUPLICATE, id));
    }
    transactionRepository.createTransaction(transactionConverter.toEntity(transactionDto));
    return transactionDto;
  }

  @Override
  public TransactionDto findTransactionById(Long id) {
    Transaction transaction = transactionRepository.findTransactionById(id);
    if (transaction == null) {
      throw new EntityNotFoundException(
          String.format(ExceptionMessage.TRANSACTION_NOT_FOUND, id));
    }
    return transactionConverter.toDto(transaction);
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
