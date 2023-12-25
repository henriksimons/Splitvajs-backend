package henrik.development.splitvajs.service.v2;

import henrik.development.splitvajs.model.Split;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

@Builder
@RequiredArgsConstructor
@Getter
public class Person {

    private final Map<String, Double> debt;
    private final Set<Expense> expenses;
    private final String id;
    private final String name;

    /**
     * @param distribution The number of people to split the cost.
     * @return Total repayment value to the person.
     */
    public Double getTotalRepayment(int distribution) {
        return expenses.stream()
                .map(expense -> getRepayment(expense, distribution))
                .reduce(0D, Double::sum);
    }


    private Double getRepayment(Expense expense, int distribution) {
        if (expense.split() == Split.EQUAL) {
            return (expense.value() / distribution) * (distribution - 1);
        }
        return expense.value();
    }

    public Double getTotalDebt() {
        return debt.values().stream().reduce(0D, Double::sum);
    }

    public void addDebt(String expenseId, Double debtValue) {
        debt.put(expenseId, debtValue);
    }
}
