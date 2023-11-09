package by.toukach.walletservice.dto;

import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomDeserializer;
import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomSerializer;
import by.toukach.walletservice.enumiration.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;
import lombok.NoArgsConstructor;

/**
 * Класс представляющий DTO для операции со счетом.
 * */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {

  private Long id;
  @JsonSerialize(using = LocalDateTimeCustomSerializer.class)
  @JsonDeserialize(using = LocalDateTimeCustomDeserializer.class)
  @Exclude
  private LocalDateTime createdAt;
  private TransactionType type;
  private Long userId;
  private Long accountId;
  private BigDecimal value;
}
