package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.Repayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Payer {
    private final List<Outlay> outlays = new ArrayList<>();
    private String name;

    public Double getExpectedRepayment() {
        return outlays.stream()
                .map(outlay -> outlay.getAmount() * getPercentage(outlay.getExpectedRepayment()))
                .reduce(0D, Double::sum);
    }

    private Double getPercentage(Repayment repayment) {
        if (repayment == Repayment.EQUAL) {
            return 0.5;
        } else if (repayment == Repayment.FULL) {
            return 1D;
        } else return 0D;
    }
}
