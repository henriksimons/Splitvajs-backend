package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.Repayment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExpenseItem(LocalDateTime creationDate, String expenseId, String payerName, String name, Double amount,
                          Repayment repayment) {
    @Override
    public String toString() {
        return "ExpenseItem{" +
                "expenseId='" + expenseId + '\'' +
                ", payerName='" + payerName + '\'' +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", repayment=" + repayment +
                '}';
    }
}
