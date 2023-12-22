package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.Repayment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PayerExpense(Double amount, LocalDateTime creationDate, String expenseId, Repayment expectedRepayment,
                           String name
) {
}
