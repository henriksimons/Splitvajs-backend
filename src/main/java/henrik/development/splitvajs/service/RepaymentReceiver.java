package henrik.development.splitvajs.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RepaymentReceiver {
    /**
     * Name of the payer with the largest outlay value.
     */
    private String name;
    /**
     * The value of the outlay.
     */
    private Double outlayAmount;
}
