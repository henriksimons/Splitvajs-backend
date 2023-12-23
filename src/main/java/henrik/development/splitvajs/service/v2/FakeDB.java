package henrik.development.splitvajs.service.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class FakeDB {

    private final Set<Expense> expenses;
    private final Set<Payer> payers;

    public Boolean addExpense(Expense expense) {
        return expenses.add(expense);
    }

    public Optional<Expense> getExpense(Expense expense) {
        return expenses.stream()
                .filter(e -> e.equals(expense))
                .findFirst();
    }

    public Boolean addPayer(Payer payer) {
        return payers.add(payer);
    }

    public List<Payer> getAllPayers() {
        return payers.stream().toList();
    }

    public List<Expense> getAllExpenses() {
        return expenses.stream().toList();
    }

    public Optional<Payer> getPayer(String payerName) {
        return payers.stream()
                .filter(p -> p.name().equalsIgnoreCase(payerName))
                .findFirst();
    }

    public void clearExpenses() {
        expenses.clear();
    }

    public void clearPayers() {
        payers.clear();
    }

    public Optional<Payer> getPayerById(String id) {
        return payers.stream()
                .filter(payer -> payer.id().equalsIgnoreCase(id))
                .findFirst();
    }

    public List<Expense> getExpensesForPayer(Payer payer) {
        return expenses.stream()
                .filter(p -> Objects.nonNull(p.payerId()))
                .filter(expense -> expense.payerId().equalsIgnoreCase(payer.id()))
                .collect(Collectors.toList());
    }

    public List<Expense> getExpensesByPayerId(String id) {
        return expenses.stream()
                .filter(expense -> expense.payerId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
    }
}
