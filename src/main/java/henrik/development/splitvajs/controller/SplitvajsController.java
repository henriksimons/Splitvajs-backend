package henrik.development.splitvajs.controller;


import henrik.development.splitvajs.model.ExpenseItem;
import henrik.development.splitvajs.service.SplitvajsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RestController
public class SplitvajsController {

    private final SplitvajsService service;


    @Autowired
    public SplitvajsController(SplitvajsService service) {
        this.service = Objects.requireNonNull(service, "SplitvajsService can not be null.");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @GetMapping("/expenses")
    public ResponseEntity getExpenses() {
        try {
            Set<ExpenseItem> expenseItems = service.getAll();
            return ResponseEntity.ok(expenseItems);
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @GetMapping("/repayments/individual")
    public ResponseEntity getIndividualRepayments() {
        try {
            Map<String, Double> individualRepayments = service.getIndividualRepayments();
            return ResponseEntity.ok(individualRepayments);
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @GetMapping("/outcome")
    public ResponseEntity getRepaymentOutcome() {
        try {
            return ResponseEntity.ok(service.getOutcome());
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @PostMapping("/expense")
    public ResponseEntity addExpense(@RequestBody ExpenseItem item) {
        try {
            ExpenseItem addedItem = service.add(item);
            return ResponseEntity.ok(String.format("Expense item added: %s", addedItem));
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity remove(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.removeById(id));
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

    private ResponseEntity<String> getExceptionResponse(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
