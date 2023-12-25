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
    private final Set<Person> people;

    public Boolean addExpense(Expense expense) {
        return expenses.add(expense);
    }

    public Optional<Expense> getExpense(Expense expense) {
        return expenses.stream()
                .filter(e -> e.equals(expense))
                .findFirst();
    }

    public Boolean addPayer(Person person) {
        return people.add(person);
    }

    public List<Person> getPeople() {
        return people.stream().toList();
    }

    public List<Expense> getAllExpenses() {
        return expenses.stream().toList();
    }

    public Optional<Person> getPayer(String payerName) {
        return people.stream()
                .filter(p -> p.getName().equalsIgnoreCase(payerName))
                .findFirst();
    }

    public void clearExpenses() {
        expenses.clear();
    }

    public void clearPayers() {
        people.clear();
    }

    public Optional<Person> getPayerById(String id) {
        return people.stream()
                .filter(person -> person.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    public List<Expense> getExpensesForPayer(Person person) {
        return expenses.stream()
                .filter(p -> Objects.nonNull(p.payerId()))
                .filter(expense -> expense.payerId().equalsIgnoreCase(person.getId()))
                .collect(Collectors.toList());
    }

    public List<Expense> getExpensesByPayerId(String id) {
        return expenses.stream()
                .filter(expense -> expense.payerId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
    }
}
