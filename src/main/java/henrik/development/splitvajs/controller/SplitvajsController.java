package henrik.development.splitvajs.controller;

import henrik.development.splitvajs.model.request.ExpenseRequestModel;
import henrik.development.splitvajs.model.request.PersonRequestModel;
import henrik.development.splitvajs.service.Expense;
import henrik.development.splitvajs.service.Person;
import henrik.development.splitvajs.service.SplitvajsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin(origins = {"http://localhost:3000", "https://henriksimons.github.io/"}, maxAge = 3600)
@RestController
public class SplitvajsController {
    private final SplitvajsServiceImpl service;


    @Autowired
    public SplitvajsController(SplitvajsServiceImpl service) {
        this.service = Objects.requireNonNull(service, "SplitvajsService can not be null.");
    }

    @PostMapping("/expense")
    public ResponseEntity addExpense(@RequestBody ExpenseRequestModel request) {
        try {
            Expense added = service.addExpense(request);
            return ResponseEntity.ok(added);
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    private ResponseEntity<String> getExceptionResponse(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @PostMapping("/person")
    public ResponseEntity addPerson(@RequestBody PersonRequestModel request) {
        try {
            Person added = service.addPerson(request.name());
            return ResponseEntity.ok(added);
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @DeleteMapping("/expenses")
    public ResponseEntity clearAll() {
        try {
            service.clear();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @DeleteMapping("/expenses/{expenseId}")
    public ResponseEntity deleteExpense(@PathVariable(value = "expenseId") String expenseId) {
        try {
            service.removeExpense(expenseId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @GetMapping("/expenses")
    public ResponseEntity getExpenses() {
        try {
            return ResponseEntity.ok(service.getExpenses());
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @GetMapping("/expenses/{personId}")
    public ResponseEntity getExpenses(@PathVariable(value = "personId") String personId) {
        try {
            return ResponseEntity.ok(service.getExpenses(personId));
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @GetMapping("/persons")
    public ResponseEntity getPersons() {
        try {
            return ResponseEntity.ok(service.getPeople());
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @GetMapping("/result")
    public ResponseEntity getResult() {
        try {
            return ResponseEntity.ok(service.getResult());
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
