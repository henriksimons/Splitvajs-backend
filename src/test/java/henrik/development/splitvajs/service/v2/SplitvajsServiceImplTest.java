package henrik.development.splitvajs.service.v2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static henrik.development.splitvajs.service.v2.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SplitvajsServiceImplTest {


    @Autowired
    SplitvajsServiceImpl service;

    @BeforeEach
    void setUp() {
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

        service.addExpense(R1);
        service.addExpense(R2);
        service.addExpense(R3);
        service.addExpense(R4);

        Map<String, Double> result = service.getResult();
        assertEquals(590, result.get("A"));
        assertEquals(-10, result.get("B"));
        assertEquals(-210, result.get("C"));
        assertEquals(-370, result.get("D"));
    }

    @Test
    void getResult2() {

        service.addExpense(RIda);
        service.addExpense(RHenrik);
        service.addExpense(RHenrik);

        Map<String, Double> result = service.getResult();
        assertEquals(1750, result.get("Henrik"));
        assertEquals(-1750, result.get("Ida"));
    }

}