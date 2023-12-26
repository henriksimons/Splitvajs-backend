package henrik.development.splitvajs.service.v2;

import henrik.development.splitvajs.model.Split;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Person {

    @Getter
    private final Map<String, Double> debt;
    @Getter
    private final Set<Expense> expenses;
    @Getter
    private final String id;
    @Getter
    private final String name;
    private Double totalPayment;

    /**
     * @param distribution The number of people to split the cost.
     * @return Total repayment value to the person.
     */
    public Double getTotalRepayment(int distribution) {
        return expenses.stream()
                .map(expense -> getRepayment(expense, distribution))
                .reduce(0D, Double::sum);
    }

    /**
     * Expected repayment to the person  for a specific expense.
     *
     * @param expense      The expense to be split.
     * @param distribution The number of splitters.
     * @return The repayment for the expense.
     */
    private Double getRepayment(Expense expense, int distribution) {
        if (expense.split() == Split.EQUAL) {
            return (expense.value() / distribution) * (distribution - 1);
        }
        return expense.value();
    }

    /**
     * @return The total amount of debt this person has to a group.
     */
    public Double getTotalDebt() {
        return debt.values().stream().reduce(0D, Double::sum);
    }

    public void addDebt(String expenseId, Double debtValue) {
        debt.put(expenseId, debtValue);
    }

    public Double getTotalPayment() {
        this.totalPayment = expenses.stream().map(Expense::value).reduce(0D, Double::sum);
        return totalPayment;
    }
}
