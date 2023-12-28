package henrik.development.splitvajs.service;

import henrik.development.splitvajs.model.request.ExpenseRequestModel;
import henrik.development.splitvajs.model.response.ExpenseResponseModel;

import java.util.List;
import java.util.Map;

public interface SplitvajsService {

    Expense addExpense(ExpenseRequestModel expenseRequestModel);

    Person addPerson(String name);

    void clear();

    List<ExpenseResponseModel> getExpenses();

    List<Expense> getExpenses(Person person);

    List<Expense> getExpenses(String payerId);

    List<Person> getPeople();

    Person getPerson(String name);

    Person getPersonById(String id);

    Map<String, Double> getResult();

    void removeExpense(String id);

    void removePerson(String id);

}
