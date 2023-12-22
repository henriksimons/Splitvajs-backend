package henrik.development.splitvajs.service;

import lombok.Builder;

@Builder
public record IndividualRepayment(String name, Double expectedRepayment) {
    @Override
    public String toString() {
        return "IndividualRepayment{" +
                "name='" + name + '\'' +
                ", expectedRepayment=" + expectedRepayment +
                '}';
    }
}
