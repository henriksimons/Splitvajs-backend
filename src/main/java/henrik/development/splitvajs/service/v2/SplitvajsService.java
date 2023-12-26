package henrik.development.splitvajs.service.v2;

import henrik.development.splitvajs.model.RequestModel;

import java.util.List;
import java.util.Map;

public interface SplitvajsService {

    Expense addExpense(RequestModel requestModel);

    List<Expense> getExpenses();

    void clear();

    void removePerson(String id);

    void removeExpense(String id);

    Person getPersonById(String id);

    Person getPerson(String name);

    List<Person> getPeople();

    List<Expense> getExpenses(Person person);

    List<Expense> getExpenses(String payerId);

    Map<String, Double> getResult();

    Person addPerson(String name);

}
