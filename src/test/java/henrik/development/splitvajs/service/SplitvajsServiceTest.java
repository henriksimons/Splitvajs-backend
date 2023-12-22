package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.ExpenseItem;
import henrik.development.splitvajs.model.Repayment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SplitvajsServiceTest {

    private static final String PAYER_A = "A";
    private static final String PAYER_B = "B";
    private static final List<ExpenseItem> expenseItemList = new ArrayList<>();

    @Autowired
    SplitvajsService splitvajsService;

    @BeforeAll
    void setUp() {

        ExpenseItem e1 = ExpenseItem.builder()
                .cost(100D)
                .payer(PAYER_A)
                .repayment(Repayment.FULL)
                .build();

        ExpenseItem e2 = ExpenseItem.builder()
                .cost(100D)
                .payer(PAYER_A)
                .repayment(Repayment.FULL)
                .build();

        ExpenseItem e3 = ExpenseItem.builder()
                .cost(100D)
                .payer(PAYER_B)
                .repayment(Repayment.FULL)
                .build();

        expenseItemList.add(e1);
        expenseItemList.add(e2);
        expenseItemList.add(e3);
    }

    @Test
    void add() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getIndividualRepayments() {
        splitvajsService.getExpenseItems().addAll(expenseItemList);
        Map<String, Double> individualRepayments = splitvajsService.getIndividualRepayments();
        assertNotNull(individualRepayments);
    }
}