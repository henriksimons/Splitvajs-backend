package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.ExpenseModel;
import henrik.development.splitvajs.model.Repayment;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Data
public class SplitvajsService {

    private final Set<ExpenseModel> expenseModels = new HashSet<>();
    private final Set<ExpenseItem> total = new HashSet<>();
    private final Set<Payer> payers = new HashSet<>();

    public ExpenseModel add(@NonNull ExpenseModel expenseModel) {
        expenseModels.add(expenseModel);
        handleExpense(expenseModel);
        return expenseModel;
    }

    private void handleExpense(ExpenseModel request) {

        String payerName = request.getPayer();
        String expenseName = request.getName();
        Double cost = request.getCost();
        Repayment repayment = request.getRepayment();
        String expenseId = UUID.randomUUID().toString();
        LocalDateTime creationDate = LocalDateTime.now();

        Optional<Payer> optionalPayer = getPayer(payerName);

        if (optionalPayer.isPresent()) {
            handleExistingPayer(expenseId, expenseName, cost, creationDate, repayment, optionalPayer.get());
        } else {
            handleNewPayer(payerName, expenseName, cost, repayment, expenseId, creationDate);
        }
    }

    private Optional<Payer> getPayer(String payerName) {
        if (payerName == null || payerName.isBlank()) {
            throw new IllegalArgumentException("Parameter payerName can not be null or empty.");
        }
        return payers
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(payerName))
                .findFirst();
    }

    private void handleNewPayer(String payerName, String expenseName, Double cost, Repayment repayment, String expenseId, LocalDateTime creationDate) {
        Payer payer = Payer.builder()
                .name(payerName)
                .build();
        payer.getExpenses().add(PayerExpense.builder()
                .amount(cost)
                .creationDate(creationDate)
                .expectedRepayment(repayment)
                .expenseId(expenseId)
                .name(expenseName)
                .build());
        payers.add(payer);
        addToTotal(expenseId, expenseName, cost, creationDate, payer, repayment);
    }

    private void handleExistingPayer(String expenseId, String expenseName, Double cost, LocalDateTime creationDate, Repayment repayment, Payer payer) {
        payer.getExpenses().add(
                PayerExpense.builder()
                        .amount(cost)
                        .creationDate(creationDate)
                        .expectedRepayment(repayment)
                        .expenseId(expenseId)
                        .name(expenseName)
                        .build()
        );
        addToTotal(expenseId, expenseName, cost, creationDate, payer, repayment);
    }


    private void addToTotal(String expenseId, String expenseName, Double cost, LocalDateTime creationDate, Payer payer, Repayment repayment) {
        total.add(
                ExpenseItem.builder()
                        .amount(cost)
                        .creationDate(creationDate)
                        .expenseId(expenseId)
                        .name(expenseName)
                        .payerName(payer.getName())
                        .repayment(repayment)
                        .build()
        );
    }

    public Set<ExpenseItem> getAll() {
        return total;
    }

    public Set<IndividualRepayment> getIndividualRepayments() {
        return payers.stream()
                .map(payer -> IndividualRepayment.builder().name(payer.getName()).expectedRepayment(payer.getExpectedRepayment()).build())
                .collect(Collectors.toSet());
    }

    private Set<String> getPayersInternal() {
        return expenseModels.stream()
                .map(ExpenseModel::getPayer)
                .collect(Collectors.toSet());
    }

    public Set<Payer> getPayers() {
        return payers;
    }

    private Double calculateRepayment(ExpenseModel expenseModel, Set<String> payers) {
        Double cost = expenseModel.getCost();
        int numOfPayers = payers.size();
        Repayment repayment = expenseModel.getRepayment();
        if (repayment == Repayment.EQUAL) {
            return cost / numOfPayers;
        } else
            return cost;
    }

    public void clear() {
        expenseModels.clear();
    }

    public RepaymentReceiver getRepaymentReceiver() {
        Set<IndividualRepayment> individualRepayments = getIndividualRepayments();

        final Double[] costs = {0D, 0D};
        final String[] payerName = {null};

/*        individualRepayments.forEach((payer, cost) -> {
            if (cost > costs[0]) {
                costs[1] = costs[0];
                costs[0] = cost;
                payerName[0] = payer;
            }
        });*/

        return RepaymentReceiver.builder()
                .outlayAmount(costs[0] - costs[1])
                .name(payerName[0])
                .build();
    }

    public Outcome getOutcome() {
        Integer numberOfPayers = getPayers().size() - 1; // Because the receiver does not split the rest.
        RepaymentReceiver repaymentReceiver = getRepaymentReceiver();
        return Outcome.builder()
                .amountToReceive(repaymentReceiver.getOutlayAmount() / numberOfPayers)
                .receiverName(repaymentReceiver.getName())
                .build();
    }

    public ExpenseModel removeById(String id) {
        Optional<ExpenseModel> optionalExpenseItem = expenseModels.stream()
                .filter(expenseModel -> expenseModel.getId().equals(id))
                .findFirst();
        if (optionalExpenseItem.isPresent()) {
            ExpenseModel expenseModel = optionalExpenseItem.get();
            expenseModels.remove(expenseModel);
            return expenseModel;
        }
        return null;
    }
}
