package henrik.development.splitvajs.service.v2;

import henrik.development.splitvajs.model.RequestModel;
import henrik.development.splitvajs.model.Split;

public class TestConstants {

    public static final RequestModel R1 = RequestModel.builder()
            .split(Split.EQUAL)
            .name("1000")
            .payer("A")
            .cost(1000D)
            .build();

    public static final RequestModel R2 = RequestModel.builder()
            .split(Split.EQUAL)
            .name("400")
            .payer("B")
            .cost(400D)
            .build();

    public static final RequestModel R3 = RequestModel.builder()
            .split(Split.EQUAL)
            .name("200")
            .payer("C")
            .cost(200D)
            .build();

    public static final RequestModel R4 = RequestModel.builder()
            .split(Split.EQUAL)
            .name("40")
            .payer("D")
            .cost(40D)
            .build();

}
