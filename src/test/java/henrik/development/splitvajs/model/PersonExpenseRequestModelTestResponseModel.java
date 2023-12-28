package henrik.development.splitvajs.model;


import henrik.development.splitvajs.model.request.ExpenseRequestModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonExpenseRequestModelTestResponseModel {

    @Test
    void testFirstLetterOfNameIsCapitalized() {
        String expected = "Lowercase";
        String input = "lowercase";
        ExpenseRequestModel expenseRequestModel = ExpenseRequestModel.builder()
                .name(input)
                .cost(0D)
                .split(Split.EQUAL)
                .payer(input)
                .build();
        assertEquals(expected, expenseRequestModel.getName());
        assertEquals(expected, expenseRequestModel.getPayer());
    }

    @Test
    void testExpenseItemCanNotBeInitiatedWithForbiddenNullValues() {
        assertThrows(NullPointerException.class, () -> {
            ExpenseRequestModel.builder()
                    .name(null)
                    .cost(0D)
                    .split(Split.EQUAL)
                    .payer("person")
                    .build();
        });
        assertThrows(NullPointerException.class, () -> {
            ExpenseRequestModel.builder()
                    .name("Name")
                    .cost(null)
                    .split(Split.EQUAL)
                    .payer("person")
                    .build();
        });
        assertThrows(NullPointerException.class, () -> {
            ExpenseRequestModel.builder()
                    .name("Name")
                    .cost(0D)
                    .split(null)
                    .payer("person")
                    .build();
        });
        assertThrows(NullPointerException.class, () -> {
            ExpenseRequestModel.builder()
                    .name("Name")
                    .cost(0D)
                    .split(Split.EQUAL)
                    .payer(null)
                    .build();
        });
    }

    @Test
    void testIdIsGenerated() {
        ExpenseRequestModel expenseRequestModel = ExpenseRequestModel.builder().payer("test").name("test").split(Split.EQUAL).cost(0D).build();
        assertNotNull(expenseRequestModel.getId());
    }

    @Test
    void testCreationDateIsGenerated() {
        LocalDate localDate = LocalDate.now();

        ExpenseRequestModel expenseRequestModel = ExpenseRequestModel.builder()
                .payer("test")
                .name("test")
                .split(Split.EQUAL)
                .cost(0D)
                .build();

        assertNotNull(expenseRequestModel.getCreationDate());
        assertEquals(localDate, expenseRequestModel.getCreationDate().toLocalDate());
    }
}