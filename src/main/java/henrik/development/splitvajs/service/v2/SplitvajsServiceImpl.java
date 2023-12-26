package henrik.development.splitvajs.service.v2;

import henrik.development.splitvajs.model.RequestModel;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class SplitvajsServiceImpl implements SplitvajsService {

    private final Group group;

    @Autowired
    public SplitvajsServiceImpl(Group group) {
        this.group = group;
    }

    @Override
    public Expense addExpense(@NonNull RequestModel requestModel) {
        Person person = resolvePayer(requestModel.getPayer());
        Expense expense = Expense.builder()
                .dateOfCreation(LocalDateTime.now())
                .id(UUID.randomUUID().toString())
                .name(requestModel.getName())
                .payerId(person.getId())
                .value(requestModel.getCost())
                .split(requestModel.getSplit())
                .build();
        group.addExpense(expense);
        Optional<Expense> optionalExpense = group.getExpense(expense);
        if (optionalExpense.isEmpty()) {
            throw new IllegalStateException("Error while saving expense in database.");
        }
        person.getExpenses().add(optionalExpense.get());
        return expense;
    }

    @Override
    public List<Expense> getExpenses() {
        return group.getExpenses();
    }

    private Person resolvePayer(@NonNull String payerName) {
        Optional<Person> optionalPayer = group.getPerson(payerName);
        if (optionalPayer.isPresent()) {
            return optionalPayer.get();
        } else {
            Person person = Person.builder()
                    .id(UUID.randomUUID().toString())
                    .name(payerName)
                    .expenses(new HashSet<>())
                    .debt(new HashMap<>())
                    .build();
            group.addPayer(person);
            return person;
        }
    }

    @Override
    public void clear() {
        group.clearExpenses();
        group.clearPayers();
    }

    @Override
    public void removePerson(@NonNull String id) {
        group.removePerson(id);
    }

    @Override
    public void removeExpense(@NonNull String id) {
        group.removeExpense(id);
    }

    @Override
    public Person getPersonById(@NonNull String id) {
        return group.getPayerById(id).orElse(null);
    }

    @Override
    public Person getPerson(@NonNull String name) {
        return group.getPerson(name).orElse(null);
    }

    @Override
    public List<Person> getPeople() {
        return group.getPeople();
    }

    @Override
    public List<Expense> getExpenses(@NonNull Person person) {
        return group.getExpensesForPayer(person);
    }

    @Override
    public List<Expense> getExpenses(@NonNull String payerId) {
        return group.getExpensesByPayerId(payerId);
    }

    @Override
    public Map<String, Double> getResult() {

        List<Person> people = group.getPeople();
        List<Expense> expenses = group.getExpenses();
        int distribution = people.size(); // Number of splitters

        people.forEach(person -> {
            expenses.forEach(expense -> {
                if (!paidBy(person, expense)) {
                    double debt = (expense.value() / distribution);
                    person.addDebt(expense.id(), debt);
                }
            });
        });

        Map<String, Double> debtPerPersonById = getDebtPerPersonId(people);
        Map<String, Double> repaymentPerPersonId = getRepaymentPerPersonId(people);

        Map<String, Double> balancePerPersonId = new HashMap<>();

        repaymentPerPersonId.forEach((id, repayment) -> {
            balancePerPersonId.put(getPersonName(id), repayment - debtPerPersonById.get(id));
        });

        return balancePerPersonId;
    }

    @Override
    public Person addPerson(@NonNull String name) {
        Person person = Person.builder()
                .id(UUID.randomUUID().toString())
                .debt(new HashMap<>())
                .expenses(new HashSet<>())
                .name(name)
                .build();
        return group.addPerson(person);
    }

    private String getPersonName(String id) {
        Optional<Person> optionalPerson = group.getPayerById(id);
        if (optionalPerson.isPresent()) {
            return optionalPerson.get().getName();
        } else return id;
    }

    private boolean paidBy(Person person, Expense expense) {
        return expense.payerId().equalsIgnoreCase(person.getId());
    }

    private Map<String, Double> getDebtPerPersonId(List<Person> persons) {
        Map<String, Double> debtPerPersonId = new HashMap<>();
        persons
                .forEach(person -> debtPerPersonId.put(person.getId(), person.getTotalDebt()));
        return debtPerPersonId;
    }

    private Map<String, Double> getRepaymentPerPersonId(List<Person> persons) {
        Map<String, Double> repaymentPerPersonId = new HashMap<>();
        persons
                .forEach(person -> repaymentPerPersonId.put(person.getId(), person.getTotalRepayment(persons.size())));
        return repaymentPerPersonId;
    }
}
