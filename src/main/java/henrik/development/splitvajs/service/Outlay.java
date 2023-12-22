package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.Repayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Outlay {
    private String name;
    private Double amount;
    private Repayment expectedRepayment;
}
