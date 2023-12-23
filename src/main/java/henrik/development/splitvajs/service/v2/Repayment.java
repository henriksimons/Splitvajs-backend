package henrik.development.splitvajs.service.v2;

import lombok.Builder;

@Builder
public record Repayment(String receiver, Double value) {
}
