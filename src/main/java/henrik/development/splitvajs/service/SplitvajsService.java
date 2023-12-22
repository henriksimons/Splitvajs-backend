package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.ExpenseItem;
import henrik.development.splitvajs.model.Repayment;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class SplitvajsService {

    private final Set<ExpenseItem> expenseItems = new HashSet<>();
    private final Set<Payer> payers = new HashSet<>();

    public ExpenseItem add(@NonNull ExpenseItem expenseItem) {
        expenseItems.add(expenseItem);
        handlePayment(expenseItem);
        return expenseItem;
    }

    private void handlePayment(ExpenseItem expenseItem) {

        String payerName = expenseItem.getPayer();
        String expenseName = expenseItem.getName();
        Double cost = expenseItem.getCost();
        Repayment repayment = expenseItem.getRepayment();

        Optional<Payer> optionalPayer = payers.stream()
                .filter(p -> p.getName().equalsIgnoreCase(payerName))
                .findFirst();

        if (optionalPayer.isPresent()) {
            Payer identifiedPayer = optionalPayer.get();
            List<Outlay> outlays = identifiedPayer.getOutlays();
            outlays.add(Outlay.builder()
                    .amount(cost)
                    .name(expenseName)
                    .expectedRepayment(repayment)
                    .build()
            );
        } else {
            Payer newPayer = Payer.builder().name(payerName).build();
            newPayer.getOutlays().add(
                    Outlay.builder()
                            .amount(cost)
                            .name(expenseName)
                            .expectedRepayment(repayment)
                            .build()
            );
            payers.add(newPayer);
        }
    }

    public Set<ExpenseItem> getAll() {
        return expenseItems;
    }

    public Map<String, Double> getIndividualRepayments() {

        Set<String> payers = getPayers();

        Map<String, Double> individualRepayments = new HashMap<>();

        payers.forEach(payer -> {
            Double repayment = expenseItems.stream()
                    .filter(expenseItem -> expenseItem.getPayer().equals(payer))
                    .map(expenseItem -> calculateRepayment(expenseItem, payers))
                    .reduce(0D, Double::sum);

            individualRepayments.put(payer, repayment);
        });
        return individualRepayments;
    }

    private Set<String> getPayers() {
        return expenseItems.stream()
                .map(ExpenseItem::getPayer)
                .collect(Collectors.toSet());
    }

    private Double calculateRepayment(ExpenseItem expenseItem, Set<String> payers) {
        Double cost = expenseItem.getCost();
        int numOfPayers = payers.size();
        Repayment repayment = expenseItem.getRepayment();
        if (repayment == Repayment.EQUAL) {
            return cost / numOfPayers;
        } else
            return cost;
    }

    public void clear() {
        expenseItems.clear();
    }

    public RepaymentReceiver getRepaymentReceiver() {
        Map<String, Double> individualRepayments = getIndividualRepayments();

        final Double[] costs = {0D, 0D};
        final String[] payerName = {null};

        individualRepayments.forEach((payer, cost) -> {
            if (cost > costs[0]) {
                costs[1] = costs[0];
                costs[0] = cost;
                payerName[0] = payer;
            }
        });

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

    public ExpenseItem removeById(String id) {
        Optional<ExpenseItem> optionalExpenseItem = expenseItems.stream()
                .filter(expenseItem -> expenseItem.getId().equals(id))
                .findFirst();
        if (optionalExpenseItem.isPresent()) {
            ExpenseItem expenseItem = optionalExpenseItem.get();
            expenseItems.remove(expenseItem);
            return expenseItem;
        }
        return null;
    }
}
