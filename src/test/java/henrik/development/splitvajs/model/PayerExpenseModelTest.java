package henrik.development.splitvajs.model;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PayerExpenseModelTest {

    @Test
    void testFirstLetterOfNameIsCapitalized() {
        String expected = "Lowercase";
        String input = "lowercase";
        ExpenseModel expenseModel = ExpenseModel.builder()
                .name(input)
                .cost(0D)
                .repayment(Repayment.EQUAL)
                .payer(input)
                .build();
        assertEquals(expected, expenseModel.getName());
        assertEquals(expected, expenseModel.getPayer());
    }

    @Test
    void testExpenseItemCanNotBeInitiatedWithForbiddenNullValues() {
        assertThrows(NullPointerException.class, () -> {
            ExpenseModel.builder()
                    .name(null)
                    .cost(0D)
                    .repayment(Repayment.EQUAL)
                    .payer("payer")
                    .build();
        });
        assertThrows(NullPointerException.class, () -> {
            ExpenseModel.builder()
                    .name("Name")
                    .cost(null)
                    .repayment(Repayment.EQUAL)
                    .payer("payer")
                    .build();
        });
        assertThrows(NullPointerException.class, () -> {
            ExpenseModel.builder()
                    .name("Name")
                    .cost(0D)
                    .repayment(null)
                    .payer("payer")
                    .build();
        });
        assertThrows(NullPointerException.class, () -> {
            ExpenseModel.builder()
                    .name("Name")
                    .cost(0D)
                    .repayment(Repayment.EQUAL)
                    .payer(null)
                    .build();
        });
    }

    @Test
    void testIdIsGenerated() {
        ExpenseModel expenseModel = ExpenseModel.builder().payer("test").name("test").repayment(Repayment.EQUAL).cost(0D).build();
        assertNotNull(expenseModel.getId());
    }

    @Test
    void testCreationDateIsGenerated() {
        LocalDate localDate = LocalDate.now();

        ExpenseModel expenseModel = ExpenseModel.builder()
                .payer("test")
                .name("test")
                .repayment(Repayment.EQUAL)
                .cost(0D)
                .build();

        assertNotNull(expenseModel.getCreationDate());
        assertEquals(localDate, expenseModel.getCreationDate().toLocalDate());
    }
}