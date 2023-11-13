package by.toukach.walletservice.mapper;

import by.toukach.walletservice.dto.CreateTransactionDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Интерфейс для преобразования Transaction в TransactionDto и обратно.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {

  TransactionMapper instance = Mappers.getMapper(TransactionMapper.class);

  /**
   * Метод для преобразования Transaction в TransactionDto.
   *
   * @param transaction Transaction для преобразования.
   * @return преобразованный TransactionDto.
   */
  TransactionDto transactionToTransactionDto(Transaction transaction);


  /**
   * Метод для преобразования TransactionDto в Transaction.
   *
   * @param transactionDto TransactionDto для преобразования.
   * @return преобразованный Transaction.
   */
  Transaction transactionDtoToTransaction(TransactionDto transactionDto);

  /**
   * Метод для преобразования CreateAccountDto в Account.
   *
   * @param createTransactionDto CreateTransactionDto для преобразования.
   * @return преобразованный Transaction.
   */
  Transaction createTransactionDtoToTransaction(CreateTransactionDto createTransactionDto);
}
