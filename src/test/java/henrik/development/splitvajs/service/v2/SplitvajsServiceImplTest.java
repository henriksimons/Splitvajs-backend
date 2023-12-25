package henrik.development.splitvajs.service.v2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static henrik.development.splitvajs.service.v2.TestConstants.*;

@SpringBootTest
class SplitvajsServiceImplTest {


    @Autowired
    SplitvajsServiceImpl service;

    @BeforeEach
    void setUp() {
        service.addExpense(R1);
        service.addExpense(R2);
        service.addExpense(R3);
        service.addExpense(R4);
    }

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
        service.getResult();
    }
}