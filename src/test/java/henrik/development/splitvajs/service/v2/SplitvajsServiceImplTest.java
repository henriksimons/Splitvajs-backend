package henrik.development.splitvajs.service.v2;

import henrik.development.splitvajs.model.RequestModel;
import henrik.development.splitvajs.model.Split;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SplitvajsServiceImplTest {

    private final RequestModel P1_DEFAULT = RequestModel.builder()
            .split(Split.FULL)
            .name("Default")
            .payer(P1)
            .cost(0D)
            .build();
    private final RequestModel P1_EQUAL_REPAYMENT_100 = RequestModel.builder()
            .split(Split.EQUAL)
            .name("P1 should be repaid 50.")
            .payer(P1)
            .cost(100D)
            .build();
    private final RequestModel P1_FULL_REPAYMENT_100 = RequestModel.builder()
            .split(Split.FULL)
            .name("P1 should be repaid 100.")
            .payer(P1)
            .cost(100D)
            .build();
    private final RequestModel P2_DEFAULT = RequestModel.builder()
            .split(Split.FULL)
            .name("Default")
            .payer(P2)
            .cost(0D)
            .build();
    private final RequestModel P2_EQUAL_REPAYMENT_100 = RequestModel.builder()
            .split(Split.EQUAL)
            .name("P2 should be repaid 50.")
            .payer(P2)
            .cost(100D)
            .build();
    private final RequestModel P2_FULL_REPAYMENT_100 = RequestModel.builder()
            .split(Split.FULL)
            .name("P2 should be repaid 100.")
            .payer(P2)
            .cost(100D)
            .build();
    private static final String P1 = "Payer1";
    private static final String P2 = "Payer2";
    @Autowired
    SplitvajsServiceImpl service;

    @AfterEach
    void tearDown() {
        service.clear();
    }

    @Test
    void add() {
    }

    @Test
    void remove() {
    }

    @Test
    void getPayerById() {
    }

    @Test
    void getPayerByName() {
    }

    @Test
    void getPayers() {
    }

    @Test
    void getExpenses() {
    }

    @Test
    void testGetExpenses() {
    }

    @Test
    void getExpensesByPayerId() {
    }

    @Test
    void getResult() {
    }

    @Test
    void getResultFullRepaymentForP2() {
        service.addExpense(P2_FULL_REPAYMENT_100);
        service.addExpense(P1_DEFAULT);
        Repayment result = service.getResult();
        assertEquals(P2, result.receiver());
        assertEquals(100, result.value());
    }

    @Test
    void getResultFullRepaymentForP1() {
        service.addExpense(P1_FULL_REPAYMENT_100);
        service.addExpense(P2_DEFAULT);
        Repayment result = service.getResult();
        assertEquals(P1, result.receiver());
        assertEquals(100, result.value());
    }

    @Test
    void getResultShouldBeEqual() {
        service.addExpense(P1_FULL_REPAYMENT_100);
        service.addExpense(P2_FULL_REPAYMENT_100);
        Repayment result = service.getResult();
        assertEquals(0D, result.value());
    }

    @Test
    void getResultP1ShouldBeRepaid50() {
        service.addExpense(P1_EQUAL_REPAYMENT_100);
        service.addExpense(P1_EQUAL_REPAYMENT_100);
        service.addExpense(P2_EQUAL_REPAYMENT_100);
        Repayment result = service.getResult();
        assertEquals(P1, result.receiver());
        assertEquals(50D, result.value());
    }

    @Test
    void getResultP2ShouldBeRepaid50() {
        service.addExpense(P1_EQUAL_REPAYMENT_100);
        service.addExpense(P2_EQUAL_REPAYMENT_100);
        service.addExpense(P2_EQUAL_REPAYMENT_100);
        Repayment result = service.getResult();

        assertEquals(P2, result.receiver());
        assertEquals(50D, result.value());
    }
}