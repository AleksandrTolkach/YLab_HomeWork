package by.toukach.walletservice.service.impl;

import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.entity.Transaction;
import by.toukach.walletservice.entity.mapper.TransactionMapper;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.TransactionRepository;
import by.toukach.walletservice.repository.impl.TransactionRepositoryImpl;
import by.toukach.walletservice.service.TransactionService;
import by.toukach.walletservice.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Класс для выполнения операций с Transaction.
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;
  private final UserService userService;
  private final TransactionMapper transactionMapper;

  @Override
  public TransactionDto createTransaction(TransactionDto transactionDto) {
    transactionDto.setCreatedAt(LocalDateTime.now());

    Transaction transaction = transactionRepository.createTransaction(transactionMapper
        .transactionDtoToTransaction(transactionDto));

    return transactionMapper.transactionToTransactionDto(transaction);
  }

  @Override
  public TransactionDto findTransactionById(Long id) {
    Transaction transaction = transactionRepository.findTransactionById(id).orElseThrow(() ->
        new EntityNotFoundException(String.format(ExceptionMessage.TRANSACTION_NOT_FOUND, id)));

    return transactionMapper.transactionToTransactionDto(transaction);
  }

  @Override
  public List<TransactionDto> findTransactionByUserId(Long userId) {
    if (!userService.isExists(userId)) {
      throw new EntityNotFoundException(
          String.format(ExceptionMessage.USER_BY_ID_NOT_FOUND, userId));
    } else {
      return transactionRepository.findTransactionByUserId(userId)
          .stream()
          .map(transactionMapper::transactionToTransactionDto)
          .toList();
    }
  }
}
