package henrik.development.splitvajs.service.v2;

import henrik.development.splitvajs.model.RequestModel;

import java.util.List;

public interface SplitvajsService {

    Expense addExpense(RequestModel requestModel);

    List<Expense> getExpenses();

    void clear();

    Payer getPayerById(String id);

    Payer getPayer(String name);

    List<Payer> getPayers();

    List<Expense> getExpenses(Payer payer);

    List<Expense> getExpenses(String payerId);

    Repayment getResult();

}
