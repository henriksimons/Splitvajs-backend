package henrik.development.splitvajs.service.v2;

import henrik.development.splitvajs.model.request.ExpenseRequestModel;
import henrik.development.splitvajs.model.Split;

public class TestConstants {

    public static final ExpenseRequestModel R1 = ExpenseRequestModel.builder()
            .split(Split.EQUAL)
            .name("1000")
            .payer("A")
            .cost(1000D)
            .build();

    public static final ExpenseRequestModel R2 = ExpenseRequestModel.builder()
            .split(Split.EQUAL)
            .name("400")
            .payer("B")
            .cost(400D)
            .build();

    public static final ExpenseRequestModel R3 = ExpenseRequestModel.builder()
            .split(Split.EQUAL)
            .name("200")
            .payer("C")
            .cost(200D)
            .build();

    public static final ExpenseRequestModel R4 = ExpenseRequestModel.builder()
            .split(Split.EQUAL)
            .name("40")
            .payer("D")
            .cost(40D)
            .build();

    public static final ExpenseRequestModel RHenrik = ExpenseRequestModel.builder()
            .split(Split.EQUAL)
            .name("2000")
            .payer("Henrik")
            .cost(2000D)
            .build();

    public static final ExpenseRequestModel RIda = ExpenseRequestModel.builder()
            .split(Split.EQUAL)
            .name("500")
            .payer("Ida")
            .cost(500D)
            .build();

}
