package henrik.development.splitvajs.service.v2;

import henrik.development.splitvajs.model.RequestModel;
import henrik.development.splitvajs.model.Split;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        Payer payer = resolvePayer(requestModel.getPayer());
        Expense expense = Expense.builder().dateOfCreation(LocalDateTime.now())
                .id(UUID.randomUUID().toString())
                .name(requestModel.getName())
                .payerId(payer.id())
                .value(requestModel.getCost())
                .split(requestModel.getSplit())
                .build();
        db.addExpense(expense);
        Optional<Expense> optionalExpense = db.getExpense(expense);
        if (optionalExpense.isEmpty()) {
            throw new IllegalStateException("Error while saving expense in database.");
        }
        payer.expenses().add(optionalExpense.get());
        return expense;
    }

    @Override
    public List<Expense> getExpenses() {
        return db.getAllExpenses();
    }

    private Payer resolvePayer(String payerName) {
        if (payerName == null) {
            throw new IllegalArgumentException("PayerName can not be null.");
        }
        Optional<Payer> optionalPayer = db.getPayer(payerName);
        if (optionalPayer.isPresent()) {
            return optionalPayer.get();
        } else {
            Payer payer = Payer.builder()
                    .id(UUID.randomUUID().toString())
                    .name(payerName)
                    .expenses(new HashSet<>()).build();
            db.addPayer(payer);
            return payer;
        }
    }

    @Override
    public void clear() {
        db.clearExpenses();
        db.clearPayers();
    }

    @Override
    public Payer getPayerById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Parameter id can not be null");
        }
        return db.getPayerById(id).orElse(null);
    }

    @Override
    public Payer getPayer(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Parameter name can not be null");
        }
        return db.getPayer(name).orElse(null);
    }

    @Override
    public List<Payer> getPayers() {
        return db.getAllPayers();
    }

    @Override
    public List<Expense> getExpenses(Payer payer) {
        return db.getExpensesForPayer(payer);
    }

    @Override
    public List<Expense> getExpenses(String payerId) {
        return db.getExpensesByPayerId(payerId);
    }

    @Override
    public Repayment getResult() {
        List<Payer> payers = db.getAllPayers();

        List<Repayment> payerRepayments = payers
                .stream()
                .map(payer -> Repayment.builder()
                        .receiver(payer.name())
                        .value(getExpectedRepayment(payer))
                        .build())
                .sorted((r1, r2) -> (int) (r2.value() - r1.value()))
                .toList();

        // TODO: 2023-12-23 Handles size 2 right now.
        double repaymentValue = payerRepayments.get(0).value() - payerRepayments.get(1).value();
        String receiver = repaymentValue == 0 ? "Equal" : payerRepayments.get(0).receiver();

        return Repayment.builder()
                .receiver(receiver)
                .value(repaymentValue)
                .build();
    }

    private Double getExpectedRepayment(Payer payer) {
        return payer.expenses()
                .stream()
                .map(expense -> expense.value() * getPercentage(expense.split()))
                .reduce(0D, Double::sum);
    }

    private Double getPercentage(Split split) {
        switch (split) {
            case FULL -> {
                return 1D;
            }
            case EQUAL -> {
                return 0.5D;
            }
            default -> {
                return 0D;
            }
        }
    }
}
