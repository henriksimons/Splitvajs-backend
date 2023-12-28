package henrik.development.splitvajs.model.response;

import lombok.Builder;

@Builder
public record ExpenseResponseModel(String name,
                                   Double value,
                                   String payerId,
                                   String payerName,
                                   Double split) {
}
