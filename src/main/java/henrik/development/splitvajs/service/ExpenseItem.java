package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.Split;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExpenseItem(LocalDateTime creationDate, String expenseId, String payerName, String name, Double amount,
                          Split split) {
    @Override
    public String toString() {
        return "ExpenseItem{" +
                "expenseId='" + expenseId + '\'' +
                ", receiver='" + payerName + '\'' +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", repayment=" + split +
                '}';
    }
}
