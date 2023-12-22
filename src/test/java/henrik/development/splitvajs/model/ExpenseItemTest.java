package henrik.development.splitvajs.model;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseItemTest {

    @Test
    void testFirstLetterOfNameIsCapitalized() {
        String expected = "Lowercase";
        String input = "lowercase";
        ExpenseItem expenseItem = ExpenseItem.builder()
                .name(input)
                .cost(0D)
                .repayment(Repayment.EQUAL)
                .payer(input)
                .build();
        assertEquals(expected, expenseItem.getName());
        assertEquals(expected, expenseItem.getPayer());
    }

    @Test
    void testExpenseItemCanNotBeInitiatedWithForbiddenNullValues() {
        assertThrows(NullPointerException.class, () -> {
            ExpenseItem.builder()
                    .name(null)
                    .cost(0D)
                    .repayment(Repayment.EQUAL)
                    .payer("payer")
                    .build();
        });
        assertThrows(NullPointerException.class, () -> {
            ExpenseItem.builder()
                    .name("Name")
                    .cost(null)
                    .repayment(Repayment.EQUAL)
                    .payer("payer")
                    .build();
        });
        assertThrows(NullPointerException.class, () -> {
            ExpenseItem.builder()
                    .name("Name")
                    .cost(0D)
                    .repayment(null)
                    .payer("payer")
                    .build();
        });
        assertThrows(NullPointerException.class, () -> {
            ExpenseItem.builder()
                    .name("Name")
                    .cost(0D)
                    .repayment(Repayment.EQUAL)
                    .payer(null)
                    .build();
        });
    }

    @Test
    void testIdIsGenerated() {
        ExpenseItem expenseItem = ExpenseItem.builder().payer("test").name("test").repayment(Repayment.EQUAL).cost(0D).build();
        assertNotNull(expenseItem.getId());
    }

    @Test
    void testCreationDateIsGenerated() {
        LocalDate localDate = LocalDate.now();

        ExpenseItem expenseItem = ExpenseItem.builder()
                .payer("test")
                .name("test")
                .repayment(Repayment.EQUAL)
                .cost(0D)
                .build();

        assertNotNull(expenseItem.getCreationDate());
        assertEquals(localDate, expenseItem.getCreationDate().toLocalDate());
    }
}