package henrik.development.splitvajs.service.v2;

import henrik.development.splitvajs.model.RequestModel;
import henrik.development.splitvajs.model.Split;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class SplitvajsServiceImpl implements SplitvajsService {

    private final FakeDB db;

    @Autowired
    public SplitvajsServiceImpl(FakeDB db) {
        this.db = db;
    }

    @Override
    public Expense addExpense(RequestModel requestModel) {
        if (requestModel == null) {
            throw new IllegalArgumentException("RequestModel can not be null.");
        }
        Person person = resolvePayer(requestModel.getPayer());
        Expense expense = Expense.builder().dateOfCreation(LocalDateTime.now())
                .id(UUID.randomUUID().toString())
                .name(requestModel.getName())
                .payerId(person.getId())
                .value(requestModel.getCost())
                .split(requestModel.getSplit())
                .build();
        db.addExpense(expense);
        Optional<Expense> optionalExpense = db.getExpense(expense);
        if (optionalExpense.isEmpty()) {
            throw new IllegalStateException("Error while saving expense in database.");
        }
        person.getExpenses().add(optionalExpense.get());
        return expense;
    }

    private void addDebt(Person payingPerson, Expense expense) {
        List<Person> people = db.getPeople();
        people.stream()
                .filter(person -> !person.getId().equalsIgnoreCase(payingPerson.getId()))
                .forEach(person -> person.getDebt().put(expense.id(), (expense.value() / people.size())));
    }

    @Override
    public List<Expense> getExpenses() {
        return db.getAllExpenses();
    }

    private Person resolvePayer(String payerName) {
        if (payerName == null) {
            throw new IllegalArgumentException("PayerName can not be null.");
        }
        Optional<Person> optionalPayer = db.getPayer(payerName);
        if (optionalPayer.isPresent()) {
            return optionalPayer.get();
        } else {
            Person person = Person.builder()
                    .id(UUID.randomUUID().toString())
                    .name(payerName)
                    .expenses(new HashSet<>())
                    .debt(new HashMap<>())
                    .build();
            db.addPayer(person);
            return person;
        }
    }

    @Override
    public void clear() {
        db.clearExpenses();
        db.clearPayers();
    }

    @Override
    public Person getPayerById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Parameter id can not be null");
        }
        return db.getPayerById(id).orElse(null);
    }

    @Override
    public Person getPayer(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Parameter name can not be null");
        }
        return db.getPayer(name).orElse(null);
    }

    @Override
    public List<Person> getPayers() {
        return db.getPeople();
    }

    @Override
    public List<Expense> getExpenses(Person person) {
        return db.getExpensesForPayer(person);
    }

    @Override
    public List<Expense> getExpenses(String payerId) {
        return db.getExpensesByPayerId(payerId);
    }

    @Override
    public Map<String, Double> getResult() {

        List<Person> people = db.getPeople();
        List<Expense> expenses = db.getAllExpenses();
        int distribution = people.size();

        people.forEach(person -> {
            expenses.forEach(expense -> {
                if (!paidBy(person, expense)) {
                    double debt = (expense.value() / distribution);
                    person.addDebt(expense.id(), debt);
                }
            });
        });

        Map<String, Double> debtPerPersonById = getDebtPerPId(people);
        Map<String, Double> repaymentPerPersonId = getRepaymentPerPId(people);

        Map<String, Double> balancePerPersonId = new HashMap<>();
        repaymentPerPersonId.forEach((id, repayment) -> {
            balancePerPersonId.put(getPersonName(id), repayment - debtPerPersonById.get(id));
        });

        return balancePerPersonId;
    }

    private String getPersonName(String id) {
        Optional<Person> optionalPerson = db.getPayerById(id);
        if (optionalPerson.isPresent()) {
            return optionalPerson.get().getName();
        } else return id;
    }

    private boolean paidBy(Person person, Expense expense) {
        return expense.payerId().equalsIgnoreCase(person.getId());
    }

    private Map<String, Double> getDebtPerPId(List<Person> persons) {
        Map<String, Double> debtPerPersonId = new HashMap<>();
        persons
                .forEach(person -> debtPerPersonId.put(person.getId(), person.getTotalDebt()));
        return debtPerPersonId;
    }

    private Map<String, Double> getRepaymentPerPId(List<Person> persons) {
        Map<String, Double> repaymentPerPersonId = new HashMap<>();
        persons
                .forEach(person -> repaymentPerPersonId.put(person.getId(), person.getTotalRepayment(persons.size())));
        return repaymentPerPersonId;
    }

    private Double getPercentage(Split split, Integer persons) {
        switch (split) {
            case FULL -> {
                return 1D;
            }
            case EQUAL -> {
                return (1.0 / persons) * (persons - 1);
            }
            default -> {
                return 0D;
            }
        }
    }
}
