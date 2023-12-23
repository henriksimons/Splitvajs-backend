package henrik.development.splitvajs.service.v2;

import lombok.Builder;

import java.util.Set;

@Builder
public record Payer(String id, String name, Set<Expense> expenses) {
}
