package henrik.development.splitvajs.model;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PayerRequestModelTest {

    @Test
    void testFirstLetterOfNameIsCapitalized() {
        String expected = "Lowercase";
        String input = "lowercase";
        RequestModel requestModel = RequestModel.builder()
                .name(input)
                .cost(0D)
                .split(Split.EQUAL)
                .payer(input)
                .build();
        assertEquals(expected, requestModel.getName());
        assertEquals(expected, requestModel.getPayer());
    }

    @Test
    void testExpenseItemCanNotBeInitiatedWithForbiddenNullValues() {
        assertThrows(NullPointerException.class, () -> {
            RequestModel.builder()
                    .name(null)
                    .cost(0D)
                    .split(Split.EQUAL)
                    .payer("payer")
                    .build();
        });
        assertThrows(NullPointerException.class, () -> {
            RequestModel.builder()
                    .name("Name")
                    .cost(null)
                    .split(Split.EQUAL)
                    .payer("payer")
                    .build();
        });
        assertThrows(NullPointerException.class, () -> {
            RequestModel.builder()
                    .name("Name")
                    .cost(0D)
                    .split(null)
                    .payer("payer")
                    .build();
        });
        assertThrows(NullPointerException.class, () -> {
            RequestModel.builder()
                    .name("Name")
                    .cost(0D)
                    .split(Split.EQUAL)
                    .payer(null)
                    .build();
        });
    }

    @Test
    void testIdIsGenerated() {
        RequestModel requestModel = RequestModel.builder().payer("test").name("test").split(Split.EQUAL).cost(0D).build();
        assertNotNull(requestModel.getId());
    }

    @Test
    void testCreationDateIsGenerated() {
        LocalDate localDate = LocalDate.now();

        RequestModel requestModel = RequestModel.builder()
                .payer("test")
                .name("test")
                .split(Split.EQUAL)
                .cost(0D)
                .build();

        assertNotNull(requestModel.getCreationDate());
        assertEquals(localDate, requestModel.getCreationDate().toLocalDate());
    }
}