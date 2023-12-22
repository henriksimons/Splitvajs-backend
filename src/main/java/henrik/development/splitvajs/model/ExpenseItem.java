package henrik.development.splitvajs.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode
public class ExpenseItem {

    /**
     * UUID representing the item.
     */
    private String id;
    /**
     * Name of the expense.
     */
    private String name;
    /**
     * Name of the paying person.
     */
    private String payer;
    /**
     * Expense cost in SEK.
     */
    private Double cost;
    /**
     * How many percent of the cost the payer expects in return.
     */
    private Repayment repayment;

    public ExpenseItem(String id, String name, String payer, Double cost, Repayment repayment) {
        this.id = UUID.randomUUID().toString();
        this.name = Objects.requireNonNull(name, "Name can not be null.").toLowerCase();
        this.payer = Objects.requireNonNull(payer, "Payer can not be null.").toLowerCase();
        this.cost = Objects.requireNonNull(cost, "Cost can not be null.");
        this.repayment = Objects.requireNonNull(repayment, "Repayment can not be null.");
    }

    @Override
    public String toString() {
        return "ExpenseItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", payer='" + payer + '\'' +
                ", cost=" + cost +
                ", repaymentPercentage=" + repayment +
                '}';
    }
}
