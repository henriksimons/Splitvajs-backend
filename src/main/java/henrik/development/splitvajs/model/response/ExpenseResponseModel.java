package henrik.development.splitvajs.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpenseResponseModel {

    private String name;
    private Double value;
    private String payerId;
    private String payerName;
    private Double split;

}
