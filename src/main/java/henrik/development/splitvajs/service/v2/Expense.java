package henrik.development.splitvajs.service.v2;

import henrik.development.splitvajs.model.Split;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Expense(LocalDateTime dateOfCreation, String id, String name, String payerId, Double value, Split split) {
}
