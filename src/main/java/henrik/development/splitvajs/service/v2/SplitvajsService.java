package henrik.development.splitvajs.service.v2;

import henrik.development.splitvajs.model.RequestModel;

import java.util.List;
import java.util.Map;

public interface SplitvajsService {

    Expense addExpense(RequestModel requestModel);

    List<Expense> getExpenses();

    void clear();

    Person getPayerById(String id);

    Person getPayer(String name);

    List<Person> getPayers();

    List<Expense> getExpenses(Person person);

    List<Expense> getExpenses(String payerId);

    Map<String, Double> getResult();

}
