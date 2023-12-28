package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.Split;
import henrik.development.splitvajs.model.request.ExpenseRequestModel;
import henrik.development.splitvajs.model.response.ExpenseResponseModel;
import henrik.development.splitvajs.model.response.ResultResponseModel;
import henrik.development.splitvajs.model.response.ResultsResponseModel;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SplitvajsServiceImpl implements SplitvajsService {

    private final Group group;

    @Autowired
    public SplitvajsServiceImpl(Group group) {
        this.group = group;
    }

    @Override
    public Expense addExpense(@NonNull ExpenseRequestModel expenseRequestModel) {
        Person person = resolvePerson(expenseRequestModel.getPayer());
        Expense expense = Expense.builder()
                .dateOfCreation(LocalDateTime.now())
                .id(UUID.randomUUID().toString())
                .name(expenseRequestModel.getName())
                .payerId(person.getId())
                .value(expenseRequestModel.getCost())
                .split(expenseRequestModel.getSplit())
                .build();
        group.addExpense(expense);
        Optional<Expense> optionalExpense = group.getExpense(expense);
        if (optionalExpense.isEmpty()) {
            throw new IllegalStateException("Error while saving expense in database.");
        }
        person.getExpenses().add(optionalExpense.get());
        return expense;
    }

    private Person resolvePerson(@NonNull String payerName) {
        Optional<Person> optionalPerson = group.getPerson(payerName);
        if (optionalPerson.isPresent()) {
            return optionalPerson.get();
        } else {
            Person person = Person.builder()
                    .id(UUID.randomUUID().toString())
                    .name(payerName)
                    .expenses(new HashSet<>())
                    .debt(new HashMap<>())
                    .build();
            group.addPerson(person);
            return person;
        }
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

    @Override
    public void clear() {
        group.clearExpenses();
        group.clearPersons();
    }

    @Override
    public List<ExpenseResponseModel> getExpenses() {
        List<Expense> expenses = group.getExpenses();
        List<Person> people = group.getPeople();

        return expenses.stream()
                .map(expense -> mapExpenseResponseModel(people, expense))
                .collect(Collectors.toList());
    }

    private ExpenseResponseModel mapExpenseResponseModel(List<Person> people, Expense expense) {
        return ExpenseResponseModel.builder()
                .payerId(expense.payerId())
                .expenseId(expense.id())
                .payerName(getNameByPayerId(expense))
                .value(expense.value())
                .split(mapSplit(people.size(), expense.split()))
                .name(expense.name())
                .build();
    }

    private String getNameByPayerId(Expense expense) {
        Optional<Person> optionalPerson = group.getPersonById(expense.payerId());
        return optionalPerson.map(Person::getName).orElse(null);
    }

    private Double mapSplit(int numberOfPersons, Split split) {
        return split == Split.EQUAL ? (1.0 / numberOfPersons) * 100 : 100;
    }

    @Override
    public List<Expense> getExpenses(@NonNull Person person) {
        return group.getExpensesForPerson(person);
    }

    @Override
    public List<Expense> getExpenses(@NonNull String payerId) {
        return group.getExpensesByPersonId(payerId);
    }

    @Override
    public List<Person> getPeople() {
        return group.getPeople();
    }

    @Override
    public Person getPerson(@NonNull String name) {
        return group.getPerson(name).orElse(null);
    }

    @Override
    public Person getPersonById(@NonNull String id) {
        return group.getPersonById(id).orElse(null);
    }

    @Override
    public ResultsResponseModel getResult() {

        List<Person> people = group.getPeople();
        List<Expense> expenses = group.getExpenses();

        calculateIndividualDebt(people, expenses);

        Map<String, Double> debtPerPersonById = getDebtPerPersonId(people);
        Map<String, Double> repaymentPerPersonId = getRepaymentPerPersonId(people);
        Map<String, Double> totalPaymentPerPersonId = getTotalPaymentPerPersonId(people);

        Map<String, Double> balancePerPersonId = new HashMap<>();

        repaymentPerPersonId.forEach((id, repayment) -> {
            balancePerPersonId.put(id, repayment - debtPerPersonById.get(id));
        });

        ResultsResponseModel results = new ResultsResponseModel();

        balancePerPersonId
                .entrySet()
                .stream()
                .map(result -> ResultResponseModel.builder()
                        .personId(result.getKey())
                        .personName(getPersonName(result.getKey()))
                        .totalDebt(debtPerPersonById.get(result.getKey()))
                        .totalExpenses(totalPaymentPerPersonId.get(result.getKey()))
                        .balance(result.getValue())
                        .build()
                )
                .forEach(results::add);

        clearDebt(people);

        return results;
    }

    private void calculateIndividualDebt(List<Person> people, List<Expense> expenses) {
        int distribution = people.size(); // Number of splitters
        people.forEach(person -> {
            expenses.forEach(expense -> {
                if (!paidBy(person, expense)) {
                    double debt;
                    if (expense.split() == Split.EQUAL) {
                        debt = (expense.value() / distribution);
                    } else {
                        debt = (expense.value() / (distribution - 1));
                    }
                    person.addDebt(expense.id(), debt);
                }
            });
        });
    }

    private Map<String, Double> getDebtPerPersonId(List<Person> persons) {
        Map<String, Double> debtPerPersonId = new HashMap<>();
        persons.forEach(person -> debtPerPersonId.put(person.getId(), person.getTotalDebt()));
        return debtPerPersonId;
    }

    private Map<String, Double> getRepaymentPerPersonId(List<Person> persons) {
        Map<String, Double> repaymentPerPersonId = new HashMap<>();
        persons.forEach(person -> repaymentPerPersonId.put(person.getId(), person.getTotalRepayment(persons.size())));
        return repaymentPerPersonId;
    }

    private Map<String, Double> getTotalPaymentPerPersonId(List<Person> persons) {
        Map<String, Double> paymentPerPersonId = new HashMap<>();
        persons.forEach(person -> paymentPerPersonId.put(person.getId(), person.getTotalPayment()));
        return paymentPerPersonId;
    }

    private String getPersonName(String id) {
        Optional<Person> optionalPerson = group.getPersonById(id);
        if (optionalPerson.isPresent()) {
            return optionalPerson.get().getName();
        } else return id;
    }

    private void clearDebt(List<Person> people) {
        for (Person person : people) {
            person.clearDebt();
        }
    }

    private boolean paidBy(Person person, Expense expense) {
        return expense.payerId().equalsIgnoreCase(person.getId());
    }

    @Override
    public void removeExpense(@NonNull String id) {

        Optional<Expense> optionalExpense = group.getExpenseById(id);

        if (optionalExpense.isPresent()) {

            Expense expense = optionalExpense.get();
            String personId = expense.payerId();

            Optional<Person> optionalPerson = group.getPersonById(personId);

            if (optionalPerson.isPresent()) {

                Person person = optionalPerson.get();

                Optional<Expense> optionalPersonExpense = person
                        .getExpenses()
                        .stream()
                        .filter(e -> e.id().equalsIgnoreCase(id))
                        .findFirst();

                if (optionalPersonExpense.isPresent()) {
                    person.getExpenses().remove(optionalExpense.get());
                }
            }
        }
        group.removeExpense(id);

    }

    @Override
    public void removePerson(@NonNull String id) {
        group.removePerson(id);
    }
}
