package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.Repayment;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class Payer {
    private final List<PayerExpense> expenses = new ArrayList<>();
    private String name;

    public Double getExpectedRepayment() {
        return expenses.stream()
                .map(payerExpense -> payerExpense.amount() * getPercentage(payerExpense.expectedRepayment()))
                .reduce(0D, Double::sum);
    }

    private Double getPercentage(Repayment repayment) {
        if (repayment == Repayment.EQUAL) {
            return 0.5;
        } else if (repayment == Repayment.FULL) {
            return 1D;
        } else return 0D;
    }


    @Override
    public String toString() {
        return "Payer{" +
                "expenses=" + expenses +
                ", name='" + name + '\'' +
                '}';
    }
}
