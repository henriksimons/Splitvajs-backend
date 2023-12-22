package henrik.development.splitvajs.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;


@Data
@Builder
@EqualsAndHashCode
public class ExpenseItem {

    /**
     * UUID representing the item.
     */
    private final String id = UUID.randomUUID().toString();
    /**
     * Date of creation.
     */
    private final LocalDateTime creationDate = LocalDateTime.now();
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

    public ExpenseItem(@NonNull String name, @NonNull String payer, @NonNull Double cost, @NonNull Repayment repayment) {
        this.name = Objects.requireNonNull(format(name));
        this.payer = Objects.requireNonNull(format(payer));
        this.cost = cost;
        this.repayment = repayment;
    }

    private String format(String string) {
        if (string == null || string.isBlank()) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(string.toLowerCase());
            sb.replace(0, 1, String.valueOf(sb.charAt(0)).toUpperCase(Locale.ROOT));
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        return "ExpenseItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", payer='" + payer + '\'' +
                ", cost=" + cost +
                ", repayment=" + repayment +
                ", creationDate=" + creationDate +
                '}';
    }
}
