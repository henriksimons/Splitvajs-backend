package henrik.development.splitvajs.service.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
// TODO: 2023-12-26 Add logic to handle separate groups.
public class Group {

    private final Map<String, Expense> expenses;
    private final Map<String, Person> people;

    public Expense addExpense(Expense expense) {
        return expenses.put(expense.id(), expense);
    }

    public Optional<Expense> getExpense(Expense expense) {
        return expenses.containsKey(expense.id()) ? Optional.of(expenses.get(expense.id())) : Optional.empty();
    }

    public Person addPayer(Person person) {
        return people.put(person.getId(), person);
    }

    public List<Person> getPeople() {
        return people.values().stream().toList();
    }

    public List<Expense> getExpenses() {
        return expenses.values().stream().toList();
    }

    public Optional<Person> getPerson(String payerName) {
        return people.values().stream().filter(person -> person.getName().equalsIgnoreCase(payerName)).findFirst();
    }

    public void clearExpenses() {
        expenses.clear();
    }

    public void clearPayers() {
        people.clear();
    }

    public Optional<Person> getPayerById(String id) {
        return people.containsKey(id) ? Optional.of(people.get(id)) : Optional.empty();
    }

    public List<Expense> getExpensesForPayer(Person person) {
        String key = person.getId();
        if (people.containsKey(key)) {
            return people.get(key).getExpenses().stream().toList();
        } else return new ArrayList<>();
    }

    public List<Expense> getExpensesByPayerId(String id) {
        return expenses.values().stream().filter(expense -> expense.payerId().equalsIgnoreCase(id)).collect(Collectors.toList());
    }

    public void removePerson(String id) {
        people.remove(id);
    }


    public void removeExpense(String id) {
        expenses.remove(id);
    }

    public Person addPerson(Person person) {
        return people.put(person.getId(), person);
    }
}
