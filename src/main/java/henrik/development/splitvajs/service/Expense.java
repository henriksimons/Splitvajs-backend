package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.Split;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Expense(LocalDateTime dateOfCreation, String id, String name, String payerId, Double value, Split split) {

    @Override
    public String toString() {
        return "Expense{" +
                "dateOfCreation=" + dateOfCreation +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", payerId='" + payerId + '\'' +
                ", value=" + value +
                ", split=" + split +
                '}';
    }
}
