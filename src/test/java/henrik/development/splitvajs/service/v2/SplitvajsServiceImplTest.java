package henrik.development.splitvajs.service.v2;

import henrik.development.splitvajs.model.response.ResultsResponseModel;
import henrik.development.splitvajs.service.SplitvajsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static henrik.development.splitvajs.service.v2.TestConstants.*;

@SpringBootTest
class SplitvajsServiceImplTest {

    @Autowired
    SplitvajsServiceImpl service;

    @AfterEach
    void tearDown() {
        service.clear();
    }

    @Test
    void getResult() {

        service.addExpense(R1);
        service.addExpense(R2);
        service.addExpense(R3);
        service.addExpense(R4);

        ResultsResponseModel result = service.getResult();
    }

    @Test
    void getResult2() {

        service.addExpense(RIda);
        service.addExpense(RHenrik);
        service.addExpense(RHenrik);

        ResultsResponseModel result = service.getResult();

    }

}