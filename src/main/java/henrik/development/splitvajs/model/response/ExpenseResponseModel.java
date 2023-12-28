package henrik.development.splitvajs.model.response;

import lombok.Builder;

@Builder
public record ExpenseResponseModel(Double split, Double value, String expenseId, String payerId, String payerName,
                                   String name) {
}
