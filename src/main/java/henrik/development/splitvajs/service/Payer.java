package henrik.development.splitvajs.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Payer {
    private final List<Outlay> outlays = new ArrayList<>();
    private String name;
}
