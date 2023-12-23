package henrik.development.splitvajs.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Result {
    /**
     * Name of the payer to receive the outcome.
     */
    private String receiverName;
    /**
     * Amount of money to receive.
     */
    private Double amountToReceive;
}
