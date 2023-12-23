package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.RequestModel;
import henrik.development.splitvajs.model.Split;
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

    private final Set<Payer> payers = new HashSet<>();
    private final Set<RequestModel> requestModels = new HashSet<>();
    private final Set<ExpenseItem> total = new HashSet<>();

    public RequestModel add(@NonNull RequestModel requestModel) {
        requestModels.add(requestModel);
        handleExpense(requestModel);
        return requestModel;
    }

    private void handleExpense(RequestModel request) {

        String payerName = request.getPayer();
        String expenseName = request.getName();
        Double cost = request.getCost();
        Split split = request.getSplit();
        String expenseId = UUID.randomUUID().toString();
        LocalDateTime creationDate = LocalDateTime.now();

        Optional<Payer> optionalPayer = getPayer(payerName);

        if (optionalPayer.isPresent()) {
            handleExistingPayer(expenseId, expenseName, cost, creationDate, split, optionalPayer.get());
        } else {
            handleNewPayer(payerName, expenseName, cost, split, expenseId, creationDate);
        }
    }


    private Optional<Payer> getPayer(String payerName) {
        if (payerName == null || payerName.isBlank()) {
            throw new IllegalArgumentException("Parameter receiver can not be null or empty.");
        }
        return payers
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(payerName))
                .findFirst();
    }

    private void handleNewPayer(String payerName, String expenseName, Double cost, Split split, String expenseId, LocalDateTime creationDate) {
        Payer payer = Payer.builder()
                .name(payerName)
                .build();
        payer.getExpenses().add(PayerExpense.builder()
                .amount(cost)
                .creationDate(creationDate)
                .split(split)
                .expenseId(expenseId)
                .name(expenseName)
                .build());
        payers.add(payer);
        addToTotal(expenseId, expenseName, cost, creationDate, payer, split);
    }

    private void handleExistingPayer(String expenseId, String expenseName, Double cost, LocalDateTime creationDate, Split split, Payer payer) {
        payer.getExpenses().add(
                PayerExpense.builder()
                        .amount(cost)
                        .creationDate(creationDate)
                        .split(split)
                        .expenseId(expenseId)
                        .name(expenseName)
                        .build()
        );
        addToTotal(expenseId, expenseName, cost, creationDate, payer, split);
    }


    private void addToTotal(String expenseId, String expenseName, Double cost, LocalDateTime creationDate, Payer payer, Split split) {
        total.add(
                ExpenseItem.builder()
                        .amount(cost)
                        .creationDate(creationDate)
                        .expenseId(expenseId)
                        .name(expenseName)
                        .payerName(payer.getName())
                        .split(split)
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
        return requestModels.stream()
                .map(RequestModel::getPayer)
                .collect(Collectors.toSet());
    }

    public Set<Payer> getPayers() {
        return payers;
    }

    private Double calculateRepayment(RequestModel requestModel, Set<String> payers) {
        Double cost = requestModel.getCost();
        int numOfPayers = payers.size();
        Split split = requestModel.getSplit();
        if (split == Split.EQUAL) {
            return cost / numOfPayers;
        } else
            return cost;
    }

    public void clear() {
        requestModels.clear();
    }

    public RepaymentReceiver getRepaymentReceiver() {
        Set<IndividualRepayment> individualRepayments = getIndividualRepayments();

        final Double[] costs = {0D, 0D};
        final String[] payerName = {null};

/*        individualRepayments.forEach((payer, cost) -> {
            if (cost > costs[0]) {
                costs[1] = costs[0];
                costs[0] = cost;
                receiver[0] = payer;
            }
        });*/

        return RepaymentReceiver.builder()
                .outlayAmount(costs[0] - costs[1])
                .name(payerName[0])
                .build();
    }

    public Result getResult() {
        Integer numberOfPayers = getPayers().size() - 1; // Because the receiver does not split the rest.
        RepaymentReceiver repaymentReceiver = getRepaymentReceiver();
        return Result.builder()
                .amountToReceive(repaymentReceiver.getOutlayAmount() / numberOfPayers)
                .receiverName(repaymentReceiver.getName())
                .build();
    }

    public RequestModel removeById(String id) {
        Optional<RequestModel> optionalExpenseItem = requestModels.stream()
                .filter(requestModel -> requestModel.getId().equals(id))
                .findFirst();
        if (optionalExpenseItem.isPresent()) {
            RequestModel requestModel = optionalExpenseItem.get();
            requestModels.remove(requestModel);
            return requestModel;
        }
        return null;
    }
}
