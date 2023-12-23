package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.Split;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PayerExpense(Double amount, LocalDateTime creationDate, String expenseId, Split split, String name) {
}
