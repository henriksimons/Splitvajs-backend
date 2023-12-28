package henrik.development.splitvajs.model.response;

import lombok.Builder;

@Builder
public record ResultResponseModel(Double balance,
                                  String personId,
                                  String personName,
                                  Double totalDebt,
                                  Double totalExpenses) {
}
